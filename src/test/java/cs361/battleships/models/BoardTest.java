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

        // Test: If captains quarters are there return sunk
        AttackStatus hit = board.hasHitShip(1, 'B');
        assertTrue( hit == AttackStatus.HIT );

        // Test: If captains quarters are there return sunk
        hit = board.hasHitShip(1, 'A');
        assertTrue( hit == AttackStatus.SUNK );

        // Test: If ship isn't there return false
        hit = board.hasHitShip(1, 'D');
        assertFalse( hit == AttackStatus.HIT );
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

        // Test: Miss
        ship = board.findHit(5, 'J');
        assertTrue( ship == null );
    }

    @Test
    public void testCheckGame() {

        // Set up
        Board board = new Board();

        // Create a list of results to feed into board.attacks variable
        List<Result> resultList = new ArrayList<Result>();
        boolean win;    // Win condition checking
        // 5 Result objects containing 1 hit, 1 miss, 3 sunk
        Result result = new Result();
        Result result2 = new Result();
        Result result3 = new Result();
        Result result4 = new Result();
        Result result5 = new Result();

        // Test: Check win condition on new game
        win = board.checkGame();
        assertFalse( win );

        // Adding one sunk to the attacks list
        result.setResult(AttackStatus.SUNK);
        resultList.add(result);
        board.setAttacks(resultList);

        // Test: Check win condition on first sunk
        win = board.checkGame();
        assertFalse( win );

        // Adding a hit to the attacks list
        result2.setResult(AttackStatus.HIT);
        resultList.add(result2);
        board.setAttacks(resultList);

        // Test: Check win condition on 1 sunk, 1 hit
        win = board.checkGame();
        assertFalse( win );

        // Adding a miss to the attacks list
        result3.setResult(AttackStatus.MISS);
        resultList.add(result3);
        board.setAttacks(resultList);

        // Test: Check win condition on 1 sunk, 1 hit, 1 miss
        win = board.checkGame();
        assertFalse( win );

        // Adding second sunk to the attacks list
        result4.setResult(AttackStatus.SUNK);
        resultList.add(result4);
        board.setAttacks(resultList);

        // Test: Check win condition on 2 sunk, 1 hit, 1 miss
        win = board.checkGame();
        assertFalse( win );

        // Adding third sunk to the attacks list
        result5.setResult(AttackStatus.SUNK);
        resultList.add(result5);
        board.setAttacks(resultList);

        // Test: Check win condition on 3 sunk, 1 hit, 1 miss
        win = board.checkGame();
        assertTrue( win );
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
    public void testSetGetShips() {
        // Set up
        Board board = new Board();

        // Ships for adding to ships list
        Ship ship = new Ship("MINESWEEPER");
        Ship ship2 = new Ship("BATTLESHIP");
        Ship ship3 = new Ship("DESTROYER");

        Result result = new Result();
        Result result2 = new Result();
        Result result3 = new Result();

        // List of the ships
        List<Ship> shipList = new ArrayList<Ship>();

        // Adding a minesweeper to the ships list
        result.setShip(ship);
        shipList.add(result.getShip());
        board.setShips(shipList);

        // Adding a battleship to the ships list
        result2.setShip(ship2);
        shipList.add(result2.getShip());
        board.setShips(shipList);

        // Adding a destroyer to the ships list
        result3.setShip(ship3);
        shipList.add(result3.getShip());
        board.setShips(shipList);

        // Create a Ships list of all of the ships
        List<Ship> allShips = board.getShips();

        // Test whether the allShips list that was set by setAttack, and got by
        // getAttack, is equal to the shipList that was added to the board.ships list
        for(int i = 0; i < allShips.size(); i++) {
            assertTrue(allShips.get(i) == shipList.get(i));
        }
    }

    @Test
    public void testSetGetAttacks() {

        // Set up
        Board board = new Board();

        // Results for adding to attacks list
        Result result = new Result();
        Result result2 = new Result();
        Result result3 = new Result();
        Result result4 = new Result();
        Result result5 = new Result();

        // List of the results
        List<Result> resultList = new ArrayList<Result>();

        // Adding one sunk to the attacks list
        result.setResult(AttackStatus.SUNK);
        resultList.add(result);
        board.setAttacks(resultList);

        // Adding a hit to the attacks list
        result2.setResult(AttackStatus.HIT);
        resultList.add(result2);
        board.setAttacks(resultList);

        // Adding a miss to the attacks list
        result3.setResult(AttackStatus.MISS);
        resultList.add(result3);
        board.setAttacks(resultList);

        // Adding second sunk to the attacks list
        result4.setResult(AttackStatus.SUNK);
        resultList.add(result4);
        board.setAttacks(resultList);


        // Adding third sunk to the attacks list
        result5.setResult(AttackStatus.SUNK);
        resultList.add(result5);
        board.setAttacks(resultList);

        // Create a Result list of all of the attacks
        List<Result> allAttacks = board.getAttacks();

        // Test whether the allAttacks list that was set by setAttack, and got by
        // getAttack, is equal to the resultList that was added to the board.attacks list
        for(int i = 0; i < allAttacks.size(); i++) {
            assertTrue(allAttacks.get(i) == resultList.get(i));
        }
    }
}
