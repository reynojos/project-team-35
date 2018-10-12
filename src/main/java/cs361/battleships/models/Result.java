package cs361.battleships.models;

public class Result {

    private Ship ship; //Ship object that will be given a result based off an action
	private AttackStatus status; //AttackStatus object that lets user know the status of an attack
	private Square spot; //Each square will have a result attached


	public AttackStatus getResult() {
		return status;
	}

	public void setResult(AttackStatus result) {
		status = result;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public Square getLocation() {
		return spot;
	}

	public void setLocation(Square square) {
		spot = square;
	}
}
