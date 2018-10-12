package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class BoardTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 0, 'A', true));
    }

    @Test
    public void testOverlapPlacement() {

        // initiate 2 boards to compare
        Board board = new Board();
        Board board2 = new Board();

        // place a ship on the board
        board.placeShip(new Ship("DESTROYER"), 1, 'A', true);

        //ensure that the placed ship's getType method returns the right type.
        assertTrue(board.getShips().get(0).getType().equals("DESTROYER"));

        // place another interfering ship and check that it returns false
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 2, 'A', false));

        // place a non interfering ship and check that it returns true
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 2, 'B', false));

        // check that there are ships on the board
        assertTrue(board.getShips() != null);

        // 3 ships should have been added to board
        assertTrue(board.getShips().size() == 2);

        // check that the getLength method returns the correct length.
        List<Ship> ships = board.getShips();
        for (Ship ship: ships){
            assertTrue(ship.getLength() == ship.getOccupiedSquares().size());
        }

        // check that a duplicate ship cannot be placed.
        assertFalse(board.placeShip(new Ship("DESTROYER"), 5, 'E', false));

        //test setShips method

        List<Ship> newShip = new ArrayList<Ship>();
        board.setShips(newShip);

        // Make sure size is 0, matching new list of ships
        assertTrue(board.getShips().size() == 0);

        newShip.add(new Ship("MINESWEEPER"));
        newShip.add(new Ship("DESTROYER"));
        newShip.add(new Ship("BATTLESHIP"));

        board.setShips(newShip);

        // Test new size of ships
        assertTrue(board.getShips().size() == 3);


    }
}
