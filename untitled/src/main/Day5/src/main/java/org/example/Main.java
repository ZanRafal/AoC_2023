package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import io.vavr.Tuple2;

public class Main {

    private static final String SEED_TO_SOIL = "seed-to-soil map:";
    private static final String SOIL_TO_FERTILIZER = "soil-to-fertilizer map:";
    private static final String FERTILIZER_TO_WATER = "fertilizer-to-water map:";
    private static final String WATER_TO_LIGHT = "water-to-light map:";
    private static final String LIGHT_TO_TEMPERATURE = "light-to-temperature map:";
    private static final String TEMPERATURE_TO_HUMIDITY = "temperature-to-humidity map:";
    private static final String HUMIDITY_TO_LOCATION = "humidity-to-location map:";

    public static void main(String[] args) throws Exception {
//        List<Long> seeds = getSeeds(readFile());
        var seedsForPart2 = getSeedsForPart2();
        Stream<Long> seeds = calculateSeedsForPart2(seedsForPart2);
        List<String> file = readFile();

        Map<String, List<SourceDestinationMap>> sections = new HashMap<>();
        sections.put(SEED_TO_SOIL, getSection(file, SEED_TO_SOIL, SOIL_TO_FERTILIZER));
        sections.put(SOIL_TO_FERTILIZER, getSection(file, SOIL_TO_FERTILIZER, FERTILIZER_TO_WATER));
        sections.put(FERTILIZER_TO_WATER, getSection(file, FERTILIZER_TO_WATER, WATER_TO_LIGHT));
        sections.put(WATER_TO_LIGHT, getSection(file, WATER_TO_LIGHT, LIGHT_TO_TEMPERATURE));
        sections.put(LIGHT_TO_TEMPERATURE, getSection(file, LIGHT_TO_TEMPERATURE, TEMPERATURE_TO_HUMIDITY));
        sections.put(TEMPERATURE_TO_HUMIDITY, getSection(file, TEMPERATURE_TO_HUMIDITY, HUMIDITY_TO_LOCATION));
        sections.put(HUMIDITY_TO_LOCATION, getSection(file, HUMIDITY_TO_LOCATION, "\\u001a"));

        ConcurrentLinkedQueue<Long> result = new ConcurrentLinkedQueue<>();

        seeds.parallel().forEach(seed -> {
            long soil = findDestination(sections.get(SEED_TO_SOIL), seed);
            long fertilizer = findDestination(sections.get(SOIL_TO_FERTILIZER), soil);
            long water = findDestination(sections.get(FERTILIZER_TO_WATER), fertilizer);
            long light = findDestination(sections.get(WATER_TO_LIGHT), water);
            long temperature = findDestination(sections.get(LIGHT_TO_TEMPERATURE), light);
            long humidity = findDestination(sections.get(TEMPERATURE_TO_HUMIDITY), temperature);
            long location = findDestination(sections.get(HUMIDITY_TO_LOCATION), humidity);

            result.add(location);
        });

        result.parallelStream().min(Long::compareTo).ifPresent(System.out::println);

    }

//    private static List<Long> calculateSeedsForPart2(List<Tuple2<Long, Long>> seedsForPart2) {
//        List<Long>result = new ArrayList<>();
//
//        for(var seed : seedsForPart2) {
//            for(int i = 0; i < seed._2; i++) {
//                result.add(seed._1 + i);
//            }
//        }
//
//        return result;
//    }

    private static Stream<Long> calculateSeedsForPart2(List<Tuple2<Long, Long>> seedsForPart2) {
        return seedsForPart2.parallelStream()
                .flatMapToLong(seed -> LongStream.range(seed._1, seed._1 + seed._2))
                .boxed();
    }

    private static long findDestination(List<SourceDestinationMap> map, long seed) {
        return map.parallelStream()
                .filter(sourceDestinationMap -> sourceDestinationMap.getSource() <= seed && sourceDestinationMap.getSource() + sourceDestinationMap.getRange() > seed)
                .findFirst()
                .map(sourceDestinationMap -> seed - sourceDestinationMap.getSource() + sourceDestinationMap.getDestination())
                .orElse(seed);
    }



    private static List<SourceDestinationMap> getSection(List<String> lines, String sectionBeginning, String sectionEnd) {
        int sectionBeginningIndex = lines.indexOf(sectionBeginning);
        int sectionEndIndex = lines.indexOf(sectionEnd);

        List<SourceDestinationMap> result = Collections.synchronizedList(new ArrayList<>());

        int end = sectionEndIndex == -1 ? lines.size() : sectionEndIndex;

        IntStream.range(sectionBeginningIndex, end).parallel().forEach(i -> {
            String line = lines.get(i);
            if (!line.contains("map:") && !line.isBlank()) {
                String[] split = line.split(" ");
                long source = Long.parseLong(split[1].trim());
                long destination = Long.parseLong(split[0].trim());
                long range = Long.parseLong(split[2].trim());
                result.add(new SourceDestinationMap(source, destination, range));
            }
        });

        return result;
    }

    private static List<Long> getSeeds(List<String> lines) {
        return Arrays.stream(lines.get(0)
                        .replace("seeds:", "")
                        .trim().split(" "))
                .map(Long::parseLong)
                .toList();
    }

    private static List<Tuple2<Long, Long>> getSeedsForPart2() throws Exception {
        var seeds = getSeeds(readFile());

        var tuple = new ArrayList<Tuple2<Long, Long>>();

        for (int i = 0; i < seeds.size(); i = i + 2) {
            tuple.add(new Tuple2<>(seeds.get(i), seeds.get(i + 1)));
        }


        return tuple;
    }

    private static List<String> readFile() throws Exception{
        return Files.readAllLines(
                Paths.get("C:\\Users\\viome\\Desktop\\JavaThings\\AoC_2023\\untitled\\src\\main\\Day5\\src\\main\\java\\org\\example\\test.txt")
        );
    }
}