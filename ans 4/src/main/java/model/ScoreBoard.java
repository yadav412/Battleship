package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tracks the opponent team's total score and win condition.
 * Manages scoring history, calculates statistics, and determines if opponents have won (score >= 2500).
 * Records points earned each turn from opponent water gun shots.
 */
public class ScoreBoard {
    private static final int WINNING_SCORE = 2500;

    private int totalScore;
    private List<Integer> scoreHistory;


    public ScoreBoard() {
        this.totalScore = 0;
        this.scoreHistory = new ArrayList<>();
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void addPoints(int points) {
        if (points > 0) {
            this.totalScore += points;
            this.scoreHistory.add(points);
        }
    }


    public void addOpponentScores(List<Integer> opponentScores) {
        int totalPoints = opponentScores.stream()
                .filter(score -> score > 0)
                .mapToInt(Integer::intValue)
                .sum();

        if (totalPoints > 0) {
            this.totalScore += totalPoints;
            this.scoreHistory.add(totalPoints);
        }
    }

    public List<Integer> getScoreHistory() {
        return scoreHistory.stream()
                .collect(Collectors.toList());
    }


    public double getAverageScorePerTurn() {
        if (scoreHistory.isEmpty()) {
            return 0.0;
        }

        return scoreHistory.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }


    public int getMaxScoreInTurn() {
        return scoreHistory.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
    }


    public boolean hasWon() {
        return totalScore >= WINNING_SCORE;
    }


    public int getWinningScore() {
        return WINNING_SCORE;
    }


    public int getPointsNeededToWin() {
        return Math.max(0, WINNING_SCORE - totalScore);
    }

    public int getTurnCount() {
        return (int) scoreHistory.stream()
                .count();
    }


    public List<Integer> getRecentScores(int count) {
        return scoreHistory.stream()
                .skip(Math.max(0, scoreHistory.size() - count))
                .collect(Collectors.toList());
    }


    public void reset() {
        this.totalScore = 0;
        this.scoreHistory.clear();
    }

    public String getSummary() {
        int turns = getTurnCount();
        double average = getAverageScorePerTurn();
        int maxScore = getMaxScoreInTurn();

        return String.format("Score: %d/%d | Turns: %d | Avg: %.1f | Max: %d",
                totalScore, WINNING_SCORE, turns, average, maxScore);
    }

    @Override
    public String toString() {
        return "ScoreBoard[Score: " + totalScore + "/" + WINNING_SCORE + "]";
    }
}


