package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testMainWhenExtrapolatedNumberIs1ThenPrints1() throws Exception {
        // Arrange
        List<Integer[]> tableNumbers = Arrays.asList(new Integer[]{1, 2, 3});
        int index = 0;

        // Act
        Main.main(new String[]{});

        // Assert
        assertEquals("1\n", outContent.toString());
    }

    @Test
    public void testMainWhenExtrapolatedNumberIs2ThenPrints2() throws Exception {
        // Arrange
        List<Integer[]> tableNumbers = Arrays.asList(new Integer[]{2, 3, 4});
        int index = 0;

        // Act
        Main.main(new String[]{});

        // Assert
        assertEquals("2\n", outContent.toString());
    }
}