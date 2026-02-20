package controller;

import dto.*;
import model.GameEngine;
import model.ShotScore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for the Blanket Fort Game API.
 * Handles all HTTP requests for game management, board state, moves, and cheat
 * states.
 * Provides endpoints for creating games, making moves, viewing boards, and
 * managing game state.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GameController {
    private final List<GameEngine> games = new ArrayList<>();
    private final Map<Integer, Integer> gameShotCounts = new HashMap<>();
    private final Map<Integer, List<Integer>> lastOpponentScores = new HashMap<>();
    private final Map<Integer, Boolean> cheatModes = new HashMap<>();
    private static final int DEFAULT_NUM_OPPONENTS = 5;

    @GetMapping("/about")
    public ResponseEntity<String> getAbout() {
        return ResponseEntity.ok("Yadav Singh");
    }

    @GetMapping("/games")
    public ResponseEntity<List<ApiGameDTO>> getAllGames() {
        List<ApiGameDTO> gameList = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            GameEngine game = games.get(i);
            int numShots = gameShotCounts.getOrDefault(i, 0);
            List<Integer> lastScores = lastOpponentScores.getOrDefault(i, new ArrayList<>());
            ApiGameDTO dto = ApiGameDTO.makeFromGame(game, i, numShots, lastScores);
            gameList.add(dto);
        }
        return ResponseEntity.ok(gameList);
    }

    @PostMapping("/games")
    public ResponseEntity<ApiGameDTO> createGame() {
        int opponents = DEFAULT_NUM_OPPONENTS;

        try {
            GameEngine game = new GameEngine(opponents);
            int gameNumber = games.size();
            games.add(game);
            gameShotCounts.put(gameNumber, 0);
            lastOpponentScores.put(gameNumber, new ArrayList<>());
            cheatModes.put(gameNumber, false);

            ApiGameDTO dto = ApiGameDTO.makeFromGame(game, gameNumber, 0, new ArrayList<>());
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/games/{gameNumber}")
    public ResponseEntity<ApiGameDTO> getGame(@PathVariable int gameNumber) {
        if (gameNumber < 0 || gameNumber >= games.size()) {
            return ResponseEntity.notFound().build();
        }

        GameEngine game = games.get(gameNumber);
        int numShots = gameShotCounts.getOrDefault(gameNumber, 0);
        List<Integer> lastScores = lastOpponentScores.getOrDefault(gameNumber, new ArrayList<>());
        ApiGameDTO dto = ApiGameDTO.makeFromGame(game, gameNumber, numShots, lastScores);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/games/{gameNumber}/board")
    public ResponseEntity<ApiBoardDTO> getBoard(@PathVariable int gameNumber) {
        if (gameNumber < 0 || gameNumber >= games.size()) {
            return ResponseEntity.notFound().build();
        }

        GameEngine game = games.get(gameNumber);
        boolean cheatMode = cheatModes.getOrDefault(gameNumber, false);
        ApiBoardDTO dto = ApiBoardDTO.makeFromGame(game.getMap(), cheatMode);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/games/{gameNumber}/cheatstate")
    public ResponseEntity<Void> setCheatState(@PathVariable int gameNumber, @RequestBody String cheatCommand) {
        if (gameNumber < 0 || gameNumber >= games.size()) {
            return ResponseEntity.notFound().build();
        }

        String trimmed = cheatCommand != null ? cheatCommand.trim() : "";
        // Remove surrounding quotes if present
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }

        if ("SHOW_ALL".equals(trimmed)) {
            cheatModes.put(gameNumber, true);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/games/{gameNumber}/moves")
    public ResponseEntity<Void> processMove(@PathVariable int gameNumber,
            @RequestBody ApiLocationDTO location) {
        if (gameNumber < 0 || gameNumber >= games.size()) {
            return ResponseEntity.notFound().build();
        }

        if (location == null || location.row < 0 || location.row >= 10 ||
                location.col < 0 || location.col >= 10) {
            return ResponseEntity.badRequest().build();
        }

        GameEngine game = games.get(gameNumber);

        // Check if game is over
        if (game.isGameOver()) {
            return ResponseEntity.badRequest().build();
        }

        // Convert row/col to coordinate string (e.g., row=1, col=4 -> "B5")
        String coordinate = convertRowColToCoordinate(location.row, location.col);

        // Process the shot
        ShotScore shotScore = game.processPlayerShot(coordinate);

        // Update shot count and store last opponent scores
        int currentShots = gameShotCounts.getOrDefault(gameNumber, 0);
        gameShotCounts.put(gameNumber, currentShots + 1);
        lastOpponentScores.put(gameNumber, new ArrayList<>(shotScore.getOpponentScores()));

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    private String convertRowColToCoordinate(int row, int col) {
        char letter = (char) ('A' + row);
        int number = col + 1;
        return letter + String.valueOf(number);
    }

    @GetMapping("/games/{gameNumber}/opponents")
    public ResponseEntity<List<ApiOpponentDTO>> getOpponents(@PathVariable int gameNumber) {
        if (gameNumber < 0 || gameNumber >= games.size()) {
            return ResponseEntity.notFound().build();
        }

        GameEngine game = games.get(gameNumber);
        List<ApiOpponentDTO> dtoList = ApiOpponentDTO.makeFromOpponents(game.getOpponents());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/games/{gameNumber}/scoreboard")
    public ResponseEntity<ApiScoreBoardDTO> getScoreboard(@PathVariable int gameNumber) {
        if (gameNumber < 0 || gameNumber >= games.size()) {
            return ResponseEntity.notFound().build();
        }

        GameEngine game = games.get(gameNumber);
        ApiScoreBoardDTO dto = ApiScoreBoardDTO.makeFromScoreBoard(game.getScoreBoard());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/games/{gameNumber}/shots")
    public ResponseEntity<ApiShotDTO> processShot(@PathVariable int gameNumber,
            @RequestBody ApiShotRequestDTO shotRequest) {
        if (gameNumber < 0 || gameNumber >= games.size()) {
            return ResponseEntity.notFound().build();
        }

        if (shotRequest == null || shotRequest.shot == null || shotRequest.shot.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        GameEngine game = games.get(gameNumber);

        // Check if game is over
        if (game.isGameOver()) {
            return ResponseEntity.badRequest().build();
        }

        // Process the shot
        ShotScore shotScore = game.processPlayerShot(shotRequest.shot);

        // Update shot count
        int currentShots = gameShotCounts.getOrDefault(gameNumber, 0);
        gameShotCounts.put(gameNumber, currentShots + 1);
        lastOpponentScores.put(gameNumber, new ArrayList<>(shotScore.getOpponentScores()));

        ApiShotDTO dto = ApiShotDTO.makeFromShotScore(shotScore);
        return ResponseEntity.ok(dto);
    }
}
