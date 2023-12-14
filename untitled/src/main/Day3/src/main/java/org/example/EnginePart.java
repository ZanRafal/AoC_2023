package org.example;

import lombok.Getter;

import java.util.List;

public class EnginePart {
    private int value;
    private final int length;
    private final List<Point> coordinates;

    public EnginePart(List<Point> coordinates) {
        this.coordinates = coordinates;
        String sum = coordinates.stream().map(Point::getValue).reduce("", String::concat);
        this.value = Integer.parseInt(sum);
        this.length = coordinates.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnginePart enginePart = (EnginePart) o;
        return value == enginePart.value && length == enginePart.length && coordinates.equals(enginePart.coordinates);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + value;
        result = 31 * result + length;
        result = 31 * result + coordinates.hashCode();
        return result;
    }

    public int getValue() {
        return value;
    }

    public List<Point> getCoordinates() {
        return coordinates;
    }
}
