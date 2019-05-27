const socketIO = require('socket.io');
const gameLib = require("./game");
const urllib = require("url");
const http = require("http");

const Game = gameLib.Game;
const Player = gameLib.Player;


let game;
let sockets = [];
let accepting = false;
let players = 0;
let waitTime = 100000;
let startWaitTime = Date.now();
let waitTimeout = setTimeout(()=>{},0);
let io;

const server = http.createServer((req, res) => {
    const url = urllib.parse(req.url, true);
    if(url.pathname == "/start" && url.query["players"] && url.query["t"]) {
        players = Number(url.query.players);
        waitTime = Number(url.query.t);
        clearTimeout(waitTimeout);
        game.players.forEach((player) => {
            player.socket.disconnect();
        });
        game = new Game(io);
        
        sockets = [];
        accepting = true;
        startWaitTime = Date.now();

        waitTimeout = setTimeout(() => {
            accepting = false;
            sockets.forEach((socket) => initializeSocket(socket))
        }, waitTime * 1000);

    } else {
        res.setHeader("Content-Type", "text/html");
        res.end(`
<input id="a" type="number" value="5"/>Number of Players<br/>
<input id="b" type="number" value="30"/>Wait duration (seconds)<br/>
<button id="c">Start</button>
<script>
const xhr = new XMLHttpRequest();
document.getElementById("c").onclick=()=>{
    xhr.open("GET", "/start?players=" + document.getElementById("a").value + "&t=" + document.getElementById("b").value, true);
    xhr.send();
}
</script>
`);
    }
}).listen(process.env.PORT || 5000);

io = socketIO(server);


game = new Game(io);

io.use((socket, next) => {
    if(accepting && sockets.length < players && Date.now() - waitTime * 1000 < startWaitTime) {
        next();
    } else {
        accepting = false;
        next(new Error("You suck"));
    }
});

io.on('connection', (socket) => {
    console.log("New user connected");
    sockets.push(socket);
});

setInterval(() => {
    game.update();
    io.emit("update", game.getUpdates())
}, 1000/30);

function initializeSocket(socket) {
    const player = new Player(game, Math.round(Math.random() * 2000), Math.round(Math.random() * 2000), game.players.length, socket);

    socket.emit("player_info", {
        id: game.players.length,
        x: player.x,
        y: player.y,
        dir: player.direction,
        health: player.health
    });

    game.players.forEach((p) => {
        socket.emit("new_player", {
            id: p.index,
            x: p.x,
            y: p.y,
            dir: p.direction,
            health: p.health,
            equip: p.equippedWeapon
        });
    });

    game.obstacles.forEach((obstacle, index) => {
        socket.emit("new_obstacle", {
            id: index,
            x: obstacle.x,
            y: obstacle.y,
            type: obstacle.getType(),
            h: obstacle.health
        });
    });

    io.emit("new_player", {
        id: player.index,
        x: player.x,
        y: player.y,
        dir: player.direction,
        health: player.health
    });

    game.players.push(player);

    socket.on("w_pressed", (data) => {
        player.moveU = true;
    });

    socket.on("a_pressed", (data) => {
        player.moveL = true;
    });

    socket.on("s_pressed", (data) => {
        player.moveD = true;
    });

    socket.on("d_pressed", (data) => {
        player.moveR = true;
    });

    socket.on("w_released", (data ) => {
        player.moveU = false;
    });

    socket.on("a_released", (data) => {
        player.moveL = false;
    });

    socket.on("s_released", (data) => {
        player.moveD = false;
    });

    socket.on("d_released", (data) => {
        player.moveR = false;
    });

    socket.on("mouse_loc", (data) => {
        player.mouse = data;
    })

    socket.on("click", () => {
        player.click();
    })

    socket.on("1", () => {
        player.newEquip = 1;
    });

    socket.on("2", () => {
        player.newEquip = 2;
    });

    socket.on("3", () => {
        player.newEquip = -1;
    });

    socket.on("r", () => {
        player.reload();
    });

    socket.on("f", () => {
        player.pickUp();
    })

    socket.on("disconnect", () => {
        io.emit("delete_player", player.index)
        delete game.players[player.index];
    });
}

console.log("Started socket");