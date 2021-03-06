var isSetup = true;
var isSonar = false;
var placedShips = [];
var game;
var shipType;
var vertical;
var submerged;
var sonars = 0;
var sonarsUsed = 0;

let out;

let overlap;

let playerScore = {
    hit:0,
    miss:0,
    sunk:0,
};

let enemyScore = {
    hit:0,
    miss:0,
    sunk:0,
};

function makeGrid(table, isPlayer) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            let square = document.createElement('div');
            square.classList.add("square");
            column.appendChild(square);
            column.addEventListener("click", cellClick);
            row.appendChild(column);

        }
        table.appendChild(row);

    }

}

function printAction(player, attack){
    var row = attack.location['row'];
    var column = attack.location['column'];
    var status = attack.result;

    status = status.toLowerCase();

    if (status == "captainhit"){
        status = "captain hit";
    }

    var log, text;

    /*Player is equal to the board that is being attacked. SO if opponent, the player is attacking the opponent ship*/
    if(player == "opponent"){
        log_container = document.getElementById("player-log");
        log = document.getElementById("player-status");
        log.innerHTML = "";

        text = document.createElement("p");
        text.appendChild(document.createTextNode('You attacked ' + column + ' ' + row + ' and it resulted in a ' + status + '.'));
        log.appendChild(text);

        log_container.scrollTop = log.scrollHeight;
    }

    else if(player == "player"){
        log_container = document.getElementById("opponent-log");
        log = document.getElementById("opponent-status");
        log.innerHTML = "";

        text = document.createElement("p");
        text.appendChild(document.createTextNode('Your opponent has attacked ' + column + ' ' + row + ' and it resulted in a ' + status + '.'));
        log.appendChild(text);

        log_container.scrollTop = log.scrollHeight;
    }
}

function updateScore(){
    document.getElementById("player1-hits").innerText = playerScore.hit;
    document.getElementById("player1-miss").innerText = playerScore.miss;
    document.getElementById("player1-sunk").innerText = playerScore.sunk;
    document.getElementById("player2-hits").innerText = enemyScore.hit;
    document.getElementById("player2-miss").innerText = enemyScore.miss;
    document.getElementById("player2-sunk").innerText = enemyScore.sunk;
}

function crossOutShip(attack, player){
    var shipSunk = attack.ship['type'];
    /*After a sunk is detected, find the ship and strike out the name of the ship sunk*/
    if(player == "player"){
        if(shipSunk == "MINESWEEPER"){
            var b = document.getElementById("place_minesweeper");
            b.innerHTML = "<strike>Minesweeper</strike>";
        }
        else if(shipSunk == "Battleship"){
            var b = document.getElementById("place_battleship");
            b.innerHTML = "<strike>Battleship</strike>";
        }

        else if(shipSunk == "DESTROYER"){
            var b = document.getElementById("place_destroyer");
            b.innerHTML = "<strike>Destroyer</strike>";
        }
        else if(shipSunk == "SUBMARINE"){
            var b = document.getElementById("place_submarine");
            b.innerHTML = "<strike>Submarine</strike>";
        }
    }

    if(player == "opponent"){
        if(shipSunk == "MINESWEEPER"){
            var b = document.getElementById("op_minesweeper");
            b.innerHTML = "<strike>Minesweeper</strike>";
        }
        else if(shipSunk == "BATTLESHIP"){
            var b = document.getElementById("op_battleship");
            b.innerHTML = "<strike>Battleship</strike>";
        }

        else if(shipSunk == "DESTROYER"){
            var b = document.getElementById("op_destroyer");
            b.innerHTML = "<strike>Destroyer</strike>";
        }
        else if(shipSunk == "SUBMARINE"){
            var b = document.getElementById("op_submarine");
            b.innerHTML = "<strike>Submarine</strike>";
        }
    }
}

