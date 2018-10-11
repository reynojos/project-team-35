package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

public class BoardTest {

    @Test
    public void testValidPlacement() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 0, 'A', true));
    }

    @Test
    public void testOverlapPlacement() {
        Board board = new Board();
        Board board2 = new Board();

        board.placeShip(new Ship("DESTROYER"), 1, 'A', true);
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 2, 'A', false));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 2, 'B', false));
        assertTrue(board.getShips() != null);
        List<Ship> ships = board.getShips();



        for (Ship ship: ships){
            assertTrue(ship.getLength() == ship.getOccupiedSquares().size());
        }

        board2.placeShip(new Ship("DESTROYER"), 5, 'C', true);

        assertTrue(board2.getShips().get(0).getOccupiedSquares().size() < 4);
    }
}
