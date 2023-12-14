package org.example;

public class Game {
    private int red;
    private int blue;
    private int green;

    public int getRed() {
        return red;
    }

    public void incrementRed(int red) {
        if(red > this.red) {
            this.red = red;
        }
    }

    public int getBlue() {
        return blue;
    }

    public void incrementBlue(int blue) {
        if(blue > this.blue) {
            this.blue = blue;
        }
    }

    public int getGreen() {
        return green;
    }

    public void incrementGreen(int green) {
        if(green > this.green) {
            this.green = green;
        }
    }
}
