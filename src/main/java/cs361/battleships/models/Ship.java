package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares;

	private ShipType type;

	public Ship(ShipType type) {
		this.type = type;

	}

	public List<Square> getOccupiedSquares() {
		//TODO implement
		return null;
	}
}
