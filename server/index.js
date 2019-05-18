const socketIO = require('socket.io');
const gameLib = require("./game");
const http = require("http");

const Game = gameLib.Game;
const Player = gameLib.Player;


let game;

const server = http.createServer((req, res) => {
    if(req.url == "/restart") {
        game.players.forEach((player) => {
            player.socket.disconnect();
        });
        game.generateRandomMap();
        res.end("restarted");
    } else {
        res.end("ree");
    }
}).listen(process.env.PORT || 5000);

const io = socketIO(server);


game = new Game(io);

io.on('connection', (socket) => {
    console.log("New user connected");

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

    socket.on("disconnect", () => {
        io.emit("delete_player", player.index)
        delete game.players[player.index];
    });

});

setInterval(() => {
    game.update();
    io.emit("update", game.getUpdates())
}, 1000/30);

console.log("Started socket");