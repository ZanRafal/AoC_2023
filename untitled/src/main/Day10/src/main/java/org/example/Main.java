package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final String file = "input";
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

        List<Point> vertices = new ArrayList<>();

        queue.add(new int[]{startingPoint.getX(), startingPoint.getY(), 0});
        visited[startingPoint.getY()][startingPoint.getX()] = true;
        vertices.add(startingPoint);

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
                    if(map[row][column] == 'J'
                            || map[row][column] == 'L'
                            || map[row][column] == '7'
                            || map[row][column] == 'F'
                    ) {
                        vertices.add(new Point(column, row, String.valueOf(map[row][column])));
                    }
                    queue.add(new int[]{column, row, cell[2] + 1});
                    visited[row][column] = true;
                }
            }

        }

//        vertices = sortVertices(vertices);

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

//        double part2 = calculatePart2(vertices, distance * 2);

//        4288 -- to much
//        System.out.println(part2);
        return distance;
    }

    private static List<Point> sortVertices(List<Point> vertices) {
        System.out.println(vertices + " unsorted");
        var startPoint = vertices.get(0);
        vertices.remove(startPoint);
        List<Point> sorted = new ArrayList<>();
        sorted.add(startPoint);

        List<Point> unprocessed = new ArrayList<>();

        var nextPoint = vertices.get(0);
        Set<Point> processed = new HashSet<>();
        processed.add(startPoint);

        for (Point point : vertices) {
            var currentPoint = getNextPoint(nextPoint, point);
            if(currentPoint.equals(nextPoint) && !processed.contains(currentPoint)) {
                sorted.add(currentPoint);
                processed.add(currentPoint);
            } else if (!currentPoint.equals(nextPoint)) {
                sorted.add(currentPoint);
                processed.add(currentPoint);
                nextPoint = currentPoint;
            }
        }

        System.out.println(sorted);
        return sorted;
    }

    private static Point getNextPoint(Point first, Point second) {
        if(first.getX() != second.getX() && first.getY() != second.getY()) {
            return first;
        }

        return switch (first.getValue()) {
            case "L" -> switch (second.getValue()) {
                case "J" -> (first.getY() == second.getY() && first.getX() < second.getX()) ? first : second;
                case "7" -> (first.getY() > second.getY() && first.getX() == second.getX() || first.getX() < second.getX() && first.getY() == second.getY()) ? first : second;
                case "F" -> (first.getY() < second.getY() && first.getX() == second.getX()) ? first : second;
                case "L" -> first;
                default -> throw new IllegalStateException("Unexpected value: " + second.getValue());
            };
            case "J" -> switch (second.getValue()) {
                case "L" -> (first.getX() > second.getX() && first.getY() == second.getY()) ? second : first;
                case "F" -> ((first.getX() == second.getX() && first.getY() > second.getY()) || (first.getX() > second.getX() && first.getY() == second.getY())) ? second : first;
                case "7" -> (first.getX() == second.getX() && first.getY() > second.getY()) ? second : first;
                case "J" -> first;
                default -> throw new IllegalStateException("Unexpected value: " + second.getValue());
            };
            case "7" -> switch (second.getValue()) {
                case "L" -> ((first.getX() == second.getX() && first.getY() < second.getY()) || (first.getX() > second.getX() && first.getY() == second.getY())) ? second : first;
                case "J" -> (first.getX() == second.getX() && first.getY() < second.getY()) ? second : first;
                case "F" -> (first.getX() > second.getX() && first.getY() == second.getY()) ? second : first;
                case "7" -> first;
                default -> throw new IllegalStateException("Unexpected value: " + second.getValue());
            };
            case "F" -> switch (second.getValue()) {
                case "L" -> (first.getY() < second.getY() && first.getX() == second.getX()) ? second : first;
                case "J" -> ((first.getY() < second.getY() && first.getX() == second.getX()) || (first.getX() < second.getX() && first.getY() == second.getY())) ? second : first;
                case "7" -> (first.getX() < second.getX() && first.getY() == second.getY()) ? second : first;
                case "F" -> first;
                default -> throw new IllegalStateException("Unexpected value: " + second.getValue());
            };
            default -> throw new IllegalStateException("Unexpected value: " + first.getValue());
        };
    }

    private static boolean isNextPoint(char c, char c1, Direction direction) {
        return switch (c) {
            case 'L' -> switch (direction) {
                case UP -> c1 == '7' || c1 == 'F';
                case RIGHT -> c1 == 'J' || c1 == '7';
                case DOWN, LEFT -> false;
            };
            case 'J' -> switch (direction) {
                case UP -> c1 == '7' || c1 == 'F';
                case RIGHT, DOWN -> false;
                case LEFT -> c1 == 'L' || c1 == 'F';
            };
            case '7' -> switch (direction) {
                case UP, RIGHT -> false;
                case DOWN -> c1 == 'L' || c1 == 'J';
                case LEFT -> c1 == 'L' || c1 == 'F';
            };
            case 'F' -> switch (direction) {
                case DOWN -> c1 == 'L' || c1 == 'J';
                case UP, LEFT -> false;
                case RIGHT -> c1 == 'J' || c1 == '7';
            };
            default -> false;
        };
    }

    static double calculatePart2(List<Point> visited, int circ) {
//        List<Point> vertices = findVertices(visited);
        return calculatePointsInside(visited) - ((double) circ / 2) + 1;
    }

    static int calculatePointsInside(List<Point> vertices) {
        double area = 0.0;
        for(int i = 0; i < vertices.size() - 1; i++) {
//            var nextIndex = (i + 1) % vertices.size();
            area += (vertices.get(i + 1).getX() + vertices.get(i).getX()) * (vertices.get(i +    1).getY() - vertices.get(i).getY());
//            area += (vertices.get(nextIndex).getY() + vertices.get(i).getX()) - (vertices.get(nextIndex).getX() - vertices.get(i).getY());
        }

        return (int) Math.abs(area / 2.0);
    }

    static Direction getDirection(Direction direction, char pipe) {

        switch (pipe) {
            case 'L' -> {
                switch (direction) {
                    case DOWN -> direction = Direction.RIGHT;
                    case LEFT -> direction = Direction.UP;
                }
            }
            case 'J' -> {
                switch (direction) {
                    case RIGHT -> direction = Direction.UP;
                    case DOWN -> direction = Direction.LEFT;
                }
            }
            case '7' -> {
                switch (direction) {
                    case UP -> direction = Direction.LEFT;
                    case RIGHT -> direction = Direction.DOWN;
                }
            }
            case 'F' -> {
                switch (direction) {
                    case UP -> direction = Direction.RIGHT;
                    case LEFT -> direction = Direction.DOWN;
                }
            }
        }
        return direction;
    }

    static List<Point> findVertices(List<Point> visited) {
        return visited.stream()
                .filter(point -> !point.getValue().equals("|")
                        && !point.getValue().equals("-"))
                .toList();
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
                Paths.get("untitled/src/main/Day10/src/main/java/org/example/" + file + ".txt")
        );
    }
}