package org.example;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void sameCoordinatesAndValueShouldBeTheSamePoint() {
        Point point1 = new Point(1, 1, "1");
        Set<Point> set = new HashSet<>();
        set.add(point1);
        Point point2 = new Point(1, 1, "1");

        assertTrue(set.contains(point2));
    }


    @Test
    void shouldCorrectlyCalculateValueOfPart() {
        var points = List.of(
                new Point(1, 1, "1"),
                new Point(1, 2, "2"),
                new Point(1, 3, "3")
        );

        EnginePart part = new EnginePart(points);

        assertEquals(123, part.getValue());

    }
}