package cs361.battleships.models;

import java.util.ArrayList;

public class Minesweeper extends Ship{

    public Minesweeper(){
        //Construct variables inherited
        this.type = "MINESWEEPER";
        this.length = 2;
        this.occupiedSquares = new ArrayList<Square>();
        this.hitLength = 0;
    }

    /*Setting captains quarters will go here, as it overrides the parent class*/


}
