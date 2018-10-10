package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

public class Ship {

	private ShipType type;
	@JsonProperty private List<Square> occupiedSquares;

	public Ship(ShipType type) {
		this.type = type;
		this.occupiedSquares = new ArrayList<>();
	}


	public Ship() {
		occupiedSquares = new ArrayList<>();
	}

    public Ship(String type) {
        if (type == "MINESWEEPER"){
            this.type=ShipType.MINESWEEPER;
        }
        else if (type == "DESTROYER"){
            this.type=ShipType.DESTROYER;
        }
        else if (type  == "BATTLESHIP"){
            this.type=ShipType.BATTLESHIP;
        }
        else{
            // something went wrong;
        }
    }

	public int getLength(){
        switch(type){

            case MINESWEEPER:
                return 2;
            case DESTROYER:
                return 3;
            case BATTLESHIP:
                return 4;
        }

        return -1;
    }

	public void place(Square pos, Boolean isVertical){
        int length = 0;
	    occupiedSquares.add(pos);

        switch(type){

            case MINESWEEPER:
                length = 2;
                break;
            case DESTROYER:
                length = 3;
                break;
            case BATTLESHIP:
                length = 4;
                break;
        }

        //how much should be added
        int startingRow = pos.getRow();
        char startingCol = pos.getColumn();

        if (isVertical){
            for (int i=1; i<length; i++){
                Square newSquare = new Square(startingRow+i, startingCol);
                occupiedSquares.add(newSquare);
            }
        }
        else {
            for (int i = 1; i < length; i++) {
                Square newSquare = new Square(startingRow, (char)(startingCol+i));
                occupiedSquares.add(newSquare);
            }
        }
    }

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}
}
