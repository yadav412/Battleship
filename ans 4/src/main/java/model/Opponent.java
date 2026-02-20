package model;

/**
 * Represents a single computer-controlled opponent in the game.
 * Each opponent owns a Fort and can fire water gun shots to score points against the player.
 * Tracks destruction state and manages fort damage.
 */
public class Opponent {
    private final String opponentId;
    private final Fort fort;
    private boolean isDestroyed;

    /**
     * Creates a new opponent with the given ID and fort.
     *
     * @param opponentId unique identifier for this opponent
     * @param fort       the fort owned by this opponent
     */
    public Opponent(String opponentId, Fort fort) {
        this.opponentId = opponentId;
        this.fort = fort;
        this.isDestroyed = false;
    }


    public String getOpponentId() {
        return opponentId;
    }


    public Fort getFort() {
        return fort;
    }


    public int fireWaterGun() {
        if (isDestroyed) {
            return 0; // Destroyed opponents don't fire
        }

        // Opponent always hits and scores points based on fort condition
        return fort.getPotentialPoints();
    }


    public int handleFortHit(Cell cell) {
        if (isDestroyed) {
            return 0; // Destroyed opponents don't earn points
        }

        int pointsEarned = fort.handleHit(cell);

        // Check if fort is now destroyed
        if (fort.isDestroyed()) {
            isDestroyed = true;
        }

        return pointsEarned;
    }


    public boolean isDestroyed() {
        return isDestroyed || fort.isDestroyed();
    }


    public boolean canFire() {
        return !isDestroyed();
    }


    public int getUndamagedCellCount() {
        return fort.getUndamagedCellCount();
    }


    public int getTotalCellCount() {
        return fort.getTotalCellCount();
    }


    public int getPotentialPoints() {
        if (isDestroyed) {
            return 0;
        }
        return fort.getPotentialPoints();
    }

    @Override
    public String toString() {
        return "Opponent(" + opponentId + ")[Fort: " + fort.getFortId() +
                ", Destroyed: " + isDestroyed + "]";
    }
}


