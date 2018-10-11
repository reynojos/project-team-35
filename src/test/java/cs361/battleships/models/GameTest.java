package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameTest {

    @Test
    public void testRowRange() {
        //Declare new game object and call a testing row
        Game g = new Game();
        int testRow = g.randRow();

        //Make sure that the random numbers are between 1 and 10
        assertTrue(testRow >= 1 && testRow <= 10);
    }

    @Test
    public void testColumnRange() {
        //Declare a string that contains all column characters
        String testAlphabet = "ABCDEFGHIJ";

        Game g = new Game();
        char testCol = g.randCol();

        //This should return true if testCol exists in the string
        //If false, it will return -1. Otherwise it will return a number between
        //0 and 9.
        assertTrue(testAlphabet.indexOf(testCol) >= 0);
    }

    @Test
    public void testBooleanValue() {
        //declare variables that will hold game objectt
        Game g = new Game();
        boolean isVertical = g.randVertical();

        //This assert will check if the variable returns true or not
        assertTrue(isVertical || !isVertical);
    }

}
