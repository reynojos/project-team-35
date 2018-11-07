package cs361.battleships.models;

import controllers.AttackGameAction;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private List<Ship> ships; // track ships on board

	private List<Result> attacks; // track the attacks that were attempted from Result class

	private int sunkShips;

	public static char MAXCOL = 'J';
	public static char MAXROW = 10;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		this.ships = new ArrayList<Ship>();
		this.attacks = new ArrayList<Result>();
		this.sunkShips = 0;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {

		if (isVertical){
			if (x+ship.getLength()-1 > MAXROW)
				return false;
		}
		else {
			if (y+ship.getLength()-1 > MAXCOL)
				return false;
		}

		// Check every ship
		for (Ship currentShip: ships){

			//Check every existing ship and make sure it has not been placed
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

 	public boolean hasShip(int x, int y){
		//Check for ship
		for(int i = 0; i < ships.size(); i++) {
			Ship currentShip = ships.get(i);
			List<Square> occupiedSquares = currentShip.getOccupiedSquares();
			//For all occupied squares of a ship
			for (int j = 0; j < occupiedSquares.size(); j++) {
				Square boat_location = occupiedSquares.get(j);
				if (boat_location != null) {
					if (x == boat_location.getRow() && y == boat_location.getColumn()) {
						return true;
					}
				}
			}
		}
		return false;
	}

 	public AttackStatus hasHitShip(int x, int y){
		//Check every ship and their occupied spots to see if there is a hit
		for(int i = 0; i < ships.size(); i++){
			Ship currentShip = ships.get(i);
			List<Square> occupiedSquares = currentShip.getOccupiedSquares();
			//For all occupied squares of a ship
			for(int j = 0; j < occupiedSquares.size(); j++){
				Square boat_location = occupiedSquares.get(j);
				if(boat_location != null){
					if(x == boat_location.getRow() && y == boat_location.getColumn()){
						if (boat_location.isCaptainsQ()){
							if (currentShip.getType().equals("MINESWEEPER") || currentShip.isCaptainHit()) {
								sunkShips++;
								return AttackStatus.SUNK;
							}
							else {
								currentShip.hitCaptain();
								return AttackStatus.CAPTAINHIT;
							}
						}
						return AttackStatus.HIT;
					}
				}
			}
		}
		return AttackStatus.MISS; //return false if nothing has been hit
	}

	// Check what ship has been hit
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

	public boolean checkGame(){
		List<Result> allAttacks = getAttacks();
		int sunkenMarks = 0;
		//Get all the attacks and count the number of sinks in those attacks.
		for(int i = 0; i < allAttacks.size(); i++){
			if(attacks.get(i).getResult() == AttackStatus.SUNK) {
				sunkenMarks++;
			}
		}
		//If the total sunken marks equals 3, its game over
		if(sunkenMarks == 3){
			return true;
		}
		else{
			return false;
		}
	}

	public Result sonarAttack(int x, char y){
		//Declare list that contains all sonar squares
		Result selectedSquare = new Result();

		List<Square> sonarSquares = new ArrayList<Square>();

		//Add all squares 2 squares to each side of x
		sonarSquares.add((new Square(x, y)));
		sonarSquares.add((new Square(x, (char)( ((int)y) + 1) )));
		sonarSquares.add((new Square(x, (char)( ((int)y) + 2) )));
		sonarSquares.add((new Square(x, (char)( ((int)y) - 1) )));
		sonarSquares.add((new Square(x, (char)( ((int)y) - 2) )));

		//Add only one to each side of the row above and below
		sonarSquares.add((new Square(x + 1, (char)( ((int)y) - 1) )));
		sonarSquares.add((new Square(x + 1, (char)( ((int)y) + 1) )));
		sonarSquares.add((new Square(x + 1, y)));

		sonarSquares.add((new Square(x - 1, (char)( ((int)y) - 1) )));
		sonarSquares.add((new Square(x - 1, (char)( ((int)y) + 1) )));
		sonarSquares.add((new Square(x - 1, y)));

		//Add the very tips of the sonar attacks
		sonarSquares.add((new Square(x + 2, y)));
		sonarSquares.add((new Square(x - 2, y)));

		//Now iterate through all the new squares and check if there are existing ships on it.
		//If there are squares, add them to the attacks, attached with a status specific to sonar attacks
		for(int i = 0; i < sonarSquares.size(); i++){
			Square location = sonarSquares.get(i);
			boolean occupied = hasShip(location.getRow(), location.getColumn());
			Result currentSonarSquare = new Result();
			if(occupied){
				//if occupied, register it as a attack with special a special pulse attack status
				currentSonarSquare.setResult(AttackStatus.SONAROCCUPIED);
				currentSonarSquare.setLocation(sonarSquares.get(i));

				attacks = getAttacks();
				this.attacks.add(currentSonarSquare);
				setAttacks(attacks);
			}
			else{
				char col = location.getColumn();
				int row = location.getRow();

				//if it is not occupied, make sure that it is still within boundaries
				if(!(row >= 11 || row <= 0 || col >= 'K' || col < 'A')){
					currentSonarSquare.setResult(AttackStatus.SONARNOTOCCUPIED);
					currentSonarSquare.setLocation(sonarSquares.get(i));
					attacks = getAttacks();
					this.attacks.add(currentSonarSquare);
					setAttacks(attacks);
				}

			}

			//Return the selected square for returning purposes
			if(i == 0){
				selectedSquare.setResult(currentSonarSquare.getResult());
				selectedSquare.setLocation(currentSonarSquare.getLocation());
			}
		}

		return selectedSquare;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y, boolean isSonarAttack) {
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

		if(isSonarAttack){
			attempt = sonarAttack(x, y);
			return attempt;
		}

		//If the test cases above passed, we can pass our coordinates into the array that holds the attacks
		//We can check if our attempt has hit a ship
		List<Result> previous_attacks = getAttacks();

		AttackStatus status = hasHitShip(x, y);
		if(status != AttackStatus.MISS){
			Ship hitShip = findHit(x,y);

			//Attach the hit ship to the attempt and set its location
			attempt.setShip(hitShip);
			attempt.setLocation(new Square(x,y));

			//Get the ship length and add one to the hit length
			int shipHitLength = hitShip.getHitLength();
			hitShip.setHitLength(shipHitLength + 1);

			//set the attempt and store it
			attempt.setResult(status);
			previous_attacks.add(attempt);
			setAttacks(previous_attacks);

			//After Every hit check if the game has ended
			if(checkGame()){
				attempt.setResult(AttackStatus.SURRENDER);

				previous_attacks.add(attempt);
				setAttacks(previous_attacks);
			}
		}
		//if hit missed
		else{
			//Return a miss and set this location to an event
			attempt.setResult(AttackStatus.MISS);
			attempt.setLocation(new Square(x, y));

			//Add attack to list of attacks
			previous_attacks.add(attempt);
			setAttacks(previous_attacks);
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
