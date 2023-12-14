package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final String file = "input";

    public static void main(String[] args) throws Exception {
        List<Integer[]> tableNumbers = parseLinesIntoTableNumbers();
        long extrapolationSum = 0;

        for (Integer[] numbers : tableNumbers) {
            extrapolationSum += extrapolateLine(numbers) ;
        }

//        Part1 -- 1772145754
        System.out.println(extrapolationSum);

        long reverseExtrapolationsSum = 0;

        for(Integer[] numbers : tableNumbers) {
            reverseExtrapolationsSum += reverseExtrapolateLine(numbers);
        }

//        Part2 -- 867
        System.out.println(reverseExtrapolationsSum);
    }

//    static int extrapolateLine(Integer[] numbers) {
//        List<Integer> results = new ArrayList<>();
//        extrapolateLine(Arrays.stream(numbers).mapToInt(x -> x).toArray(), results);
//        return results.stream().mapToInt(x -> x).sum();
//    }
//
//    static int[] extrapolateLine(int[] numbers, List<Integer> results) {
//        int[] result = new int[numbers.length - 1];
//
//        for(int i = 0; i < numbers.length - 1; i++) {
//            result[i] = extrapolate(new int[]{numbers[i], numbers[i + 1]});
//        }
//
//        results.add(numbers[numbers.length - 1]);
//
//        if( Arrays.stream(result).allMatch(x -> x == 0) ) {
//            return result;
//        }
//        return extrapolateLine(result, results);
//    }
//
//    static int extrapolate(int[] numbers) {
//        int n = numbers.length;
//        return numbers[n - 1] - numbers[n - 2];
//    }

    static int reverseExtrapolateLine(Integer[] numbers) {
        List<Integer> results = new ArrayList<>();
        int[] currentNumbers = Arrays.stream(numbers).mapToInt(x -> x).toArray();

        while (true) {
            int[] result = new int[currentNumbers.length - 1];

            for (int i = 0; i < currentNumbers.length - 1; i++) {
                result[i] = currentNumbers[i] - currentNumbers[i + 1] ;
            }

            results.add(currentNumbers[0]);

            if (Arrays.stream(result).allMatch(x -> x == 0)) {
                break;
            }

            currentNumbers = result;
        }

        return results.stream().mapToInt(x -> x).sum();
    }

    static int extrapolateLine(Integer[] numbers) {
        List<Integer> results = new ArrayList<>();
        int[] currentNumbers = Arrays.stream(numbers).mapToInt(x -> x).toArray();

        while (true) {
            int[] result = new int[currentNumbers.length - 1];
            for (int i = 0; i < currentNumbers.length - 1; i++) {
                result[i] = currentNumbers[i + 1] - currentNumbers[i];
            }

            results.add(currentNumbers[currentNumbers.length - 1]);

            if (Arrays.stream(result).allMatch(x -> x == 0)) {
                break;
            }
            currentNumbers = result;
        }

        return results.stream().mapToInt(x -> x).sum();
    }

    static List<Integer[]> parseLinesIntoTableNumbers() throws Exception {
        List<String> lines = readFile();
        List<Integer[]> tableNumbers = new ArrayList<>();

        for(String line : lines) {
            String[] numbers = line.split(" ");
            Integer[] numbersInt = new Integer[numbers.length];

            for(int i = 0; i < numbers.length; i++) {
                numbersInt[i] = Integer.parseInt(numbers[i]);
            }

            tableNumbers.add(numbersInt);
        }

        return tableNumbers;
    }


    static List<String> readFile() throws Exception{
        return Files.readAllLines(
                Paths.get("C:\\Users\\viome\\Desktop\\JavaThings\\AoC_2023\\untitled\\src\\main\\Day9\\src\\main\\java\\org\\example\\" + file + ".txt")
        );
    }
}