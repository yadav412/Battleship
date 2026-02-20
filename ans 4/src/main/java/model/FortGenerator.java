package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Generates random polyomino shapes for forts.
 * Creates connected 5-cell shapes using a random walk algorithm.
 * Can generate random shapes or specific predefined shapes (line, L, T, plus).
 */
public class FortGenerator {
    private static final int FORT_SIZE = 5; // Each fort has 5 cells
    private final Random random;


    public FortGenerator() {
        this.random = new Random();
    }


    public FortGenerator(long seed) {
        this.random = new Random(seed);
    }


    public List<Cell> generateFortShape(String fortId) {
        List<Cell> fortCells = new ArrayList<>();

        // Start with a single cell at the origin
        Cell startCell = new Cell(0, 0);
        fortCells.add(startCell);

        // Generate the remaining cells using a random walk approach
        while (fortCells.size() < FORT_SIZE) {
            Cell newCell = generateNextCell(fortCells);
            if (newCell != null) {
                fortCells.add(newCell);
            } else {
                // If we can't generate a valid next cell, restart
                return generateFortShape(fortId);
            }
        }

        return fortCells;
    }

    private Cell generateNextCell(List<Cell> existingCells) {
        List<Cell> candidates = new ArrayList<>();

        // Find all adjacent cells that aren't already in the fort
        for (Cell cell : existingCells) {
            // Check all 4 directions
            int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

            for (int[] dir : directions) {
                int newRow = cell.getRow() + dir[0];
                int newCol = cell.getCol() + dir[1];
                Cell candidate = new Cell(newRow, newCol);

                // Check if this candidate is valid
                if (!existingCells.contains(candidate)) {
                    candidates.add(candidate);
                }
            }
        }

        // If no candidates, return null
        if (candidates.isEmpty()) {
            return null;
        }

        // Randomly select a candidate
        return candidates.get(random.nextInt(candidates.size()));
    }


    public List<Cell> generateFortShapeAtPosition(String fortId, int startRow, int startCol) {
        List<Cell> fortCells = generateFortShape(fortId);

        // Translate all cells to the specified position using streams
        return fortCells.stream()
                .map(cell -> new Cell(cell.getRow() + startRow, cell.getCol() + startCol))
                .collect(Collectors.toList());
    }


    public List<List<Cell>> generateMultipleFortShapes(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    String fortId = String.valueOf((char) ('A' + i)); // A, B, C, etc.
                    return generateFortShape(fortId);
                })
                .collect(Collectors.toList());
    }


    public List<Cell> generateSpecificShape(String shapeType, String fortId) {
        List<Cell> cells = new ArrayList<>();

        switch (shapeType.toLowerCase()) {
            case "line":
                // Horizontal line using streams
                cells.addAll(IntStream.range(0, FORT_SIZE)
                        .mapToObj(i -> new Cell(0, i))
                        .collect(Collectors.toList()));
                break;
            case "l":
                // L shape
                cells.add(new Cell(0, 0));
                cells.add(new Cell(1, 0));
                cells.add(new Cell(2, 0));
                cells.add(new Cell(2, 1));
                cells.add(new Cell(2, 2));
                break;
            case "t":
                // T shape
                cells.add(new Cell(0, 1));
                cells.add(new Cell(1, 0));
                cells.add(new Cell(1, 1));
                cells.add(new Cell(1, 2));
                cells.add(new Cell(2, 1));
                break;
            case "plus":
                // Plus shape
                cells.add(new Cell(0, 1));
                cells.add(new Cell(1, 0));
                cells.add(new Cell(1, 1));
                cells.add(new Cell(1, 2));
                cells.add(new Cell(2, 1));
                break;
            default:
                // Default to random shape
                return generateFortShape(fortId);
        }

        return cells;
    }


    public int getFortSize() {
        return FORT_SIZE;
    }
}


