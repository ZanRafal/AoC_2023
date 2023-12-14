package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final int MAX_RED = 12;
    private static final int MAX_GREEN = 13;
    private static final int MAX_BLUE = 14;

    public static void main(String[] args) throws Exception {
        int idSum = 0;
        int powerSum = 0;

        List<String> gameLines = readFile();
        Map<Integer, Game> games = getGames(gameLines);

        for(Map.Entry<Integer, Game> game: games.entrySet()) {
            if(gamePossible(game.getValue())) {
                idSum += game.getKey();
            }
        }

        System.out.println("Id sum : " + idSum);

        for(Map.Entry<Integer, Game> game: games.entrySet()) {
            Game thisGame = game.getValue();
            int powerThisGame = thisGame.getBlue() * thisGame.getGreen() * thisGame.getRed();

            powerSum += powerThisGame;
        }
        System.out.println("Power of cubes  " + powerSum);
    }

    private   static boolean gamePossible(Game gameToValidate) {
        if(gameToValidate.getBlue() > MAX_BLUE) {
            return false;
        } else if (gameToValidate.getRed() > MAX_RED) {
            return false;
        }  else return gameToValidate.getGreen() <= MAX_GREEN;
    }

    private static Map<Integer, Game> getGames(List<String> games) {
        Map<Integer, Game> gamesMap = new HashMap<>();

        for (int id = 0; id < games.size(); id++) {
            Game newGame = createGame(games.get(id));
            int newId = id + 1;
            gamesMap.put(newId, newGame);
        }

        return gamesMap;
    }

    private static Game createGame(String rounds) {
       Game game = new Game();

        String[] gameRounds = rounds.split(":");
        gameRounds = gameRounds[1].split(";");
       for(String round: gameRounds) {

        for(String ballNumber : round.split(",")) {
            if(ballNumber.contains("green")) {
                String result = ballNumber.replaceAll("[^0-9]", "");
                game.incrementGreen(Integer.parseInt(result));
            }
            if (ballNumber.contains("red")) {
                String result = ballNumber.replaceAll("[^0-9]", "");
                game.incrementRed(Integer.parseInt(result));
            }
            if(ballNumber.contains("blue")) {
                String result = ballNumber.replaceAll("[^0-9]", "");
                game.incrementBlue(Integer.parseInt(result));
            }
        }

       }
       return game;
    }

    private static List<String> readFile() throws Exception{
        return Files.readAllLines(
                Paths.get("C:\\Users\\viome\\Desktop\\JavaThings\\AoC_2023\\untitled\\src\\main\\Day2\\src\\main\\java\\org\\example\\input.txt")
        );
    }
}