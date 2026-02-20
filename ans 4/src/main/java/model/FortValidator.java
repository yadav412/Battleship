package model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Validates fort placement on the game board.
 * Ensures forts are within bounds, form valid connected polyominos, and don't overlap with existing forts.
 * Used during game initialization to place forts correctly.
 */
public class FortValidator {
    private static final int BOARD_SIZE = 10; // 10x10 board


    public boolean isValidPlacement(List<Cell> fortCells, int boardSize) {
        if (fortCells == null || fortCells.isEmpty()) {
            return false;
        }

        // Check if all cells are within bounds using streams
        boolean allWithinBounds = fortCells.stream()
                .allMatch(cell -> isWithinBounds(cell, boardSize));
        if (!allWithinBounds) {
            return false;
        }

        // Check if the fort is a valid polyomino (all cells connected)
        if (!isValidPolyomino(fortCells)) {
            return false;
        }

        return true;
    }

    public boolean isValidPlacement(List<Cell> fortCells, List<List<Cell>> existingForts, int boardSize) {
        // First check basic validity
        if (!isValidPlacement(fortCells, boardSize)) {
            return false;
        }

        // Check for overlaps with existing forts using streams
        boolean hasAnyOverlap = existingForts.stream()
                .anyMatch(existingFort -> hasOverlap(fortCells, existingFort));
        if (hasAnyOverlap) {
            return false;
        }

        return true;
    }


    public boolean isWithinBounds(Cell cell, int boardSize) {
        return cell.getRow() >= 0 && cell.getRow() < boardSize &&
                cell.getCol() >= 0 && cell.getCol() < boardSize;
    }


    public boolean isWithinBounds(Cell cell) {
        return isWithinBounds(cell, BOARD_SIZE);
    }


    public boolean isValidPolyomino(List<Cell> fortCells) {
        if (fortCells.size() < 2) {
            return true; // Single cell is always valid
        }

        // Use a simple connectivity check - all cells should be reachable from the
        // first cell
        return isConnected(fortCells);
    }


    private boolean isConnected(List<Cell> fortCells) {
        if (fortCells.isEmpty()) {
            return true;
        }

        // Use a simple flood-fill approach to check connectivity
        boolean[] visited = new boolean[fortCells.size()];
        java.util.Queue<Integer> queue = new java.util.LinkedList<>();
        queue.offer(0);
        visited[0] = true;
        int visitedCount = 1;

        while (!queue.isEmpty()) {
            int currentIndex = queue.poll();
            Cell currentCell = fortCells.get(currentIndex);

            // Check all other cells for adjacency
            for (int i = 0; i < fortCells.size(); i++) {
                if (!visited[i] && areAdjacent(currentCell, fortCells.get(i))) {
                    visited[i] = true;
                    queue.offer(i);
                    visitedCount++;
                }
            }
        }

        return visitedCount == fortCells.size();
    }


    private boolean areAdjacent(Cell cell1, Cell cell2) {
        int rowDiff = Math.abs(cell1.getRow() - cell2.getRow());
        int colDiff = Math.abs(cell1.getCol() - cell2.getCol());
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }


    public boolean hasOverlap(List<Cell> fort1Cells, List<Cell> fort2Cells) {
        for (Cell cell1 : fort1Cells) {
            for (Cell cell2 : fort2Cells) {
                if (cell1.getRow() == cell2.getRow() && cell1.getCol() == cell2.getCol()) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean canPlaceFortAt(List<Cell> fortCells, int startRow, int startCol, int boardSize) {
        // Create positioned cells using streams
        List<Cell> positionedCells = fortCells.stream()
                .map(cell -> new Cell(cell.getRow() + startRow, cell.getCol() + startCol))
                .collect(Collectors.toList());

        return isValidPlacement(positionedCells, boardSize);
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }
}