function markHits(board, elementId, surrenderText) {

    hits = 0;
    miss = 0;
    sunk = 0;

    board.attacks.forEach((attack) => {

        let className;

        if (attack.result === "MISS"){
            className = "miss";
            miss++;
        }
        else if (attack.result === "CAPTAINHIT"){
            className = "cpt";
            hits++;
        }
        else if (attack.result === "HIT"){
            className = "hit";
            hits++;
        }
        else if (attack.result === "SUNK"){
            className = "hit"
            sunk++;
            hits++;
            crossOutShip(attack, elementId);
        }

        else if(attack.result == "SONAROCCUPIED"){
            className = "occupied";
        }

        else if(attack.result == "SONARNOTOCCUPIED"){
            className = "not-occupied";
        }

        else if (attack.result === "SURRENDER")
            showModal(surrenderText);

        var a = document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)];

        if(!(a.classList.contains("miss") || a.classList.contains("hit")))
            a.classList.add(className);

        if(a.classList.contains("occupied") || a.classList.contains("not-occupied"))
            a.className = className;

        printAction(elementId, attack);


    });

    if (elementId != "player"){
        playerScore.miss = miss;
        playerScore.hit = hits;
        playerScore.sunk = sunk;

        sonars = playerScore.sunk - sonarsUsed;
        var a = document.getElementById("sonars-available");
        a.innerText = sonars;

        if(sonars > 0)
            document.getElementById("sonar-button").disabled = false;
    }
    else{
        enemyScore.miss = miss;
        enemyScore.hit = hits;
        enemyScore.sunk = sunk;
    }
}



function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);

    if (game === undefined) {
        return;
    }



    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
    }));

    markHits(game.opponentsBoard, "opponent", "won");
    markHits(game.playersBoard, "player", "lost");
    updateScore();

}

var oldListener;

function registerCellListener(f) {

    let el = document.getElementById("player");

    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }

    oldListener = f;
}

function removeLink(shipType){
    /*Once ship has been placed, remove link and replace string inside based off type*/
    if(shipType == "MINESWEEPER"){
        var a = document.getElementById("place_minesweeper");
        a.innerText = "Minesweeper";
        a.removeAttribute("href");
    }
    else if (shipType == "BATTLESHIP"){
        var a = document.getElementById("place_battleship");
        a.innerText = "Battleship";
        a.removeAttribute("href");
    }
    else if (shipType == "DESTROYER"){
        var a = document.getElementById("place_destroyer");
        a.innerText = "Destroyer";
        a.removeAttribute("href");
    }
    else if (shipType == "SUBMARINE"){
        var a = document.getElementById("place_submarine");
        a.innerText = "Submarine";
        a.removeAttribute("href");
    }
}

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);

    if (isSetup) {
        if (shipType == undefined || placedShips.includes(shipType))
        {
            showModal("placement-ship");
            return;
        }
        else if (overlap){
            showModal("overlap");
            return;
        }
        else if (!this.classList.contains("placed"))
        {
            showModal("placement-board");
            return;
        }
        else if (out){
            showModal("bounds");
            return;
        }

        // Always hide submerged checkbox unless submarine is clicked
        document.getElementById("submerged-checkbox").classList.add("hideElement");

        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical, isSubmerged: submerged}, function(data) {
            game = data;
            redrawGrid();
            placedShips.push(shipType);
            removeLink(shipType);
            if (placedShips.length == 4) {
                isSetup = false;
                registerCellListener((e) => {});
                document.getElementById("vertical-checkbox").classList.add("hideElement");
                document.getElementById("sonar-button-container").classList.remove("hideElement");
                document.getElementById('player').classList.remove("clickable");
                document.getElementById('opponent').classList.add("clickable");
            }
            shipType = undefined;
        });

    } else {
        if (!this.parentElement.parentElement.classList.contains("clickable")){
            showModal("guess-board");
            return;
        }
        else if (this.classList.contains("miss") || this.classList.contains("hit")){
            showModal("guess-double");
            return;
        }
        //considering the tests above past, remove one sonar

        sendXhr("POST", "/attack", {game: game, x: row, y: col, isSonarAttack: isSonar}, function(data) {
            game = data;
            redrawGrid();
            //considering the tests have passed, remove a pulse
            if(isSonar){
                isSonar = false;
                sonars = sonars - 1;
                sonarsUsed = sonarsUsed + 1;

                var a = document.getElementById("sonars-available");
                a.innerText = sonars;

                if(sonars == 0){
                     document.getElementById("sonar-button").disabled = true;
                }
            }
        });



    }

}



function sendXhr(method, url, data, handler) {

    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            alert("Cannot complete the action");
            return;

        }
        handler(JSON.parse(req.responseText));
    });

    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}



function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        vertical = document.getElementById("is_vertical").checked;
        submerged = document.getElementById("is_submerged").checked;
        let table = document.getElementById("player");

        out = false;
        overlap = false;

        for (let i=0; i<size; i++) {
            let cell;
            /*After ship selection, check if the object is orientated to be veritcal or not*/
            if(vertical) {
                let tableRow = table.rows[row+i];

                // For first 4 squares (submarine is only one > 4)
                if(i < 4) {
                    if (tableRow === undefined) {
                        // ship is over the edge; let the back end deal with it
                        out = true;
                        break;
                    }
                    else
                        cell = tableRow.cells[col];
                }
                // For submarine off square
                else {
                        cell = table.rows[row + i - 2].cells[col + 1];
                }
            }
            else {
                if(i < 4) {
                    cell = table.rows[row].cells[col+i];
                }
                else {
                    cell = table.rows[row - 1].cells[col + i - 2];
                }
            }

            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                out = true;
                break;
            }

            // If cell is occupied and not submerged
            if (cell.classList.contains("occupied") && !submerged){
                overlap = true;
            }

            cell.classList.toggle("placed");
        }
    }

}

