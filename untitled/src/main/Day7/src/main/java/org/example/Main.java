package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {



    public static void main(String[] args) throws Exception {
        List<Hand> hands = getAllHands();
//        253933213 -- correct part 1
//        253473930 -- correct part 2
        System.out.println(hands);
        System.out.println(calculateBidSum(hands));


    }

    static long calculateBidSum(List<Hand> hands) {
        long sum = 0;
        int rank = 1;

        for (Hand hand : hands) {
            sum += ((long) hand.getBid() * rank);
            rank++;
        }
        return sum;
    }

    static List<Hand> getAllHands() throws Exception {
        List<String> lines = readFile();

        return lines.stream()
                .map(line -> line.split(" "))
                .map(array -> new Hand(array[0], Integer.parseInt(array[1])))
                .sorted().
                toList();
    }

    static List<String> readFile() throws Exception{
        return Files.readAllLines(
                Paths.get("C:\\Users\\viome\\Desktop\\JavaThings\\AoC_2023\\untitled\\src\\main\\Day7\\src\\main\\java\\org\\example\\input.txt")
        );
    }
}