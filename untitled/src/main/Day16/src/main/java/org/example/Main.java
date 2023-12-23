package org.example;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Main {
    private static final String file = "input";

    public static void main(String[] args) throws Exception {
        var map = getInputMap();
        int energizedTiles = bfs(map);
        System.out.println("Part 1: " + energizedTiles);

        var maxEnergizedTiles = getMaxEnergizedTiles(map);
        System.out.println("Part 2: " + maxEnergizedTiles);
    }

    static int getMaxEnergizedTiles(char[][] map) {
        return getMapBoundaryPoints(map).stream()
                .mapToInt(pointDirection -> bfs(map, pointDirection))
                .max()
                .orElseThrow();
    }

    static int bfs(char[][] map) {
        return bfs(map, new PointDirection(new Point(0, 0), Direction.RIGHT));
    }

    static Set<PointDirection> getMapBoundaryPoints(char[][] map) {
        Set<PointDirection> boundaryPoints = new HashSet<>();
        for (int i = 0; i < map.length; i++) {
            if (map[i][0] == '.') {
                boundaryPoints.add(new PointDirection(new Point(0, i), Direction.RIGHT));
            }
            if (map[i][map[i].length - 1] == '.') {
                boundaryPoints.add(new PointDirection(new Point(map[i].length - 1, i), Direction.LEFT));
            }
        }
        for (int i = 0; i < map[0].length; i++) {
            if (map[0][i] == '.') {
                boundaryPoints.add(new PointDirection(new Point(i, 0), Direction.DOWN));
            }
            if (map[map.length - 1][i] == '.') {
                boundaryPoints.add(new PointDirection(new Point(i, map.length - 1), Direction.UP));
            }
        }
        return boundaryPoints;
    }

    static int bfs(char[][] map, PointDirection startPoint) {
        var energizedTiles = 0;
        Queue<PointDirection> queue = new LinkedList<>();
        queue.add(startPoint);
        boolean[][][] visited = new boolean[map.length][map[0].length][4];

        while (!queue.isEmpty()) {
            PointDirection currentPointDirection = queue.poll();
            Point currentPoint = currentPointDirection.point();
            Direction currentDirection = currentPointDirection.direction();

            if (currentPoint.x < 0 || currentPoint.x >= map[0].length || currentPoint.y < 0 || currentPoint.y >= map.length || visited[currentPoint.y][currentPoint.x][currentDirection.ordinal()]) {
                continue;
            }

            visited[currentPoint.y][currentPoint.x][currentDirection.ordinal()] = true;

            switch (map[currentPoint.y][currentPoint.x]) {
                case '.' ->
                        queue.add(new PointDirection(new Point(currentPoint.x + currentDirection.getX(), currentPoint.y - currentDirection.getY()), currentDirection));
                case '/' -> {
                    Direction newDirection = switch (currentDirection) {
                        case RIGHT -> Direction.UP;
                        case LEFT -> Direction.DOWN;
                        case UP -> Direction.RIGHT;
                        case DOWN -> Direction.LEFT;
                    };
                    queue.add(new PointDirection(new Point(currentPoint.x + newDirection.getX(), currentPoint.y - newDirection.getY()), newDirection));
                }
                case '\\' -> {
                    Direction newDirection = switch (currentDirection) {
                        case LEFT -> Direction.UP;
                        case RIGHT -> Direction.DOWN;
                        case UP -> Direction.LEFT;
                        case DOWN -> Direction.RIGHT;
                    };
                    queue.add(new PointDirection(new Point(currentPoint.x + newDirection.getX(), currentPoint.y - newDirection.getY()), newDirection));
                }
                case '|' -> {
                    if (currentDirection == Direction.UP || currentDirection == Direction.DOWN) {
                        queue.add(new PointDirection(new Point(currentPoint.x + currentDirection.getX(), currentPoint.y - currentDirection.getY()), currentDirection));
                    } else if (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT) {
                        queue.add(new PointDirection(new Point(currentPoint.x + Direction.UP.getX(), currentPoint.y - Direction.UP.getY()), Direction.UP));
                        queue.add(new PointDirection(new Point(currentPoint.x + Direction.DOWN.getX(), currentPoint.y - Direction.DOWN.getY()), Direction.DOWN));
                    }
                }
                case '-' -> {
                    if (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT) {
                        queue.add(new PointDirection(new Point(currentPoint.x + currentDirection.getX(), currentPoint.y - currentDirection.getY()), currentDirection));
                    } else if (currentDirection == Direction.UP || currentDirection == Direction.DOWN) {
                        queue.add(new PointDirection(new Point(currentPoint.x + Direction.LEFT.getX(), currentPoint.y - Direction.LEFT.getY()), Direction.LEFT));
                        queue.add(new PointDirection(new Point(currentPoint.x + Direction.RIGHT.getX(), currentPoint.y - Direction.RIGHT.getY()), Direction.RIGHT));
                    }
                }
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (visited[i][j][0] || visited[i][j][1] || visited[i][j][2] || visited[i][j][3]) {
                    energizedTiles++;
                }
            }
        }

        return energizedTiles;
    }

    static char[][] getInputMap() throws Exception {
            List<String> input = readFile();
            char[][] map = new char[input.size()][input.get(0).length()];

            for(int i = 0; i < input.size(); i++) {
                for(int j = 0; j < input.get(i).length(); j++) {
                    map[i][j] = input.get(i).charAt(j);
                }
            }
            return map;
    }

    static List<String> readFile() throws Exception {
        return Files.readAllLines(
                Paths.get("untitled/src/main/Day16/src/main/java/org/example/" + file + ".txt")
        );
    }
}