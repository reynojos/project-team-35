package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Square {

	private int row;
	private char column;

	public Square(){}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
	}

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
