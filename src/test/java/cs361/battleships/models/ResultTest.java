package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResultTest {
    @Test
    public void testNewResult(){
        Result result = new Result();

        assertTrue(result.getResult() == null);
        assertTrue(result.getLocation() == null);
        assertTrue(result.getShip() == null);

        result.setResult(AttackStatus.INVALID);
        assertTrue(result.getResult() == AttackStatus.INVALID);
        result.setResult(AttackStatus.MISS);
        assertTrue(result.getResult() == AttackStatus.MISS);
        result.setResult(AttackStatus.HIT);
        assertTrue(result.getResult() == AttackStatus.HIT);

        Square square = new Square(1,'A');
        result.setLocation(square);
        assertTrue(result.getLocation().equals(square));

        Ship ship = new Ship();
        result.setShip(ship);
        assertTrue(result.getShip().equals(ship));
    }
}
