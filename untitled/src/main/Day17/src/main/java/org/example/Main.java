package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    private static final Direction[] DIRECTIONS = {
            Direction.UP,
            Direction.RIGHT,
            Direction.DOWN,
            Direction.LEFT
    };

    private static final int INF = Integer.MAX_VALUE;
    private static final String file = "test";

    public static void main(String[] args) throws Exception {
        var map = getInputMap();

//        Arrays.stream(map).map(Arrays::toString).forEach(System.out::println);
        int leastHeatLoss = calculateHeatLoss(map);
        System.out.println("Least heat loss: " + leastHeatLoss);
    }

    static int calculateHeatLoss(int[][] map) {
        int[][][] distance = new int[map.length][map[0].length][4];
        for (int[][] row : distance) {
            for (int[] cell : row) {
                Arrays.fill(cell, INF);
            }
        }

        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(0, 0, 0, -1, 0));

        while (!queue.isEmpty()) {
            Node node = queue.poll();

            if (node.x == map.length - 1 && node.y == map[0].length - 1) {
                return node.cost;
            }

            for (Direction direction : DIRECTIONS) {
                if (node.direction != -1 && Math.abs(node.direction) == 2) continue;

                for (int i = 1; i <= 3; i++) {
                    int nx = node.x + direction.getX();
                    int ny = node.y - direction.getY();

                    if(nx < 0 || nx >= map.length || ny < 0 || ny >= map[0].length) break;

                    int ncost = node.cost + map[nx][ny];
                    if(ncost < distance[nx][ny][i]) {
                        distance[nx][ny][i] = ncost;
                        queue.add(new Node(nx, ny, ncost, i, node.steps + 1));
                    }
                }
            }
        }

        return -1;
    }

    static int[][] getInputMap() throws Exception {
        return readFile().stream()
                .map(line -> line.chars()
                        .map(Character::getNumericValue)
                        .toArray())
                .toArray(int[][]::new);
    }

    static List<String> readFile() throws Exception {
        return Files.readAllLines(
                Paths.get("untitled/src/main/Day17/src/main/java/org/example/" + file + ".txt")
        );
    }
}