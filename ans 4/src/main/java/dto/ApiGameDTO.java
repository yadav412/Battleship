package dto;

import model.GameEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for game state information.
 * Contains game status, win/loss conditions, opponent points, and active fort count.
 */
public class ApiGameDTO {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int opponentPoints;
    public long numActiveOpponentForts;

    // Amount of points that the opponents scored on the last time they fired.
    // If opponents have not yet fired, then it should be an empty array (0 size).
    public int[] lastOpponentPoints;

    public static ApiGameDTO makeFromGame(GameEngine game, int gameNumber, int numShots, List<Integer> lastOpponentScores) {
        ApiGameDTO dto = new ApiGameDTO();
        dto.gameNumber = gameNumber;
        dto.isGameWon = game.getGameState() == GameEngine.GameState.PLAYER_WON;
        dto.isGameLost = game.getGameState() == GameEngine.GameState.OPPONENTS_WON;
        dto.opponentPoints = game.getScoreBoard().getTotalScore();
        dto.numActiveOpponentForts = game.getOpponents().stream()
                .filter(opponent -> !opponent.isDestroyed())
                .count();
        
        // Convert List<Integer> to int[]
        if (lastOpponentScores != null && !lastOpponentScores.isEmpty()) {
            dto.lastOpponentPoints = lastOpponentScores.stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
        } else {
            dto.lastOpponentPoints = new int[0];
        }
        
        return dto;
    }
}

