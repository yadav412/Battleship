package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a single opponent fort on the game board.
 * Manages the cells that make up the fort, tracks damage, and calculates points based on undamaged cells.
 * Each fort consists of 5 connected cells forming a polyomino shape.
 */
public class Fort {
    private final String fortId;
    private final List<Cell> cells;
    private final List<Cell> undamagedCells; // brians note


    public Fort(String fortId) {
        this.fortId = fortId;
        this.cells = new ArrayList<>();
        this.undamagedCells = new ArrayList<>();
    }


    public String getFortId() {
        return fortId;
    }


    public void addCell(Cell cell) {
        cells.add(cell);
        undamagedCells.add(cell);
        cell.setFortId(fortId);
    }

    public List<Cell> getCells() {
        return cells.stream()
                .collect(Collectors.toList());
    }


    public List<Cell> getUndamagedCells() {
        return undamagedCells.stream()
                .collect(Collectors.toList());
    }


    public int getUndamagedCellCount() {
        return undamagedCells.size();
    }


    public int getTotalCellCount() {
        return cells.size();
    }

    public int handleHit(Cell cell) {
        if (!cells.contains(cell)) {
            return 0; // Cell not part of this fort
        }

        if (cell.isHit()) {
            // Already hit, no additional damage but still earns points
            return calculatePoints();
        }

        // Mark cell as hit
        cell.markAsHit();
        undamagedCells.remove(cell);

        return calculatePoints();
    }


    private int calculatePoints() {
        int undamagedCount = getUndamagedCellCount();

        // Points based on undamaged cells (5=20, 4=20, 3=5, 2=2, 1=1, 0=0)
        switch (undamagedCount) {
            case 5:
            case 4:
                return 20;
            case 3:
                return 5;
            case 2:
                return 2;
            case 1:
                return 1;
            case 0:
                return 0;
            default:
                return 0;
        }
    }

    public boolean isDestroyed() {
        return undamagedCells.isEmpty();
    }


    public boolean containsCell(Cell cell) {
        return cells.contains(cell);
    }


    public int getPotentialPoints() {
        return calculatePoints();
    }

    @Override
    public String toString() {
        return "Fort(" + fortId + ")[" + getUndamagedCellCount() + "/" + getTotalCellCount() + "]";
    }
}


