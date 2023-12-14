package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Game> games = getGames();
        int result = calculateWinPossibilities(games);
//        199515
        System.out.println(result);

        int result2 = calculateWinPossibilities(getSingleGame());
        System.out.println(result2);
    }

    static int calculateWinPossibilities(List<Game> games) {
        return games.stream().map(game -> IntStream.range(0, game.time())
                .filter((i -> (long) (game.time() - i) * i >= game.distance()))
                .count())
                .reduce(1L, (a, b) -> a * b)
                .intValue();
    }

    static List<Game> getGames() throws Exception {
        var file = readFile();
        var timeValues = file.get(0).replaceAll("\\D+", " ").trim().split(" ");
        var distanceValues = file.get(1).replaceAll("\\D+", " ").trim().split(" ");

        List<Game> games = new ArrayList<>();
        for (int i = 0; i < timeValues.length; i++) {
            games.add(new Game(Integer.parseInt(timeValues[i]), Integer.parseInt(distanceValues[i])));
        }

        return games;
    }

    static List<Game> getSingleGame() throws Exception {
        var file = readFile();
        String timeValues = file.get(0).replaceAll("\\D", "").trim();
        String distanceValues = file.get(1).replaceAll("\\D", "").trim();

        return List.of(
                new Game(Integer.parseInt(timeValues), Long.parseLong(distanceValues))
        );
    }

    static List<String> readFile() throws Exception{
        return Files.readAllLines(
                Paths.get("C:\\Users\\viome\\Desktop\\JavaThings\\AoC_2023\\untitled\\src\\main\\Day6\\src\\main\\java\\org\\example\\input.txt")
        );
    }
}