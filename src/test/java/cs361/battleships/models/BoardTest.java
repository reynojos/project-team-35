package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testValidPlacement() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship(ShipType.MINESWEEPER), 0, 'A', true));
    }

    @Test
    public void testOverlapPlacement() {
        Board board = new Board();
        board.placeShip(new Ship(ShipType.DESTROYER), 0, 'A', true);
        assertFalse(board.placeShip(new Ship(ShipType.MINESWEEPER), 1, 'A', false));
    }
}
