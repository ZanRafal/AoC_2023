package org.example;

import io.vavr.Tuple2;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Main {
    static final String file = "input";

    public static void main(String[] args) throws  Exception {
        var map = getInputMap();
        var vertices = findVertices(map);

        var part2Map = getPart2InputMap();
        var part2Vertices = findVertices(part2Map);
    }

    static long shoelace(List<Point> vertices) {
        long area = 0;
        int j = vertices.size() - 1;

        for (int i = 0; i < vertices.size(); i++) {
            area += (long) (vertices.get(j).x + vertices.get(i).x) * (vertices.get(j).y - vertices.get(i).y);
            j = i;
        }

        return area / 2;
    }

    static List<Point> findVertices(Map<String, Tuple2<Direction, Integer>> map) {
        List<Point> result = new ArrayList<>();
        Point currentPoint = new Point(0, 0);
        int circ = 0;

        for(Tuple2<Direction, Integer> entry : map.values()) {
            var newPoint = new Point(currentPoint.x + entry._1.getX() * entry._2, currentPoint.y + entry._1.getY() * entry._2);
            result.add(newPoint);
            currentPoint = newPoint;
            circ += entry._2;
        }

        System.out.println(shoelace(result) + circ /2 + 1);
        return result;
    }

    static Map<String, Tuple2<Direction, Integer>> getInputMap() throws Exception {
        List<String> input = readFile();
        Map<String, Tuple2<Direction, Integer>> map = new LinkedHashMap<>();

        for(String line : input) {
            String[] split = line.split(" ");
            Direction direction = Direction.getFromName(split[0]);
            int tiles = Integer.parseInt(split[1]);
            String color = split[2].replaceAll("\\(|\\)|#", "");
            map.put(color, new Tuple2<>(direction, tiles));
        }
        return map;
    }

    static Map<String, Tuple2<Direction, Integer>> getPart2InputMap() throws Exception {
        List<String> input = readFile();
        Map<String, Tuple2<Direction, Integer>> result = new LinkedHashMap<>();

        for(String line : input) {
            String hash = line.split(" ")[2].replaceAll("\\(|\\)|#", ""); ;
            Direction direction = getDirection(hash.charAt(5));
            int tiles = Integer.parseInt(hash.substring(0, 5), 16);
            result.put(hash, new Tuple2<>(direction, tiles));
        }

        return result;
    }

    static Direction getDirection(char directionCode) {
        return switch (directionCode) {
            case '0' -> Direction.R;
            case '1' -> Direction.D;
            case '2' -> Direction.L;
            case '3' -> Direction.U;
            default -> throw new IllegalArgumentException("Invalid direction code: " + directionCode);
        };}

    static List<String> readFile() throws Exception {
        return Files.readAllLines(
                Paths.get("untitled/src/main/Day18/src/main/java/org/example/" + file + ".txt")
        );
    }
}