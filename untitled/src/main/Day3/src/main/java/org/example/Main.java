package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final int[][] DIRECTIONS = {
            {0, 1}, // up
            {1, 0}, // right
            {0, -1}, // down
            {-1, 0},// left
            {1, 1}, // up right
            {1, -1}, // down right
            {-1, 1}, // up left
            {-1, -1} // down  left
    };

    private static final int[][] ROW_LEFT_DIRECTIONS = {
            {-1, 0}, // left - 1
            {-2, 0}, // left - 2
    };

    private static final int[][] ROW_RIGHT_DIRECTIONS = {
            {1, 0}, // right
            {2, 0}, // right + 1
    };

    public static void main(String[] args) throws Exception {
        int sumOfEngineParts;
        long sumOfGearRatios;

        char[][] map = getMap(readFile());
        Set<Point> setOfAllSpecialCharacters = findSetOfAllSpecialCharacters(map);
        Set<Point> setOfAllNumbersInCharacterVicinity = findSetOfAllNumbersInCharacterVicinity(map, setOfAllSpecialCharacters);
        sumOfEngineParts = calculateSumOfParts(map, setOfAllNumbersInCharacterVicinity);

        Set<Point> setOfAllGears = findSetOfAllGears(map);

        sumOfGearRatios = calculateSumGearRatios(map, setOfAllGears);

        System.out.println(sumOfEngineParts);
        System.out.println(sumOfGearRatios);
    }

    private static long calculateSumGearRatios(char[][] map, Set<Point> setOfAllGears) {
        Set<EnginePart> parts = new HashSet<>();
        Map<Point, List<Point>> gearNumberMap = new HashMap<>();


        for(Point gear : setOfAllGears ) {
            gearNumberMap.put(gear, new ArrayList<>());
            for(int[] direction : DIRECTIONS) {
                int x = gear.getX()+ direction[0];
                int y = gear.getY() + direction[1];

                if(Character.isDigit(map[y][x])) {
                    Point digitPoint = new Point(x, y, String.valueOf(map[y][x]));
                    gearNumberMap.get(gear).add(digitPoint);
                }
            }
        }

        Map<Point, List<Point>> result = gearNumberMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() >= 2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<Point, List<EnginePart>> gearPartMap = new HashMap<>();

        for (Map.Entry<Point, List<Point>> gear : result.entrySet()) {
            gearPartMap.put(gear.getKey(), new ArrayList<>());

            for (Point point : gear.getValue()) {

                List<Point> left = checkLeftSideOfPoint(map, point);
                if (containsCoordinatesOfOtherPart(parts, left)) {
                    continue;
                }
                List<Point> middle = List.of(new Point(point.getX(), point.getY(), String.valueOf(map[point.getY()][point.getX()])));
                if (containsCoordinatesOfOtherPart(parts, middle)) {
                    continue;
                }
                List<Point> right = checkRightSideOfPoint(map, point);

                if (containsCoordinatesOfOtherPart(parts, right)) {
                    continue;
                }

                var combinedPart = new ArrayList<Point>();
                combinedPart.addAll(left);
                combinedPart.addAll(middle);
                combinedPart.addAll(right);
                var newPart = new EnginePart(combinedPart);
                gearPartMap.get(gear.getKey()).add(newPart);
                parts.add(newPart);
            }
        }

        long totalGearRatioSum = 0;

        for (Map.Entry<Point, List<EnginePart>> gear : gearPartMap.entrySet()) {
            List<Integer> gearRatioCompounds = new ArrayList<>();

            for (EnginePart part : gear.getValue()) {
                gearRatioCompounds.add(part.getValue());
            }

            if (gearRatioCompounds.size() >= 2) {
                totalGearRatioSum += (long) gearRatioCompounds.get(0) * gearRatioCompounds.get(1);
            }
        }
//        78236071
        return totalGearRatioSum;
    }

    private static int calculateSumOfParts(char[][] map, Set<Point> setOfAllNumbersInCharacterVicinity) {
        Set<EnginePart> parts = new HashSet<>();

        for (Point point : setOfAllNumbersInCharacterVicinity) {

            List<Point> combinedPart = Stream.of(
                    checkLeftSideOfPoint(map, point),
                    List.of(new Point(point.getX(), point.getY(), String.valueOf(map[point.getY()][point.getX()]))),
                    checkRightSideOfPoint(map, point)
            ).flatMap(Collection::stream).toList();

            if(combinedPart.stream().noneMatch(p -> containsCoordinatesOfOtherPart(parts, List.of(p)))) {
                parts.add(new EnginePart(combinedPart));
            }
        }

//        533775
        return parts.stream().mapToInt(EnginePart::getValue).sum();
    }

    private static List<Point> checkPointsInDirections(char[][] map, Point point, int[][] directions) {
        return Arrays.stream(directions)
                .map(direction -> new Point(point.getX() + direction[0], point.getY() + direction[1], String.valueOf(map[point.getY()][point.getX()])))
                .filter(newPoint -> newPoint.getX() >= 0 && newPoint.getY() >= 0 && newPoint.getX() < map[0].length && newPoint.getY() < map.length)
                .filter(newPoint -> Character.isDigit(map[newPoint.getY()][newPoint.getX()]))
                .collect(Collectors.toList());
    }

    private static boolean containsCoordinatesOfOtherPart(Set<EnginePart> parts, List<Point> left) {
        for (EnginePart part : parts) {
            for (Point point : left) {
                if(part.getCoordinates().contains(point)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<Point> checkLeftSideOfPoint(char[][] map, Point point) {
        List<Point> points = new ArrayList<>();
        for (int[] direction : ROW_LEFT_DIRECTIONS) {
            int x = point.getX() + direction[0];
            int y = point.getY() + direction[1];

            if(x < 0 || y  < 0) {
                continue;
            }

            if (!Character.isDigit(map[y][x])) {
                break;
            }

            points.add(new Point(x, y, String.valueOf(map[y][x])));
        }

        Collections.reverse(points);
        return points;
    }

    private static List<Point> checkRightSideOfPoint(char[][] map, Point point) {
        List<Point> points = new ArrayList<>();
        for (int[] direction : ROW_RIGHT_DIRECTIONS) {
            int x = point.getX() + direction[0];
            int y = point.getY() + direction[1];

            if(x > map[0].length || y > map.length - 1 ) {
                break;
            }

            if (!Character.isDigit(map[y][x])) {
                break;
            }
            points.add(new Point(x, y, String.valueOf(map[y][x])));
        }
        return points;
    }

    private static Set<Point> findSetOfAllNumbersInCharacterVicinity(char[][] map, Set<Point> setOfAllSpecialCharacters) {
        return setOfAllSpecialCharacters.stream()
                .flatMap((point -> checkPointsInDirections(map, point, DIRECTIONS).stream()))
                .collect(Collectors.toSet());
    }

    private static Set<Point> findSetOfAllSpecialCharacters(char[][] map) {
        Set<Point> set = new HashSet<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length ; j++) {
                if (isSpecialCharacter(map[j][i])) {
                    set.add(new Point(i, j, String.valueOf(map[j][i])));
                }
            }
        }
        return set;
    }

    private static Set<Point> findSetOfAllGears(char[][] map) {
        Set<Point> set = new HashSet<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length ; j++) {
                if (isGear(map[j][i])) {
                    set.add(new Point(i, j, String.valueOf(map[j][i])));
                }
            }
        }
        return set;
    }

    private static List<String> readFile() throws Exception{
        return Files.readAllLines(
                Paths.get("C:\\Users\\viome\\Desktop\\JavaThings\\AoC_2023\\untitled\\src\\main\\Day3\\src\\main\\java\\org\\example\\input.txt")
        );
    }

    private static char[][] getMap(List<String> input) {
        char[][] map = new char[input.size()][input.get(0).length()];
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).length(); j++) {
                map[i][j] = input.get(i).charAt(j);
            }
        }
        return map;
    }

    private static boolean isSpecialCharacter(char c) {
        return c != '.' && !Character.isDigit(c);
    }

    private static boolean isGear(char c) {
        return c == '*';
    }
}