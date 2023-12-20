package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final String file = "input";

    public static void main(String[] args) throws Exception {
        List<String> input = getInputMap();
        var part1 = calculateTotalSumOfHashes(input);
        System.out.println(part1);
        var part2 = calculateFocusingPower(input);
        System.out.println(part2);
    }

    static int calculateFocusingPower(List<String> input) {
        HashMap<Integer, List<Lens>> boxes = new HashMap<>();

        for(int i = 0; i < 256; i++) {
            boxes.put(i, new ArrayList<>());
        }

        for(String value : input) {
            String label = value.replaceAll("[^a-zA-Z]", "");
            String digits = value.replaceAll("[^0-9]", "");
            int power = digits.isEmpty() ? 0 : Integer.parseInt(digits);
            int boxNumber = calculateHash(label);

            if(value.contains("-")) {
                var num = boxes.get(boxNumber);
                boxes.get(boxNumber).removeIf(lens -> lens.label().equals(label));
            } else if (value.contains("=")) {
                Lens newLens = new Lens(label, power);
                List<Lens> box = boxes.get(boxNumber);
                int index = -1;
                for (int i = 0; i < box.size(); i++) {
                    if (box.get(i).label().equals(label)) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    box.remove(index);
                    box.add(index, newLens);
                } else {
                    box.add(newLens);
                }
            }
        }

        System.out.println(boxes);

        int totalFocusingPower = 0;
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = 0; j < boxes.get(i).size(); j++) {
                totalFocusingPower += (i + 1) * (j + 1) * boxes.get(i).get(j).focalLength();
            }
        }

        return totalFocusingPower;
    }

    static int calculateTotalSumOfHashes(List<String> input) {
        return input.stream().mapToInt(Main::calculateHash).sum();
    }

    private static int calculateHash(String value) {
        return value.chars()
                .reduce(0, (currentValue, character) -> (currentValue + character) * 17 % 256);
    }

    static List<String> getInputMap() throws Exception {
        return Arrays.stream(readFile().split(",")).toList();
    }

    static String readFile() throws Exception{
        return Files.readString(
                Paths.get("untitled/src/main/Day15/src/main/java/org/example/" + file + ".txt")
        );
    }
}