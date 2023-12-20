package org.example;

public record Lens(String label, int focalLength) {

    public Lens(String value) {
        this(value.split("-=")[0], Integer.parseInt(value.split("-=")[1]));
    }

    public static Lens of(String value) {
        return new Lens(value.split("-=")[0]  + " " + value.split("-=")[1]);
    }
}
