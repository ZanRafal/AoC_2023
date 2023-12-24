package org.example;

public enum Direction {
    U(0, 1),
    R(1, 0),
    D(0, -1),
    L(-1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Direction getFromName(String name) {
        return Direction.valueOf(name.toUpperCase());
    }
}