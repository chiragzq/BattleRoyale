/**
 * Manages the state of the game and manage updates between previous game states.
 */
class Game {
    constructor() {
        this.players = [];
        this.updates = [];
    }

    update() {
        console.log(this.players)
        this.players.forEach((player) => {
            if(player && player.update()) {
                this.updates.push({
                    type: "player",
                    id: player.index,
                    x: player.x,
                    y: player.y,
                    dir: player.direction,
                    health: player.health
                })
            }
        });
    }

    getUpdates() {
        const ret = this.updates;
        this.updates = [];
        if(ret.length) {
            console.log(ret);
        }
        return ret;
    }
}

class Player {
    constructor(x, y, index) {
        this.x = x;
        this.y = y;
        this.index = index; 

        this.direction = 180;
        this.health = 100;

        this.moveU = false;
        this.moveL = false;
        this.moveD = false;
        this.moveR = false;

        this.speed = 10;
    }

    update() {
        let updated = false;
        if(this.moveU) {
            this.y -= this.speed;
            updated = true;
        }
        if(this.moveD) {
            this.y += this.speed;
            updated = true;
        }
        if(this.moveL) {
            this.x -= this.speed;
            updated = true;
        }
        if(this.moveR) {
            this.x += this.speed;
            updated = true;
        }
        return updated;
    }
}


module.exports.Game = Game;
module.exports.Player = Player;