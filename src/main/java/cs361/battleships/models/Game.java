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
        //separate player ship from opponent ship
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical);
        if (!successful)
            return false;

        Ship opponentShip = new Ship(ship.getType());

        boolean opponentPlacedSuccessfully;
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(opponentShip, randRow(), randCol(), randVertical());
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
        } while(opponentAttackResult.getResult() != INVALID);

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

    public boolean randVertical() {
        //This will return true or false depending if the random
        //integer from 0-1 is 0
        Random r = new Random();
        return (r.nextInt(2) != 0);
    }
}
