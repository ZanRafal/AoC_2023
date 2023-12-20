package org.example;

import io.vavr.Tuple2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String file = "input";
    private static final char EMPTY = '.';
    private static final char GALAXY = '#';

    public static void main(String[] args) throws Exception {
        long distance = expandUniverse(1000000 );

//        Part1 9214785
//        Part2 613686987427
        System.out.println(distance);
    }

//    OnlyPart1
//    static long calculateDistances(List<Point> galaxies) {
//        long[][] dist = new long[galaxies.size()][galaxies.size()];
//
//        for(int i = 0; i < galaxies.size(); i++) {
//            Arrays.fill(dist[i], Integer.MAX_VALUE);
//            dist[i][i] = 0;
//        }
//
//        for (int i = 0; i < galaxies.size(); i++) {
//            for (int j = i + 1; j < galaxies.size(); j++) {
//                int distance = Math.abs(galaxies.get(i).x - galaxies.get(j).x) + Math.abs(galaxies.get(i).y - galaxies.get(j).y);
//                dist[i][j] = distance;
//                dist[j][i] = distance;
//            }
//        }

//        for (int i = 0; i < galaxies.size(); i++) {
//            for (int j = 0; j < galaxies.size(); j++) {
//                for (int k = 0; k < galaxies.size(); k++) {
//                    if(dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE && dist[i][k] + dist[k][j] < dist[i][j]) {
//                        dist[i][j] = dist[i][k] + dist[k][j];
//                    }
//                }
//            }
//        }

//        long sum = 0;
//        for (int i = 0; i < galaxies.size(); i++) {
//            for(int j = i + 1; j < galaxies.size(); j++) {
//                sum += dist[i][j];
//            }
//        }
//
//        return sum;
//    }

//    static List<Point> findGalaxies(char[][] universe) {
//        List<Point> galaxies = new ArrayList<>();
//
//        for(int i = 0; i < universe.length; i++) {
//            for(int j = 0; j < universe[0].length; j++) {
//                if(universe[i][j] == GALAXY) {
//                    galaxies.add(new Point(j, i));
//                }
//            }
//        }
//
//        return galaxies;
//    }

    static  List<Tuple2<Integer, Integer>> findGalaxies(char[][] universe) {
        List<Tuple2<Integer, Integer>> galaxies = new ArrayList<>();
        for (int i = 0; i < universe.length; i++) {
            for (int j = 0; j < universe[0].length; j++) {
                if (universe[i][j] == '#') {
                    galaxies.add(new Tuple2<>(i, j));
                }
            }
        }
        return galaxies;
    }

    static long expandUniverse(int expansionRate) throws Exception {
        var map = getMap();
        var emptyRows = findRowsToExpand(map);
        var emptyColumns = findColumnsToExpand(map);
        var galaxies = findGalaxies(map);

        long sum = 0;
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                var left = galaxies.get(i);
                var right = galaxies.get(j);

                sum += Math.abs(left._1 - right._1) + Math.abs(left._2 - right._2);

                var extraRows = emptyRows.stream()
                        .filter(row -> (left._1< row && right._1 > row) || (left._1> row && right._1 < row))
                        .count() * (expansionRate - 1);

                var extraColumns = emptyColumns.stream()
                        .filter(column -> (left._2 < column && right._2 > column) || (left._2 > column && right._2 < column))
                        .count() * (expansionRate - 1);
                sum += extraRows + extraColumns;
            }
        }

        return sum;
    }

//    Only part 1
//    static char[][] expandUniverse(char[][] oldUniverse) {
//        List<Integer> newColumnsCoordinates = findColumnsToExpand(oldUniverse);
//        List<Integer> newRowsCoordinates = findRowsToExpand(oldUniverse);
//
//        char[][] newUniverse = new char[oldUniverse.length +  newRowsCoordinates.size()][oldUniverse[0].length +  newColumnsCoordinates.size()];
//
//        for (char[] row : newUniverse) {
//            Arrays.fill(row, EMPTY);
//        }
//
//        int newRow = 0;
//        for (int oldRow = 0; oldRow < oldUniverse.length; oldRow++) {
//            int newCol = 0;
//            for (int oldCol = 0; oldCol < oldUniverse[0].length; oldCol++) {
//                newUniverse[newRow][newCol] = oldUniverse[oldRow][oldCol];
//                newCol++;
//                if (newColumnsCoordinates.contains(oldCol)) {
//                    newCol++;
//                }
//            }
//            newRow++;
//            if (newRowsCoordinates.contains(oldRow)) {
//                newRow++;
//            }
//        }
//
//        return newUniverse;
//    }

    private static List<Integer> findRowsToExpand(char[][] oldUniverse) {
        List<Integer> rowIndexes = new ArrayList<>();
        for (int i = 0; i < oldUniverse.length; i++) {
            boolean allDots = true;
            for(int j = 0; j < oldUniverse[i].length; j++) {
                if(oldUniverse[i][j] != '.') {
                    allDots = false;
                    break;
                }
            }
            if(allDots) {
                rowIndexes.add(i);
            }
        }

        return rowIndexes;

    }

    private static List<Integer> findColumnsToExpand(char[][] oldUniverse) {
        List<Integer> columnIndexes = new ArrayList<>();

        for (int i = 0; i < oldUniverse[0].length; i++) {
            boolean allDots = true;
            for (char[] chars : oldUniverse) {
                if (chars[i] != '.') {
                    allDots = false;
                    break;
                }
            }
            if(allDots) {
                columnIndexes.add(i);
            }
        }

        return columnIndexes;
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
                Paths.get("untitled/src/main/Day11/src/main/java/org/example/" + file + ".txt")
        );
    }
}