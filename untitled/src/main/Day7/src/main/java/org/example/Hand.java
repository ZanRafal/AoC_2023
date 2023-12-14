package org.example;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static org.example.CardValues.CARD_STRENGTHS;

public class Hand implements Comparable<Hand> {
    private final String hand;

    public int getBid() {
        return bid;
    }

    private final int bid;

    public Hand(String hand, int bid) {
        this.hand = hand;
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "hand='" + hand + '\'' +
                ", bid=" + bid +
                '}';
    }

    @Override
    public int compareTo(Hand o) {
        char[] thisCharArray = this.hand.toCharArray();
        char[] thatCharArray = o.hand.toCharArray();

//        Part 1
//        var thisMap = getMapOfCards(thisCharArray).values().stream().sorted(Comparator.comparing(Integer::intValue).reversed()).toList();
//        var thatMap = getMapOfCards(thatCharArray).values().stream().sorted(Comparator.comparing(Integer::intValue).reversed()).toList();

        char[] thisWithoutJokers = replaceBestCardWIthJoker(thisCharArray);
        char[] thatWithoutJokers = replaceBestCardWIthJoker(thatCharArray);

        var thisMapWithoutJokers = getMapOfCards(thisWithoutJokers).values().stream().sorted(Comparator.comparing(Integer::intValue).reversed()).toList();
        var thatMapWithoutJokers = getMapOfCards(thatWithoutJokers).values().stream().sorted(Comparator.comparing(Integer::intValue).reversed()).toList();

        var minSize = Math.min(thisMapWithoutJokers.size(), thatMapWithoutJokers.size());

        for(int i = 0; i < minSize; i++) {
            if (thisMapWithoutJokers.get(i) > thatMapWithoutJokers.get(i)) {
                return 1;
            } else if (thisMapWithoutJokers.get(i) < thatMapWithoutJokers.get(i)) {
                return -1;
            }
        }

        return checkIfTheSameCards(this.hand.toCharArray(), o.hand.toCharArray());
    }

    private int checkIfTheSameCards(char[] thisHand, char[] thatHand){
        var size = Math.min(thisHand.length, thatHand.length);

        for(int i = 0; i < size; i++){
            var thisCard = CARD_STRENGTHS.get(String.valueOf(thisHand[i]));
            var thatCard = CARD_STRENGTHS.get(String.valueOf(thatHand[i]));
            if(!thisCard.equals(thatCard)){
                return thisCard > thatCard ? 1 : -1;
            }
        }
        return 0;
    }

    private Map<String, Integer> getMapOfCards(char[] hand) {
        Map<String, Integer> result = new HashMap<>();

        for (char card : hand) {
            String cardKey = String.valueOf(card);
            result.put(cardKey, result.getOrDefault(cardKey, 0) + 1);
        }

        return result;
    }

    private boolean containsJ(char[] hand){
        for (char c : hand) {
            if(c == 'J'){
                return true;
            }
        }
        return false;
    }

    private char[] replaceBestCardWIthJoker(char[] hand) {
        if (!containsJ(hand)) {
            return hand;
        }

        Map<String, Integer> sortedCardMap = getSortedCardMap(hand);
        String bestCard = getBestCard(sortedCardMap);

        if (bestCard == null) {
            return hand;
        }

        return replaceJokersWithBestCard(hand, bestCard);
    }

    private Map<String, Integer> getSortedCardMap(char[] hand) {
        Map<String, Integer> cardMap = getMapOfCards(hand);
        return cardMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private String getBestCard(Map<String, Integer> sortedCardMap) {
        return sortedCardMap.keySet().stream()
                .filter(value -> !value.equals("J"))
                .findFirst()
                .orElse(null);
    }

    private char[] replaceJokersWithBestCard(char[] hand, String bestCard) {
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == 'J') {
                hand[i] = bestCard.charAt(0);
            }
        }
        return hand;
    }
}