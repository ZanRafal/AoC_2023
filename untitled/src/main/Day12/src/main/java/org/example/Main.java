package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final String file = "test1";

    public static void main(String[] args) throws Exception {
        Map<String, Integer[]> input = parseInput();
//        System.out.println(input);
        calculatePossibleOptions(input);

    }

    static int calculatePossibleOptions(Map<String, Integer[]> input) {
        int count = 0;

        for (Map.Entry<String, Integer[]> entry : input.entrySet()) {
            var groups = entry.getKey().replaceAll("\\.", " ").trim().split("\\s+");
            int results = calculateOptions(groups, entry.getValue());
            count += results;
        }
        return count;
    }

    private static int calculateOptions(String[] groups, Integer[] value) {

        return 0;
    }

    static Map<String, Integer[]> parseInput() throws Exception {
        List<String> lines = readFile();
        Map<String, Integer[]> parsedLines = new HashMap<>();

        for (String line : lines) {
            var key = line.split(" ")[0];
            var nums = Arrays.stream(line.split(" ")[1].split(",")).map(Integer::parseInt).toArray(Integer[]::new);
            parsedLines.put(key, nums);
        }

        return parsedLines;
    }

    static List<String> readFile() throws Exception{
        return Files.readAllLines(
                Paths.get("C:\\Users\\viome\\Desktop\\JavaThings\\AoC_2023\\untitled\\src\\main\\Day12\\src\\main\\java\\org\\example\\" + file + ".txt")
        );
    }

}