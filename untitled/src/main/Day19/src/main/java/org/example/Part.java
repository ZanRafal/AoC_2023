package org.example;

public record Part(String id, short number) {

    public Part(String value) {
        this(value.split("=")[0], Short.parseShort(value.split("=")[1]));
    }
}
