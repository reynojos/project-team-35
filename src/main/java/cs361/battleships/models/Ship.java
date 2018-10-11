package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    private String type;
	private List<Square> occupiedSquares;

	public Ship(String type) {
		this.type = type;
		occupiedSquares = new ArrayList<>();
	}


	public Ship() {
		occupiedSquares = new ArrayList<>();
		type="MINESWEEPER";
	}

	public String getType(){
	    return type;
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

	public void place(Square pos, Boolean isVertical){ ;

        int length=getLength();

        //how much should be added
        int startingRow = pos.getRow();
        char startingCol = pos.getColumn();

        if (isVertical){
            for (int i=0; i<length; i++){
                Square newSquare = new Square(startingRow+i, startingCol);
                occupiedSquares.add(newSquare);
            }
        }
        else {
            for (int i = 0; i < length; i++) {
                Square newSquare = new Square(startingRow, (char)(startingCol+i));
                occupiedSquares.add(newSquare);
            }
        }
    }

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}
}
