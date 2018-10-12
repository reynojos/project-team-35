package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResultTest {
    @Test
    public void testNewResult(){

        // Test: result, ship and location are set to null
        Result result = new Result();

        assertTrue(result.getResult() == null);
        assertTrue(result.getLocation() == null);
        assertTrue(result.getShip() == null);

        // Test: all attack status constants are setting and returning
        result.setResult(AttackStatus.INVALID);
        assertTrue(result.getResult() == AttackStatus.INVALID);
        result.setResult(AttackStatus.MISS);
        assertTrue(result.getResult() == AttackStatus.MISS);
        result.setResult(AttackStatus.HIT);
        assertTrue(result.getResult() == AttackStatus.HIT);
        result.setResult(AttackStatus.SUNK);
        assertTrue(result.getResult() == AttackStatus.SUNK);
        result.setResult(AttackStatus.SURRENDER);
        assertTrue(result.getResult() == AttackStatus.SURRENDER);

        // Test: location is setting and returning to a valid square
        Square square = new Square(1,'A');
        result.setLocation(square);
        assertTrue(result.getLocation().equals(square));

        // Test: ship is setting and returning to a valid ship
        Ship ship = new Ship();
        result.setShip(ship);
        assertTrue(result.getShip().equals(ship));
    }
}
