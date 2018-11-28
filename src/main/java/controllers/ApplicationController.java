package controllers;

import com.google.inject.Singleton;
import cs361.battleships.models.*;
import ninja.Context;
import ninja.Result;
import ninja.Results;

@Singleton
public class ApplicationController {

    public Result index() {
        return Results.html();
    }

    public Result newGame() {
        Game g = new Game();
        return Results.json().render(g);
    }

    public Result placeShip(Context context, PlacementGameAction g) {
        Game game = g.getGame();
        String shipType = g.getShipType();

        Ship ship;

        //Construct ship based off type
        if(shipType.equals("MINESWEEPER")){
            ship = new Minesweeper();
        }
        else if(shipType.equals("BATTLESHIP")){
            ship = new Battleship();
        }
        else if(shipType.equals("DESTROYER")){
            ship = new Destroyer();
        }
        else {
            ship = new Submarine();
        }

        boolean result = game.placeShip(ship, g.getActionRow(), g.getActionColumn(), g.isVertical(), g.isSubmerged());
        if (result) {
            return Results.json().render(game);
        } else {
            return Results.badRequest();
        }
    }

    public Result attack(Context context, AttackGameAction g) {
        Game game = g.getGame();
        boolean result = game.attack(g.getActionRow(), g.getActionColumn(), g.getisSonarAttack());
        if (result) {
            return Results.json().render(game);
        } else {
            return Results.badRequest();
        }
    }
}
