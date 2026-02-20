package model;
import java.util.List;

/**
 * Represents the result of a single player shot during a turn.
 * Contains information about whether the shot hit, if the cell was already shot,
 * opponent scores from their return fire, and the current game state.
 */
public class ShotScore {
    private final boolean isHit;
    private final boolean wasAlreadyShot;
    private final List<Integer> opponentScores;
    private final GameEngine.GameState gameState;

    public ShotScore(boolean isHit, boolean wasAlreadyShot,
                     List<Integer> opponentScores, GameEngine.GameState gameState) {
        this.isHit = isHit;
        this.wasAlreadyShot = wasAlreadyShot;
        this.opponentScores = opponentScores;
        this.gameState = gameState;
    }

    public boolean isHit() { return isHit; }
    public boolean wasAlreadyShot() { return wasAlreadyShot; }
    public List<Integer> getOpponentScores() { return opponentScores; }
    public GameEngine.GameState getGameState() { return gameState; }
}


