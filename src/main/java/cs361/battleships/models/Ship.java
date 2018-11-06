package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

import static cs361.battleships.models.AttackStatus.*;

public class Ship {

    @JsonProperty private List<Square> occupiedSquares;
    private String type;
    private int length;
    private int hitLength;
    private boolean captainHit;

	public Ship(String type) {
		this.type = type;
		this.hitLength = 0;
		this.captainHit = false;
		occupiedSquares = new ArrayList<Square>();
        //Have a switch statement that makes the
        //ship a certain length depending on the size
		switch(type){
            case "MINESWEEPER":
                length = 2;
                break;
            case "DESTROYER":
                length = 3;
                break;
            case "BATTLESHIP":
                length = 4;
                break;
        }
	}

	public void setHitLength(int i){
	    hitLength = i;
    }

    public int getHitLength(){
	    return hitLength;
    }

	public Ship() {
		occupiedSquares = new ArrayList<>();
	}

	public String getType(){
	    return this.type;
    }

	public int getLength(){
        switch(type){
            case "MINESWEEPER":
                return 2;
            case "DESTROYER":
                return 3;
            case "BATTLESHIP":
                return 4;
        }
        return -1;
    }

	public void place(Square pos, Boolean isVertical){

        int length=getLength();

        //how much should be added
        int startingRow = pos.getRow();
        char startingCol = pos.getColumn();

        //If the boat is vertical, the dimensions that the squares are allocated
        //must represent a vertical pattern
        if (isVertical){
            for (int i=0; i<length; i++){
                Square newSquare = new Square(startingRow+i, startingCol);

                if (type.equals("MINESWEEPER") && i == 0)
                    newSquare.setCaptainsQ(true);
                else if (!type.equals("MINESWEEPER") && i == 1)
                    newSquare.setCaptainsQ(true);

                occupiedSquares.add(newSquare);
            }
        }
        else {
            for (int i = 0; i < length; i++) {
                Square newSquare = new Square(startingRow, (char)(startingCol+i));

                if (type.equals("MINESWEEPER") && i == 0)
                    newSquare.setCaptainsQ(true);
                else if (!type.equals("MINESWEEPER") && i == 1)
                    newSquare.setCaptainsQ(true);

                occupiedSquares.add(newSquare);
            }
        }
    }

    // set captain hit to true
    public void hitCaptain(){
	    captainHit = true;
    }

    // return whether captain has been hti
    public boolean isCaptainHit(){
        return captainHit;
    }

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}
}
