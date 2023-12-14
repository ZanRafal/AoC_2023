package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @ParameterizedTest
    @CsvSource({
            "four82nine74, 482974",
            "hlpqrdh3, hlpqrdh3",
            "eightsevenhrsseven988, 87hrs7988",
            "324pzonenine, 324pz19",
            "fglpbone79fourvrgcmgklbmthree, fglpb1794vrgcmgklbm3",
            "fmbbkvthdcdmcjxzclk42six4, fmbbkvthdcdmcjxzclk4264",
            "four22xcqsnvktnpfshtmm, 422xcqsnvktnpfshtmm",
            "qmfsccxsixfivelnmpjqjcsc1sixpfpmeight, qmfsccx65lnmpjqjcsc16pfpm8",
            "eight1nine5nine9six, 8195996",
            "s4r91seven, s4r917",
            "6pspkslrnxpplkhgqlcqfour, 6pspkslrnxpplkhgqlcq4",
            "sixeightnzrzgjvsrnmtqgx5, 68nzrzgjvsrnmtqgx5",
            "sixtwo1, 621",
            "h6, h6",
            "five8pbcsllrbvg787, 58pbcsllrbvg787",
            "dpfhfeight28onefourtwo, dpfhf828142",
    })
    public void testChangeWordToNumber(String input, String expectedOutput) {
       Main converter = new Main();
        String result = converter.replaceLetteredNumbers(input);
        assertEquals(expectedOutput, result);
    }

}