package cs361.battleships.models;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.util.List;

public class ShipTest {
    Ship newShip;

    @Test
    public void testGetSquares(){
        Ship ship = new Ship(ShipType.MINESWEEPER);

        List squares = ship.getOccupiedSquares();

        assertTrue(squares.size() == 0);
    }

    @Test
    public void testPlacements(){
        ShipType types[] = ShipType.values();
        int length = types.length;

        for (int i=0; i<length; i++){
            testPlacement(types[i], true);
            testPlacement(types[i], false);
        }
    }


    // Test the placement of a new ship
    private void testPlacement(ShipType type, Boolean isVertical){
        char col = 'A';
        int row = 0;

        newShip = new Ship(type);
        newShip.place(new Square(0, 'A'),isVertical);

        List<Square> squares = newShip.getOccupiedSquares();

        assertTrue(squares.size() == newShip.getLength());

        if (isVertical) {
            for (int i = 0; i < newShip.getLength(); i++) {
                assertTrue(squares.get(i).getColumn() == col);
                assertTrue(squares.get(i).getRow() == row + i);
            }
        }
        else {
            for (int i = 0; i < newShip.getLength(); i++) {
                assertTrue(squares.get(i).getRow() == row);
                assertTrue(squares.get(i).getColumn() == (char) (col + i));
            }
        }

    }

}
