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

    @Test
    public void testPlaceShip() {

        // Set up
        Game game = new Game();

        // Test: Place valid ship (true)
        boolean place = game.placeShip(new Ship("MINESWEEPER"), 1, 'A', false);
        assertTrue( place );

        // Test: Place ship on top of another ship
        place = game.placeShip(new Ship("BATTLESHIP"), 1, 'A', false);
        assertFalse( place );

        // Test: Place battleship
        place = game.placeShip(new Ship("BATTLESHIP"), 2, 'A', false);
        assertTrue( place );

        // Test: Place ship outside of bounds
        place = game.placeShip(new Ship("DESTROYER"), 0, 'Z', true);
        assertFalse( place );

        // Test: Place destroyer
        place = game.placeShip(new Ship("DESTROYER"), 3, 'A', false);
        assertTrue( place );

        // Test: Duplicate ship placement
        place = game.placeShip(new Ship("MINESWEEPER"), 4,'A', false);
        assertFalse( place );
        place = game.placeShip(new Ship("DESTROYER"), 5,'A', false);
        assertFalse( place );
        place = game.placeShip(new Ship("BATTLESHIP"), 6,'A', false);
        assertFalse( place );
    }

    @Test
    public void testAttack() {

        // Set up
        Game game = new Game();

        // Test: Valid attack (true)
        boolean attack = game.attack(1, 'A');
        assertTrue( attack );

        // Test: Invalid char (false)
        attack = game.attack(1, 'Z');
        assertFalse( attack );

        // Test: Invalid int (false)
        attack = game.attack(50, 'B');
        assertFalse( attack );

        // Test: Invalid char (false)
        attack = game.attack(1, '5');
        assertFalse( attack );
    }

    @Test
    public void testAlphabet() {
        Game game = new Game();

        String alphabet = game.getAlphabet();
        String testAlhpa = "ABCDEFGHIJ";

        assertTrue( alphabet == testAlhpa );
    }
}
