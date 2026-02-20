package model;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Core game engine that coordinates the water fight game logic.
 * Manages the map, forts, opponents, scoring, and turn sequence.
 */
public class GameEngine {
    private final Map map;
    private final List<Fort> fortsOnMap;
    private final ScoreBoard scoreBoard;
    private final List<Opponent> opponents;
    private GameState gameState;

    // status of game
    public enum GameState {
        IN_PROGRESS,
        PLAYER_WON,
        OPPONENTS_WON
    }

    // Creates a new game with N opponents, if all forts cannot be placed, an
    // exception is thrown
    public GameEngine(int numOfOpponents) throws Exception {
        this.map = new Map();
        this.scoreBoard = new ScoreBoard();

        // Place forts on map using a FortPlacer object
        FortPlacer placer = new FortPlacer();
        this.fortsOnMap = placer.placeForts(map, numOfOpponents);

        // Create list of opponents
        this.opponents = createOpponents(fortsOnMap);

        // GameState is IN_PROGRESS when all forts can be placed on map and game has
        // been created
        this.gameState = GameState.IN_PROGRESS;
    }

    // Create a list of opponents, where each opponent has a fort and an opponentId
    private List<Opponent> createOpponents(List<Fort> fortsOnMap) {
        List<Opponent> opponents = new ArrayList<>();
        for (int i = 0; i < fortsOnMap.size(); i++) {
            String opponentId = "#" + (i + 1);
            opponents.add(new Opponent(opponentId, fortsOnMap.get(i)));
        }
        return opponents;
    }

    /**
     * - Player actions -
     * Processes a player's shot at the coordinates given
     * Returns the score caused by the shot (shot can be hit or miss)
     */
    public ShotScore processPlayerShot(String shotCoordinates) {
        // 1. validate coordinates of shot - make sure it's a cell on the map

        // if coordinates are not on map, fail early fail fast
        if (!map.isCellCoordinatesValid(shotCoordinates)) {
            // report a miss
            // keep ShotScore the same
            return new ShotScore(false, false, new ArrayList<>(), gameState);
        }

        // 2. Get cell
        Cell cell = map.getCellByCoordinates(shotCoordinates);

        // 3. Check if the cell has already been shot
        boolean wasAlreadyShot = cell.hasBeenShot();

        // 4. Determine if it is part of a fort
        boolean isHit = cell.isPartOfFort();

        // 5. mark cell and tell fort one of its cells have been hit
        if (isHit) {
            cell.markAsHit();

            // If first time, tell the Opponent's fort that it's cell was soaked
            if (!wasAlreadyShot) {
                String fortId = cell.getFortId();
                for (Opponent opponent : opponents) {
                    if (opponent.getFort().getFortId().equals(fortId)) {
                        opponent.handleFortHit(cell);
                        break;
                    }
                }
            }
        } else {
            cell.markAsMiss();
        }
        // 6. Opponents fire back
        List<Integer> opponentScores = processOpponentShots();

        // 7. Check win/loss
        updateGameState();

        // 8. Return results
        return new ShotScore(isHit, wasAlreadyShot, opponentScores, gameState);
    }

    private List<Integer> processOpponentShots() {
        // Use stream to collect scores from each opponent
        List<Integer> scores = opponents.stream()
                .map(opponent -> opponent.canFire() ? opponent.fireWaterGun() : 0)
                .filter(score -> score > 0)
                .collect(Collectors.toList());

        // Add scores to the scoreboard
        scoreBoard.addOpponentScores(scores);

        return scores;
    }

    private void updateGameState() {
        if (hasPlayerWon()) {
            gameState = GameState.PLAYER_WON;
        } else if (haveOpponentsWon()) {
            gameState = GameState.OPPONENTS_WON;
        }
        // Otherwise stays IN_PROGRESS
    }

    // Checks if player has won (all forts destroyed).
    private boolean hasPlayerWon() {
        // Use stream to check if all opponents are destroyed
        return opponents.stream()
                .allMatch(opponent -> opponent.isDestroyed());
    }

    // Checks if opponents have won (their score is >= 2500).
    private boolean haveOpponentsWon() {
        return scoreBoard.hasWon();
    }

    // below are trivial getter methods to allow TextUI to access game information
    public Map getMap() {
        return map;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public List<Opponent> getOpponents() {
        return opponents;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isGameOver() {
        return gameState != GameState.IN_PROGRESS;
    }

    public int getNumOpponents() {
        return opponents.size();
    }
}
