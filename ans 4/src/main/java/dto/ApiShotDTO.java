package dto;

import model.ShotScore;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for a shot result.
 * Contains information about whether the shot hit, if it was already shot,
 * opponent scores, and game state.
 */
public class ApiShotDTO {
    public boolean isHit;
    public boolean wasAlreadyShot;
    public List<Integer> opponentScores;
    public String gameState; // "IN_PROGRESS", "PLAYER_WON", "OPPONENTS_WON"

    public static ApiShotDTO makeFromShotScore(ShotScore shotScore) {
        ApiShotDTO dto = new ApiShotDTO();
        dto.isHit = shotScore.isHit();
        dto.wasAlreadyShot = shotScore.wasAlreadyShot();
        dto.opponentScores = new ArrayList<>(shotScore.getOpponentScores());
        dto.gameState = shotScore.getGameState().name();
        return dto;
    }
}
