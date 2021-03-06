package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static cs361.battleships.models.AttackStatus.*;

public class Game {

    @JsonProperty private Board playersBoard;
    @JsonProperty private Board opponentsBoard;

    private String alphabet = "ABCDEFGHIJ";

    public Game(){
        playersBoard = new Board();
        opponentsBoard = new Board();
    }

    public String getAlphabet(){
        return alphabet;
    }

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {

        // Error Handling: Checking bounds of x and y
        if(x < 1 || x > 10 || y < 'A' || y > 'J')
            return false;

        //separate player ship from opponent ship
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical);
        if (!successful)
            return false;

        Ship opponentShip;

        if(ship.getType().equals("MINESWEEPER")){
            opponentShip = new Minesweeper();
        }
        else if(ship.getType().equals("DESTROYER")){
            opponentShip = new Destroyer();
        }
        else if(ship.getType().equals("BATTLESHIP")){
            opponentShip = new Battleship();
        }
        else {
            opponentShip = new Submarine();
        }

        boolean opponentPlacedSuccessfully;
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(opponentShip, randRow(), randCol(), randBool());
        } while (!opponentPlacedSuccessfully);

        return true;
    }

    /*
    Overload function to handle different ship statuses from front end
     */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical, boolean isSubmerged) {

        // Error Handling: Checking bounds of x and y
        if(x < 1 || x > 10 || y < 'A' || y > 'J')
            return false;

        //separate player ship from opponent ship
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical, isSubmerged);
        if (!successful)
            return false;

        Ship opponentShip;

        if(ship.getType().equals("MINESWEEPER")){
            opponentShip = new Minesweeper();
        }
        else if(ship.getType().equals("DESTROYER")){
            opponentShip = new Destroyer();
        }
        else if(ship.getType().equals("BATTLESHIP")){
            opponentShip = new Battleship();
        }
        else {
            opponentShip = new Submarine();
        }

        boolean opponentPlacedSuccessfully;
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(opponentShip, randRow(), randCol(), randBool(), randBool());
        } while (!opponentPlacedSuccessfully);

        return true;
    }

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean attack(int x, char  y) {
        Result playerAttack = opponentsBoard.attack(x, y);
        if (playerAttack.getResult() == INVALID) {
            return false;
        }

        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == INVALID);

        return true;
    }

    /*
	Overloaded attack so we can access attack types
	 */
    public boolean attack(int x, char  y, boolean isSonarAttack) {
        Result playerAttack;
        if (isSonarAttack) {
            playerAttack = opponentsBoard.sonarAttack(x, y);
        }
        else{
            playerAttack = opponentsBoard.attack(x, y);
        }
        if (playerAttack.getResult() == INVALID) {
            return false;
        }

        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == INVALID);

        return true;
    }

    public char randCol() {
        //This will select a random character between A - J
        Random r = new Random();

        //Declare a string that contains possible choices
        //From this string, randomly select an index that will be returned
        String alphabet = "ABCDEFGHIJ";
        return this.alphabet.charAt(r.nextInt(alphabet.length()));
    }

    public int randRow() {
        //This will return a random number between 1 - 10
        Random r = new Random();
        return (r.nextInt(10) + 1);
    }

    public boolean randBool() {
        //This will return true or false depending if the random
        //integer from 0-1 is 0
        Random r = new Random();
        return (r.nextInt(2) != 0);
    }
}
