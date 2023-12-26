package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    static String file = "test";
    public static void main(String[] args) throws Exception {
        System.out.println("Part 1: " + solvePart1());
//        System.out.println("Part 2: " + solvePart2());
    }

//    static long solvePart2() throws Exception {
//        long result = 0;
////        var parts = generateCombinations();
//        var workflows = getWorkflows();
//
//        for (List<Part> partCharacteristics : parts) {
//            Workflow currentWorkflow = workflows.stream().filter(workflow -> workflow.id().equals("in")).findFirst().get();
//            while (!currentWorkflow.id().equals("A")) {
//                String newItem = validatePart(partCharacteristics, currentWorkflow);
//
//                if (newItem.equals("R")) {
//                    break;
//                }
//
//                if (newItem.equals("A")) {
//                    result ++;
////                    result += partCharacteristics.stream().mapToInt(Part::number).sum();
//                    break;
//                }
//                currentWorkflow = workflows.stream().filter(workflow -> workflow.id().equals(newItem)).findFirst().get();
//            }
//        }
//        return result;
//    }

    static int solvePart1() throws Exception {
        int result = 0;
        var workflows = getWorkflows();
        var parts = getParts();

        for (List<Part> partCharacteristics : parts) {
            Workflow currentItem = workflows.stream().filter(workflow -> workflow.id().equals("in")).findFirst().get();
            while (!currentItem.id().equals("A")) {
                String newItem = validatePart(partCharacteristics, currentItem);

                if (newItem.equals("R")) {
                    break;
                }

                if (newItem.equals("A")) {
                    result += partCharacteristics.stream().mapToInt(Part::number).sum();
                    break;
                }

                currentItem = workflows.stream().filter(workflow -> workflow.id().equals(newItem)).findFirst().get();
            }
        }
        return result;
    }

    static String validatePart(List<Part> partCharacteristics, Workflow workflow) {
        for(Rule rule : workflow.rules()) {
            Optional<Part> optionalPart = partCharacteristics.stream().filter(part -> isValid(part, rule)).findFirst();

            if(optionalPart.isPresent()) {
                return rule.getDestination();
            }
        }
        return workflow.destination();
    }

    static boolean isValid(Part part, Rule rule) {
        if(!part.id().equals(rule.getCompared())) {
            return false;
        }

        switch (rule.getCondition()) {
            case '<' -> {
                return part.number() < rule.getValue();
            }
            case '>' -> {
                return part.number() > rule.getValue();
            }
        }

        return false;
    }

    static List<List<Part>> getParts() throws Exception {
        return getInputPart(2).stream()
                .map(line -> line.replace("{", "").replace("}", "").split(","))
                .map(parts -> Arrays.stream(parts).map(Part::new).toList())
                .toList();
    }

    static List<Workflow> getWorkflows() throws Exception {
        return getInputPart(1).stream()
                .map(Workflow::ofWorkflow)
                .toList();
    }

    static List<String> getInputPart(int part) throws Exception {
        var result = new ArrayList<String>();
        var lines = readFile();

        int emptyLineIndex = -1;

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().isEmpty()) {
                emptyLineIndex = i;
                break;
            }
        }

        if (part == 1) {
            for (int i = 0; i < emptyLineIndex; i++) {
                result.add(lines.get(i));
            }
        } else if (part == 2) {
            for (int i = emptyLineIndex + 1; i < lines.size(); i++) {
                result.add(lines.get(i));
            }
        }

        return result;
    }

    static List<String> readFile() throws Exception {
        return Files.readAllLines(
                Paths.get("untitled/src/main/Day19/src/main/java/org/example/" + file + ".txt")
        );
    }
}