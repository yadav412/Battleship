package model;

/**
 * Represents the game board as a 2D grid of cells.
 * Manages cell access by row/column indices or coordinate strings (e.g., "B5").
 * Provides validation for cell coordinates and positions.
 */
public class Map {
    private static final int BOARD_SIZE = 10; // (10x10) board size (indices: 0-9)
    private final Cell[][] map;

    public Map() {
        // generates 2D array (map) that can hold cells
        this.map = new Cell[BOARD_SIZE][BOARD_SIZE];
        // generate cells and store in map
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                map[row][col] = new Cell(row, col);
            }
        }
    }

    public Cell getCellByRowCol(int row, int col) {
        return map[row][col];
    }

    public Cell getCellByCoordinates(String coordinates) {
        // parse "B5" -> get Cell (1, 4)
        int row = parseRow(coordinates);
        int col = parseCol(coordinates);

        return getCellByRowCol(row, col);
    }

    public boolean isCellRowColValid(int row, int col) {
        return (row >= 0 && row < BOARD_SIZE) && (col >= 0 && col < BOARD_SIZE);
    }

    public boolean isCellCoordinatesValid(String coordinates) {
        // valid coordinates are length 2 (e.g. C3) or 3 (B10)
        if (coordinates == null || coordinates.length() < 2 || coordinates.length() > 3) {
            return false;
        }
        try {
            int row = parseRow(coordinates);
            int col = parseCol(coordinates);
            return isCellRowColValid(row, col);
        } catch (Exception e) {
            return false;
        }
    }

    public Cell[][] getMap() {
        return map;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    private int parseRow(String coordinate) {
        char letter = coordinate.charAt(0);
        // normalize by making it a capital letter
        char upperLetter = Character.toUpperCase(letter);
        // ascii value for 'A' is 65, subtraction results in correct letter number for row
        return upperLetter - 'A';
    }

    private int parseCol(String coordinate) {
        // makes a substring starting at index 1 of the string
        String numberPart = coordinate.substring(1);
        // subtract 1 to standardize index starting at 0
        return (Integer.parseInt(numberPart) - 1);
    }

}


