package cs361.battleships.models;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.ArrayList;

public class ShipTest {
    Ship newShip;

    @Test
    public void testGetSquares(){
        Ship ship = new Ship("MINESWEEPER");

        List squares = ship.getOccupiedSquares();

        assertTrue(squares.size() == 0);
    }

    @Test
    public void testPlacements(){
        List<String> types = new ArrayList<>();
        types.add("MINESWEEPER");
        types.add("DESTROYER");
        types.add("BATTLESHIP");

        int length = types.size();

        for (int i=0; i<length; i++){
            testPlacement(types.get(i), true);
            testPlacement(types.get(i), false);
        }
    }


    // Test the placement of a new ship
    private void testPlacement(String type, Boolean isVertical){
        char col = 'A';
        int row = 0;

        newShip = new Ship(type);
        newShip.place(new Square(0, 'A'),isVertical);

        List<Square> squares = newShip.getOccupiedSquares();

        assertTrue(squares.size() == newShip.getLength());

        if (isVertical) {
            for (int i = 0; i < newShip.getLength()-1; i++) {
                assertTrue(squares.get(i).getColumn() == col);
                assertTrue(squares.get(i).getRow() == row + i);
            }
        }
        else {
            for (int i = 0; i < newShip.getLength()-1; i++) {
                assertTrue(squares.get(i).getRow() == row);
                assertTrue(squares.get(i).getColumn() == (char) (col + i));
            }
        }

        if (type == "MINESWEEPER"){
            assertTrue(newShip.getLength() == 2);
        }
        if (type == "DESTROYER"){
            assertTrue(newShip.getLength() == 3);
        }
        if (type == "BATTLESHIP"){
            assertTrue(newShip.getLength() == 4);
        }

    }

}
