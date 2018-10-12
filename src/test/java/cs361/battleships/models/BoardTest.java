package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class BoardTest {

    @Test
    public void testAttack(){
        // set up board for testing.
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', false);
        board.placeShip(new Ship("DESTROYER"), 9, 'A', false);
        board.placeShip(new Ship("BATTLESHIP"), 10, 'A', false);

        assertFalse(board.checkGame());

        // Test: barely miss
        AttackStatus attack = board.attack(2, 'A').getResult();
        assertTrue(attack == AttackStatus.MISS);

        // Test: repeat/invalid
        attack = board.attack(2, 'A').getResult();
        assertTrue(attack == AttackStatus.INVALID);

        // Test: hit
        attack = board.attack(1, 'A').getResult();
        assertTrue(attack == AttackStatus.HIT);

        // Test: out of bounds/invalid
        attack = board.attack(0, 'A').getResult();
        assertTrue(attack == AttackStatus.INVALID);

        // Test: sunken, and checkWin based on surrender value
        attack = board.attack(1, 'B').getResult();
        assertFalse(attack == AttackStatus.SURRENDER);
        assertTrue(attack == AttackStatus.SUNK);

        // Test: sunk set up for surrender & checkGame function test for false
        attack = board.attack(9, 'A').getResult();
        assertTrue(attack == AttackStatus.HIT);
        attack = board.attack(9, 'B').getResult();
        assertTrue(attack == AttackStatus.HIT);

        attack = board.attack(9, 'C').getResult();
        assertFalse(attack == AttackStatus.SURRENDER);
        assertTrue(attack == AttackStatus.SUNK);

        // Test: surrender (checkGame function)
        attack = board.attack(10, 'A').getResult();
        assertTrue(attack == AttackStatus.HIT);
        attack = board.attack(10, 'B').getResult();
        assertTrue(attack == AttackStatus.HIT);
        attack = board.attack(10, 'C').getResult();
        assertTrue(attack == AttackStatus.HIT);
        attack = board.attack(10, 'D').getResult();
        assertFalse(attack == AttackStatus.SUNK);
        assertTrue(attack == AttackStatus.SURRENDER);

    }

    @Test
    public void testHasBeenSelected() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', false);

        board.attack(1, 'A');

        boolean selected = board.hasBeenSelected(1, 'A');
        assertTrue( selected );

        selected = board.hasBeenSelected(2, 'A');
        assertFalse( selected );
    }

    @Test
    public void testHasHitShip() {

        // Set up
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', false);

        // Test: If ship is there return true
        boolean hit = board.hasHitShip(1, 'A');
        assertTrue( hit );

        // Test: If ship isn't there return false
        hit = board.hasHitShip(1, 'D');
        assertFalse( hit );
    }

    @Test
    public void testFindHit() {
        // Set up
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', false);
        board.attack(1, 'A');

        // Test: Hit minesweeper ship
        Ship ship = board.findHit(1, 'A');
        assertTrue( ship.getType() == "MINESWEEPER" );

        ship = board.findHit(5, 'J');
        assertTrue( ship == null );
    }

    @Test
    public void testBoardPlacement() {

        // initiate 2 boards to compare
        Board board = new Board();
        Board board2 = new Board();

        // Make sure we can't place a ship out of bounds
        // col
        assertFalse(board.placeShip(new Ship("DESTROYER"), 10, 'A', true));
        // row
        assertFalse(board.placeShip(new Ship("DESTROYER"), 1, 'J', false));

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

    @Test
    public void testGetSetShips() {
        Board board = new Board();
        board.placeShip(new Ship("MINSWEEPER"), 1, 'A', false);
    }
}
