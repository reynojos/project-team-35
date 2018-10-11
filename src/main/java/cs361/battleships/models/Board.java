package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Board {

	private List<Ship> ships; // track ships on board

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		ships = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {

		// Check every ship
		for (Ship currentShip: ships){
			if (ship.getType().equals(currentShip.getType())){
				return false;
			}
			// Check every square in current ship
			for (Square square: currentShip.getOccupiedSquares()){
				//keep original x and y so we can keep testing past first iteration.
				int x_updated = x;
				char y_updated = y;
				// and every square in potential ship
				for (int i=0; i < ship.getLength(); i++){

					if (square.getColumn() == y_updated && square.getRow() == x_updated){
						return false;
					}
					if (isVertical)
						x_updated++;
					else
						y_updated++;
				}
			}
		}

		// If we've gotten to this point then we can add the ship.
		ship.place(new Square(x, y), isVertical);
		ships.add(ship);

		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//TODO Implement
		return null;
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

	public List<Result> getAttacks() {
		//TODO implement
		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
	}
}
