package cs361.battleships.models;

import java.util.ArrayList;

public class Submarine extends Ship {

    public Submarine() {
        //Construct variables inherited
        this.type = "SUBMARINE";
        this.length = 5;
        this.submerged = false;
        this.occupiedSquares = new ArrayList<Square>();
        this.hitLength = 0;
    }
}