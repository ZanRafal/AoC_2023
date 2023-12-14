package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final String file = "input";

    private static final Map<Character, List<int[]>> PIPE_CONNECTIONS = new HashMap<>() {{
        put('|', Arrays.asList(new int[]{0, 1}, new int[]{0, -1})); // vertical pipe connects north and south
        put('-', Arrays.asList(new int[]{1, 0}, new int[]{-1, 0})); // horizontal pipe connects east and west
        put('L', Arrays.asList(new int[]{0, -1}, new int[]{-1, 0})); // 90-degree bend connects north and east
        put('J', Arrays.asList(new int[]{0, -1}, new int[]{1, 0})); // 90-degree bend connects north and west
        put('7', Arrays.asList(new int[]{0, 1}, new int[]{1, 0})); // 90-degree bend connects south and west
        put('F', Arrays.asList(new int[]{0, 1}, new int[]{-1, 0})); // 90-degree bend connects south and east
        put('S', Arrays.asList(new int[]{0, 1}, new int[]{0, -1}, new int[]{0, 1}, new int[]{0, -1})); // start point
    }};

    private static final Map<Character, List<int[]>> OUTGOING_CONNECTIONS = new HashMap<>() {{
        put('|', Arrays.asList(new int[]{0, 1}, new int[]{0, -1})); // vertical pipe connects north and south
        put('-', Arrays.asList(new int[]{1, 0}, new int[]{-1, 0})); // horizontal pipe connects east and west
        put('L', Arrays.asList(new int[]{0, 1}, new int[]{1, 0})); // 90-degree bend connects north and east
        put('J', Arrays.asList(new int[]{0, 1}, new int[]{-1, 0})); // 90-degree bend connects north and west
        put('7', Arrays.asList(new int[]{0, -1}, new int[]{-1, 0})); // 90-degree bend connects south and west
        put('F', Arrays.asList(new int[]{0, -1}, new int[]{1, 0})); // 90-degree bend connects south and east
        put('S', Arrays.asList(new int[]{0, 1}, new int[]{0, -1}, new int[]{0, 1}, new int[]{0, -1})); // start point
    }};

//    private static final int[][] DIRECTIONS = {
//            {0, 1}, // up
//            {1, 0}, // right
//            {0, -1}, // down
//            {-1, 0},// left
//    };

    private static final Direction[] DIRECTIONS = {
            Direction.UP,
            Direction.RIGHT,
            Direction.DOWN,
            Direction.LEFT
    };

    public static void main(String[] args) throws Exception {
        Point startPoint = findStartPoint();
        char[][] map = getMap();
        int distance = findFurthestDistance(startPoint, map);
//        Part1 --- 6725
        System.out.println(distance);
    }

    static int findFurthestDistance(Point startingPoint, char[][] map) {
        int distance = 0;
        boolean[][] visited = new boolean[map.length][map[0].length];
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{startingPoint.getX(), startingPoint.getY(), 0});
        visited[startingPoint.getY()][startingPoint.getX()] = true;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            distance = cell[2];
            var point = new Point(cell[0], cell[1], String.valueOf(map[cell[1]][cell[0]]));

            for (Direction direction : DIRECTIONS) {
                int row = cell[1] - direction.getY();
                int column = cell[0] + direction.getX();

                if(row >= 0 && row < map.length
                        && column >= 0
                        && column < map[row].length
                        && !visited[row][column]
                        && canConnect(point.getValue().charAt(0), map[row][column], direction)
                ) {
                    queue.add(new int[]{column, row, cell[2] + 1});
                    visited[row][column] = true;
                }
            }

        }

        for(int i = 0; i < visited.length; i++) {
            for(int j = 0; j < visited[i].length; j++) {
                if(visited[i][j]) {
                    System.out.print("\u001B[32m" +map[i][j] + "\u001B[0m");
                } else {
                    System.out.print(map[i][j]);
                }
            }
            System.out.println();
        }

        return distance;
    }

    public static boolean canConnect(char currentPipe, char nextPipe, Direction direction) {
        if (currentPipe == '.' || nextPipe == '.') {
            return false;
        }
        return switch (currentPipe) {
            case 'S' -> switch (direction) {
                case UP -> nextPipe == '|' || nextPipe == '7' || nextPipe == 'F';
                case DOWN -> nextPipe == '|' || nextPipe == 'L' || nextPipe == 'J';
                case LEFT -> nextPipe == '-' || nextPipe == 'L' || nextPipe == 'F';
                case RIGHT -> nextPipe == '-' || nextPipe == 'J' || nextPipe == '7';
            };
            case '|' -> switch (direction) {
                case UP ->
                        nextPipe == '|' || nextPipe == '7' || nextPipe == 'F';
                case DOWN -> nextPipe == '|' || nextPipe == 'L' || nextPipe == 'J';
                default -> false;
            };
            case '-' -> switch (direction) {
                case LEFT -> nextPipe == '-' || nextPipe == 'L'  || nextPipe == 'F';
                case RIGHT -> nextPipe == '-' || nextPipe == 'J' || nextPipe == '7';
                default -> false;
            };
            case 'L' -> switch (direction) {
                case UP -> nextPipe == '|' || nextPipe == 'F' || nextPipe == '7';
                case RIGHT -> nextPipe == '-' || nextPipe == 'J' || nextPipe == '7';
                default -> false;
            };
            case 'J' -> switch (direction) {
                case UP -> nextPipe == '|' || nextPipe == '7' || nextPipe == 'F';
                case LEFT -> nextPipe == '-' || nextPipe == 'F' || nextPipe == 'L';
                default -> false;
            };
            case '7' -> switch (direction) {
                case DOWN -> nextPipe == '|' || nextPipe == 'L' || nextPipe == 'J';
                case LEFT -> nextPipe == '-' || nextPipe == 'L' || nextPipe == 'F';
                default -> false;
            };
            case 'F' -> switch (direction) {
                case DOWN -> nextPipe == '|' || nextPipe == 'J' || nextPipe == 'L';
                case RIGHT -> nextPipe == '-' || nextPipe == 'J' || nextPipe == '7';
                default -> false;
            };
            default -> false;
        };
    }

    static Point findStartPoint() throws Exception {
        var map = getMap();
        Point startPoint = new Point();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j <map[i].length; j++) {
                if (map[i][j] == 'S') {
                    startPoint.setX(j);
                    startPoint.setY(i);
                    startPoint.setValue(String.valueOf(map[i][j]));
                }
            }
        }
        return startPoint;
    }

    static char[][] getMap() throws Exception {
        List<String> input = readFile();

        char[][] map = new char[input.size()][input.get(0).length()];
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).length(); j++) {
                map[i][j] = input.get(i).charAt(j);
            }
        }
        return map;
    }

    static List<String> readFile() throws Exception{
        return Files.readAllLines(
                Paths.get("C:\\Users\\viome\\Desktop\\JavaThings\\AoC_2023\\untitled\\src\\main\\Day10\\src\\main\\java\\org\\example\\" + file + ".txt")
        );
    }
}