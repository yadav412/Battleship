package model;

/**
 * Represents a single cell on the game board.
 * Stores cell coordinates (row, col), state (UNKNOWN, HIT, MISS), and optional fort association.
 * Cells can be marked as hit or miss when shot at by the player.
 */
public class Cell {
    private final int row;
    private final int col;
    private CellState state;
    private String fortId; // null if not part of a fort

    public enum CellState {
        UNKNOWN, // ~ (fog)
        HIT, // X (destroyed fort block)
        MISS // (space) (grass)
    }


    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.state = CellState.UNKNOWN;
        this.fortId = null;
    }


    public int getRow() {
        return row;
    }


    public int getCol() {
        return col;
    }

    public CellState getState() {
        return state;
    }


    public void setState(CellState state) {
        this.state = state;
    }


    public String getFortId() {
        return fortId;
    }


    public void setFortId(String fortId) {
        this.fortId = fortId;
    }

    public boolean isPartOfFort() {
        return fortId != null;
    }

    public void markAsHit() {
        this.state = CellState.HIT;
    }

    public void markAsMiss() {
        this.state = CellState.MISS;
    }


    public boolean isHit() {
        return state == CellState.HIT;
    }


    public boolean hasBeenShot() {
        return state == CellState.HIT || state == CellState.MISS;
    }

    @Override
    public String toString() {
        return "Cell(" + row + "," + col + ")[" + state + "]";
    }
}


