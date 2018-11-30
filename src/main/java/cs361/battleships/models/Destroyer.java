package cs361.battleships.models;

import java.util.ArrayList;

public class Destroyer extends Ship{

    public Destroyer(){
        //Construct variables inherited
        this.type = "DESTROYER";
        this.length = 3;
        this.occupiedSquares = new ArrayList<Square>();
        this.submerged = false;
        this.hitLength = 0;
    }
}
