package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Card> cards = mapGamesFromInput(readFile());
        int totalScore = calculateTotalScore(cards);
        int sumOfMatches = calculateNumberOfCards(cards);

        System.out.println(totalScore);
        System.out.println(sumOfMatches);
    }

    private static int calculateNumberOfCards(List<Card> cards) {
        int cardCount[] = new int[cards.size()];

        Arrays.fill(cardCount, 1);

        List<Integer> numberOfMatches = cards
                .stream()
                .map(Card::getNumberOfMatches)
                .toList();

        for(int i = 0; i < cards.size() - 1; i++) {
            int cardMatches = numberOfMatches.get(i);
            int extraCards = cardCount[i];

            for (int j = 1; j <= cardMatches ; j++) {
                cardCount[i + j] += extraCards;
            }
        }

//        3286321
        return Arrays.stream(cardCount).sum();
    }


    private static List<Card> mapGamesFromInput(List<String> input) {
        List<Card> cards = new ArrayList<>();

        for(String line : input) {
            int gameId = getGameId(line);
            List<Integer> winningNumbers = getWinningNumbers(line);
            List<Integer> ownedNumbers = getOwnedNumbers(line);

            cards.add(new Card(gameId, winningNumbers, ownedNumbers));
        }

        return cards;
    }

    private static int calculateTotalScore(List<Card> cards) {
        int totalScore = 0;

        for(Card card : cards) {
            totalScore += card.getScore();
        }

        return totalScore;
    }

    private static List<Integer> getWinningNumbers(String line) {
        String numbersPart = line.split(":")[1];
        String winningNumbersPart = numbersPart.split("\\|")[0].trim();
        String[] winningNumbers = winningNumbersPart.split(" ");

        return Arrays.stream(winningNumbers)
                .filter(value -> !value.isEmpty())
                .map(Integer::parseInt)
                .toList();
    }

    private static List<Integer> getOwnedNumbers(String line) {
        String numbersPart = line.split(":")[1];
        String ownedNumbersPart = numbersPart.split("\\|")[1];
        String[] ownedNumbers = ownedNumbersPart.split(" ");

        return Arrays.stream(ownedNumbers)
                .filter(value -> !value.isEmpty())
                .map(Integer::parseInt)
                .toList();
    }

    private static int getGameId(String line) {
        String idPart = line.split(":")[0];

        return Integer.parseInt(idPart.replaceAll("[^0-9]", ""));
    }

    private static List<String> readFile() throws Exception{
        return Files.readAllLines(
                Paths.get("C:\\Users\\viome\\Desktop\\JavaThings\\AoC_2023\\untitled\\src\\main\\Day4\\src\\main\\java\\org\\example\\input.txt")
        );
    }
}