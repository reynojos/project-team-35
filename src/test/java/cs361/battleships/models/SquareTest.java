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

        // Test: Override constructor, getRow, getColumn
        Square square = new Square(1, 'A');
        assertTrue(square.getRow() == 1);
        assertTrue(square.getColumn() == 'A');
    }
}