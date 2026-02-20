package dto;

import model.Cell;

/**
 * Data Transfer Object for a single cell on the game board.
 * Contains cell coordinates, state, and fort association information.
 */
public class ApiCellDTO {
    public int row;
    public int col;
    public String state; // "UNKNOWN", "HIT", "MISS"
    public String fortId; // null if not part of a fort

    public static ApiCellDTO makeFromCell(Cell cell) {
        ApiCellDTO dto = new ApiCellDTO();
        dto.row = cell.getRow();
        dto.col = cell.getCol();
        dto.state = cell.getState().name();
        dto.fortId = cell.getFortId();
        return dto;
    }
}
