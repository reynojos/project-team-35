package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Square {

	private int row;
	private char column;
	private boolean captainsQ;

	public Square(){
		captainsQ = false;
	}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
		this.captainsQ = false;
	}
	public Square(int row, char column, boolean captainsQ) {
		this.row = row;
		this.column = column;
		this.captainsQ = captainsQ;
	}

	public boolean isCaptainsQ(){ return captainsQ; }

	public void setCaptainsQ(boolean isQ){ this.captainsQ = isQ; }

	public char getColumn() {
		return column;
	}

	public void setColumn(char column) {
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
}
