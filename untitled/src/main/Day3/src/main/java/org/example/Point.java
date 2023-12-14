package org.example;

import java.util.Objects;

public class Point {
    private int x;
    private int y;
    private String value;

    public Point(int x, int y, String value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String  getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Point point)) {
            return false;
        }
        return getX() == point.getX() &&
                getY() == point.getY() &&
                Objects.equals(getValue(), point.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getValue());
    }
}
