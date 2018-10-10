package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testValidPlacement() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 0, 'A', true));
    }

    @Test
    public void testOverlapPlacement() {
        Board board = new Board();
        board.placeShip(new Ship("DESTROYER"), 1, 'A', true);
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 2, 'A', false));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 2, 'B', false));
    }
}
