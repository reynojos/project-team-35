package cs361.battleships.models;

import java.util.ArrayList;

public class Battleship extends Ship {
    public Battleship(){
        //Construct variables inherited
        this.type = "BATTLESHIP";
        this.length = 4;
        this.occupiedSquares = new ArrayList<Square>();
        this.hitLength = 0;
    }
}
