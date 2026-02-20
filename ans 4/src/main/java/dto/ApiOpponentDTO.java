package dto;

import model.Opponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for an opponent information.
 * Contains opponent ID, fort ID, cell damage status, and destruction state.
 */
public class ApiOpponentDTO {
    public String opponentId;
    public String fortId;
    public int undamagedCellCount;
    public int totalCellCount;
    public boolean isDestroyed;

    public static ApiOpponentDTO makeFromOpponent(Opponent opponent) {
        ApiOpponentDTO dto = new ApiOpponentDTO();
        dto.opponentId = opponent.getOpponentId();
        dto.fortId = opponent.getFort().getFortId();
        dto.undamagedCellCount = opponent.getUndamagedCellCount();
        dto.totalCellCount = opponent.getTotalCellCount();
        dto.isDestroyed = opponent.isDestroyed();
        return dto;
    }

    public static List<ApiOpponentDTO> makeFromOpponents(List<Opponent> opponents) {
        List<ApiOpponentDTO> dtoList = new ArrayList<>();
        for (Opponent opponent : opponents) {
            dtoList.add(makeFromOpponent(opponent));
        }
        return dtoList;
    }
}


