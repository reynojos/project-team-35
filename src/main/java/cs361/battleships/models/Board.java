package cs361.battleships.models;

import controllers.AttackGameAction;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private List<Ship> ships; // track ships on board
	private List<Result> attacks; // track the atnnjfsfstacks that were attempted from Result class
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		this.ships = new ArrayList<Ship>();
		this.attacks = new ArrayList<Result>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {

		// Check every ship
		for (Ship currentShip: ships){

			//Check every exisiting ship and make sure it has not been placed
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

	public boolean hasBeenSelected(int x, int y){
		//Get attacks
		List<Result> previous_attacks = getAttacks();

		//Check the array of previous attacks
		for(int i = 0; i < previous_attacks.size(); i++){
			Square attack_location = previous_attacks.get(i).getLocation();
			if(attack_location != null){
				int testRow = attack_location.getRow();
				char testCol = attack_location.getColumn();
				if(x == testRow && y == testCol){
					return true;
				}
			}
		}
		return false; //If the for loop above is passed, that the attack was not previously recorded
 	}

 	public boolean hasHitShip(int x, int y){
		//Check every ship and their occupied spots to see if there is a hit
		for(int i = 0; i < ships.size(); i++){
			List<Square> occupiedSquares = ships.get(i).getOccupiedSquares();
			//For all occupied squares of a ship
			for(int j = 0; j < occupiedSquares.size(); j++){
				Square boat_location = occupiedSquares.get(j);
				if(boat_location != null){
					if(x == boat_location.getRow() && y == boat_location.getColumn()){
						return true;
					}
				}
			}
		}
		return false; //return false if nothing has been hit
	}

	public Ship findHit(int x, int y){
		//Check every ship check if it has been hit
		for(int i = 0; i < ships.size(); i++){
			List<Square> occupiedSquares = ships.get(i).getOccupiedSquares();
			//For all occupied squares of a ship
			for(int j = 0; j < occupiedSquares.size(); j++){
				Square boat_location = occupiedSquares.get(j);
				if(boat_location != null){
					if(x == boat_location.getRow() && y == boat_location.getColumn()){
						return ships.get(i); //return what ship has been hit
					}
				}
			}
		}
		return null; //return nothing (should not be the case because we assumed a hit)
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Result attempt = new Result(); //new attack that will be placed

		//Make sure the x is within range
		if(x >= 11 || x <= 0){
			attempt.setResult(AttackStatus.INVALID);
			return attempt;
		}

		//Make sure that the char is valid and within range
		if(y >= 'K' || y < 'A'){
			attempt.setResult(AttackStatus.INVALID);
			return attempt;
		}

		//Make sure that this spot has not been previously attacked
		if(hasBeenSelected(x, y)){
			attempt.setResult(AttackStatus.INVALID);
			return attempt;
		}

		//If the test cases above passed, we can pass our coordinates into the array that holds the attacks
		//We can check if our attempt has hit a ship


		if(hasHitShip(x, y)){
			Ship hitShip = findHit(x,y);
			attempt.setResult(AttackStatus.HIT);
			attempt.setLocation(new Square(x, y));
			attempt.setShip(hitShip);

			//Add attack to list of attacks
			attacks.add(attempt);

			//Return which ship was attacked, and add hit marker


		}
		//if hit missed
		else{
			//Return a miss and set this location to an event
			attempt.setResult(AttackStatus.MISS);
			attempt.setLocation(new Square(x, y));

			//Add attack to list of attacks
			attacks.add(attempt);



			return attempt;
		}

		return attempt;
	}

	public List<Ship> getShips() {
		return this.ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

	public List<Result> getAttacks() {
		return this.attacks;
	}

	public void setAttacks(List<Result> attacks) {
		this.attacks = attacks;
	}
}
