package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SquareTest {

    @Test
    public void testNewSquare() {

        // Test: Default Constructor, setRow, setColumn, getRow, getColumn
        Square square1 = new Square();

        square1.setRow(1);
        square1.setColumn('A');
        assertTrue(square1.getRow() == 1);
        assertTrue(square1.getColumn() == 'A');
        assertFalse(square1.getRow() == 9);
        assertFalse(square1.getColumn() == 'Z');
        assertFalse(square1.getRow() != 1);
        assertFalse(square1.getColumn() != 'A');

        // check if captains quarter was set correctly
        assertFalse(square1.isCaptainsQ());

        // Test: Constructor with bool, getRow, getColumn
        Square square = new Square(1, 'A', true);
        assertTrue(square.getRow() == 1);
        assertTrue(square.getColumn() == 'A');
        assertFalse(square.getRow() == 9);
        assertFalse(square.getColumn() == 'Z');
        assertFalse(square.getRow() != 1);
        assertFalse(square.getColumn() != 'A');

        // check if captains quarter was set correctly
        assertTrue(square.isCaptainsQ());

        // Test: Constructor with no bool, getRow, getColumn
        Square square2 = new Square(1, 'A');
        assertTrue(square2.getRow() == 1);
        assertTrue(square2.getColumn() == 'A');
        assertFalse(square2.getRow() == 9);
        assertFalse(square2.getColumn() == 'Z');
        assertFalse(square2.getRow() != 1);
        assertFalse(square2.getColumn() != 'A');

        // check if captains quarter was set correctly
        assertFalse(square2.isCaptainsQ());
    }
}