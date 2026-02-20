package dto;

import model.ScoreBoard;

/**
 * Data Transfer Object for the scoreboard information.
 * Contains total score, winning score threshold, points needed to win, and turn count.
 */
public class ApiScoreBoardDTO {
    public int totalScore;
    public int winningScore;
    public int pointsNeededToWin;
    public int turnCount;

    public static ApiScoreBoardDTO makeFromScoreBoard(ScoreBoard scoreBoard) {
        ApiScoreBoardDTO dto = new ApiScoreBoardDTO();
        dto.totalScore = scoreBoard.getTotalScore();
        dto.winningScore = scoreBoard.getWinningScore();
        dto.pointsNeededToWin = scoreBoard.getPointsNeededToWin();
        dto.turnCount = scoreBoard.getTurnCount();
        return dto;
    }
}


