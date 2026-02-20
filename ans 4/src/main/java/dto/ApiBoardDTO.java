package dto;

import model.Cell;
import model.Map;

/**
 * Data Transfer Object for the game board state.
 * Contains a 2D array of cell states representing the current board
 * configuration.
 * Supports both normal mode (fog/hit/miss) and cheat mode
 * (fort/field/hit/miss).
 */
public class ApiBoardDTO {
    public int boardWidth;
    public int boardHeight;

    // cellState[row][col] = {"fog", "hit", "fort", "miss", "field"}
    public String[][] cellStates;

    public static ApiBoardDTO makeFromGame(Map map, boolean cheatMode) {
        ApiBoardDTO dto = new ApiBoardDTO();
        int boardSize = map.getBoardSize();
        dto.boardWidth = boardSize;
        dto.boardHeight = boardSize;

        Cell[][] cells = map.getMap();
        dto.cellStates = new String[boardSize][boardSize];

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Cell cell = cells[row][col];
                String state;

                if (cheatMode) {
                    // In cheat mode, show all cells
                    if (cell.getState() == Cell.CellState.HIT) {
                        state = "hit";
                    } else if (cell.getState() == Cell.CellState.MISS) {
                        state = "miss";
                    } else if (cell.isPartOfFort()) {
                        state = "fort";
                    } else {
                        state = "field";
                    }
                } else {
                    // Normal mode: only show explored cells
                    if (cell.getState() == Cell.CellState.HIT) {
                        state = "hit";
                    } else if (cell.getState() == Cell.CellState.MISS) {
                        state = "miss";
                    } else {
                        state = "fog";
                    }
                }

                dto.cellStates[row][col] = state;
            }
        }

        return dto;
    }
}

// lol lmao