function hideModal(){
    document.getElementById('modal').innerHTML = "";
    let backdrop = document.getElementById('modal-back');
    backdrop.style.display = "none";
}

function showModal(type) {
    let backdrop = document.getElementById('modal-back');
    backdrop.style.display = "block";

    let exit = document.createElement('button');
    exit.classList.add("exit-button");
    exit.innerHTML = "OK";

    exit.addEventListener("click", hideModal);

    let modal = document.getElementById('modal');

    if(type != undefined){
        let text = document.createElement("p");
        text.classList.add("modal-text");

        if (type == "placement-board"){
            text.innerText = "You must place a ship on your own board. Try again.";
            modal.classList.add("short");
            modal.classList.remove("long");
        }
        else if (type == "placement-ship"){
            text.innerText = "Select a new ship to place.";
            modal.classList.add("short");
            modal.classList.remove("long");
        }
        else if (type == "guess-double"){
            text.innerText = "Guess in a location that you have not guessed before.";
            modal.classList.add("short");
            modal.classList.remove("long");
        }
        else if (type == "guess-board"){
            text.innerText = "You must place your guess on the opponent board.";
            modal.classList.add("short");
            modal.classList.remove("long");
        }
        else if (type == "bounds"){
            text.innerText = "You must place your ship within the bounds of your board.";
            modal.classList.add("short");
            modal.classList.remove("long");
        }
        else if (type == "overlap"){
            text.innerText = "You can't overlap ships.";
            modal.classList.add("short");
            modal.classList.remove("long");
        }
        else if (type == "won" || type == "lost") {
            text.innerText = "You "+type+"!";
            modal.classList.add("short");
            modal.classList.remove("long");

            exit.removeEventListener("click", hideModal);
            exit.addEventListener("click", function(e){
                window.location.href = "/";
            });
        }
        else if (type == "help"){

                    text.innerText = "Welcome to our battleship game!" +
                    "\n\nTo play, start by selecting a ship to place and click a spot on player1's board." +
                    "\nKeep picking until you have place all 3 ships." +
                    "\n\nNow it's time to attack!" +
                    "\nClick a spot on player2's board to place a guess."+
                    "\nKeep guessing until either player wins."+
                    "\n\nCaptain's Quarters" +
                    "\nEach ship contains a Captain's quarters"+
                    "\nWhen hit, the entire ship is sunk, regardless of the status of its other elements."+
                    "\nThe Captains quarters for battleships and destroyers are armored,"+
                    "\nwhich means it takes two attacks on the same square to count as a hit."+
                    "\n\nSonar Pulse"+
                    "\nThe sonar pulse allows you to reveal enemy ships on a portion of the map"+
                    "\nThe sonar pulse shows free spaces as grey squares and enemy ships as black squares"+
                    "\nThe sonar pulse is available only after you have sunken a ship"+
                    "\nYou are allowed a total of two sonar pulse's per game"+
                    "\n\nIf you would like to play again, click ok on the end of game popup.";
                    modal.classList.add("long");
                    modal.classList.remove("short");
                }
        else {
            text.innerText = type;
            modal.classList.add("short");
        }
        if (document.getElementsByClassName("modal-text").length == 0)
            modal.appendChild(text);
    }

    if (document.getElementsByClassName("exit-button").length == 0)
        modal.appendChild(exit);
}

function setSonarAttack(){
    isSonar = true;
}

function initGame() {
    /*Initialize the player and opponent boards*/
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);

    /*Add click event listeners to modal, and ship placers*/
    document.getElementById("help").addEventListener("click", function(e){
        showModal("help");
    });

    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
        registerCellListener(place(2));
    });

    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
        registerCellListener(place(3));
    });

    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
        registerCellListener(place(4));
    });

    document.getElementById("place_submarine").addEventListener("click", function(e) {
        shipType = "SUBMARINE";
        registerCellListener(place(5));
    });

    document.getElementById("sonar-button").addEventListener("click", function(e){
        setSonarAttack();
    });

    /*Send a request to our backend*/
    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};
