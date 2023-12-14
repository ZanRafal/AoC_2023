package org.example;

import java.util.List;

public class Card {
    private int cardId;
    private List<Integer> winningNumbers;
    private List<Integer> ownedNumbers;
    private int score;

    public int getNumberOfMatches() {
        return numberOfMatches;
    }

    public void setNumberOfMatches(int numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    private int numberOfMatches = 0;

    public int getCardId() {
        return cardId;
    }

    public int getScore() {
        return score;
    }

    public Card(int cardId, List<Integer> winningNumbers, List<Integer> ownedNumbers) {
        this.cardId = cardId;
        this.winningNumbers = winningNumbers;
        this.ownedNumbers = ownedNumbers;
        this.score = calculateScore();
    }

    private int calculateScore() {
        int score = 0;

        for(int number : ownedNumbers) {
            if(winningNumbers.contains(number)) {
                if(score == 0) {
                    score = 1;
                } else {
                    score *= 2;
                }
                numberOfMatches++;
            }
        }

        return score;
    }

}
