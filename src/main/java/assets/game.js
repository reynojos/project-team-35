var isSetup = true;

var placedShips = [];

var game;

var shipType;

var vertical;

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
        else if (attack.result === "HIT"){
            className = "hit";
            hits++;
        }
        else if (attack.result === "SUNK"){
            className = "hit"
            sunk++;
            hits++;
        }
        else if (attack.result === "SURRENDER")
            showModal(surrenderText);
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);

        printAction(elementId, attack);
    });

    if (elementId != "player"){
        playerScore.miss = miss;
        playerScore.hit = hits;
        playerScore.sunk = sunk
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

        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;
            redrawGrid();
            placedShips.push(shipType);
            if (placedShips.length == 3) {
                isSetup = false;
                registerCellListener((e) => {});
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
        sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
            game = data;
            redrawGrid();
        })
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

        let table = document.getElementById("player");

        out = false;
        overlap = false;

        for (let i=0; i<size; i++) {

            let cell;

            if(vertical) {

                let tableRow = table.rows[row+i];

                if (tableRow === undefined) {

                    // ship is over the edge; let the back end deal with it

                    out = true;

                    break;

                }

                cell = tableRow.cells[col];

            } else {

                cell = table.rows[row].cells[col+i];

            }

            if (cell === undefined) {

                // ship is over the edge; let the back end deal with it

                out = true;

                break;

            }

            if (cell.classList.contains("occupied")){
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
        }
        else if (type == "placement-ship"){
            text.innerText = "Select a new ship to place.";
            modal.classList.add("short");
        }
        else if (type == "guess-double"){
            text.innerText = "Guess in a location that you have not guessed before.";
            modal.classList.add("short");
        }
        else if (type == "guess-board"){
            text.innerText = "You must place your guess on the opponent board.";
            modal.classList.add("short");
        }
        else if (type == "bounds"){
            text.innerText = "You must place your ship within the bounds of your board.";
            modal.classList.add("short");
        }
        else if (type == "overlap"){
            text.innerText = "You can't overlap ships.";
            modal.classList.add("short");
        }
        else if (type == "won" || type == "lost") {
            text.innerText = "You "+type+"!";
            modal.classList.add("short");

            exit.removeEventListener("click", hideModal);
            exit.addEventListener("click", function(e){
                window.location.href = "/";
            });
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

function initGame() {

    makeGrid(document.getElementById("opponent"), false);

    makeGrid(document.getElementById("player"), true);

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

    sendXhr("GET", "/game", {}, function(data) {

        game = data;

    });

};