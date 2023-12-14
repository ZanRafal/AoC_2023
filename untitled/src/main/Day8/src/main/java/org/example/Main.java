package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

//    TODO: Learn and read about LCM and GCD
    private static final String file = "input";

    public static void main(String[] args) throws Exception {
        var options = findOptionsMap();
        var moves = getMoves();
        int shortestRoute = getShortestRoute(moves, options);
        long shortestRoutePart2 = calculateRoutesForGhosts(moves, options);

//        Part 1 -- 13301
        System.out.println(shortestRoute);
//        Part 2 -- 7309459565207
        System.out.println(shortestRoutePart2);
    }

    private static int getShortestRoute(char[] moves, Map<String, List<String>> options) {
        List<String> currentOptions = options.get("AAA");
        int sum = 0;

        int i = 0;
        while (true) {
            int dir = getDirection(moves[i % moves.length]);
            var nextOption = currentOptions.get(dir);

            sum++;
            if(nextOption.equals("ZZZ")) {
                return sum;
            }

            currentOptions = options.get(nextOption);
            i++;
        }
    }

    static long calculateRoutesForGhosts(char[] moves, Map<String, List<String>> options) {
        List<String> keys = getStartKeysForPart2(options);
        List<Long> sums = new ArrayList<>();

        for(String key : keys) {
            sums.add(getShortestRouteForGhost(moves, options, key));
        }

        return calculateLCM(sums);
    }

    static long calculateLCM(List<Long> sums) {
        long lcm = sums.get(0);
        for(int i = 1; i < sums.size(); i++) {
            lcm = calculateLCM(lcm, sums.get(i));
        }

        return lcm;
    }

    static long calculateLCM(long a, long b) {
        return ( a * b ) / calculateGCD(a, b);
    }

    static long calculateGCD(long a, long b) {
        if(b == 0) {
            return a;
        }

        return calculateGCD(b, a % b);
    }

    static long getShortestRouteForGhost(char[] moves, Map<String, List<String>> options, String startKey) {
        List<String> currentOptions = options.get(startKey);
        long sum = 0;

        int i = 0;
        while (true) {
            int dir = getDirection(moves[i % moves.length]);
            var nextOption = currentOptions.get(dir);

            sum++;
            if(nextOption.endsWith("Z")) {
                return sum;
            }

            currentOptions = options.get(nextOption);
            i++;
        }
    }

    static List<String> getStartKeysForPart2(Map<String, List<String>> options) {
        return options.keySet().stream()
                .filter(key -> key.endsWith("A"))
                .toList();
    }

    static int getDirection(char move) {
        return move == 'L' ? 0 : 1;
    }

    static char[] getMoves() throws Exception  {
        return readFile().get(0).toCharArray();
    }

    static Map<String, List<String>> findOptionsMap() throws Exception {
        List<String> lines = readFile();
        HashMap<String, List<String>> optionsMap = new HashMap<>();

        for(int i = 2; i < lines.size(); i++) {
            String[] line = lines.get(i).replaceAll("[^a-zA-Z]" , " ").split("\\s+");
            String key = line[0];
            List<String> value = List.of(line[1], line[2]);
            optionsMap.put(key, value);
        }
        return optionsMap;
    }


    static List<String> readFile() throws Exception{
        return Files.readAllLines(
                Paths.get("C:\\Users\\viome\\Desktop\\JavaThings\\AoC_2023\\untitled\\src\\main\\Day8\\src\\main\\java\\org\\example\\" + file + ".txt")
        );
    }
}