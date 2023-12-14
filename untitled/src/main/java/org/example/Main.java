package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        List<String> lines = main.readFile();

        int sum = 0;
        for (String line : lines) {
            sum += main.getNumberFromLine(line);
        }
        System.out.println("Sum " +sum);
    }

    private List<String> readFile() {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get("C:\\Users\\viome\\Desktop\\JavaThings\\AoC_2023\\untitled\\src\\main\\java\\org\\example\\input.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private int getNumberFromLine(String line) {
       line = replaceLetteredNumbers(line);
       line = line.replaceAll("[^0-9]", "");

        char first = line.charAt(0);
        char last = line.charAt(line.length() - 1);

        return Integer.parseInt(String.valueOf(first) + String.valueOf(last));
    }

    String replaceLetteredNumbers(String line) {
        String[] searchList = new String[]{"oneight", "twone", "eightwo", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        String[] replacementList = new String[]{"18", "21", "82", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        for(int i = 0; i < searchList.length; i++) {
            line = line.replaceAll(searchList[i], replacementList[i]);
        }
        return line;
    }
}