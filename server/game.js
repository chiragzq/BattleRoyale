const _weapon = require("./weapon");

const Rifle = _weapon.Rifle;
const Bullet = _weapon.Bullet;

/**
 * Manages the state of the game and manage updates between previous game states.
 */
class Game {
    constructor() {
        this.players = [];
        this.bullets = [];
        
        this.updates = [];
    }

    update() {
        //console.log(this.players)
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
        this.bullets.forEach((bullet, index) => {
            if(!bullet) return;
            bullet.move();
            if(bullet.isOffScreen()) {
                this.updates.push({
                    type: "remove_bullet",
                    id: index
                });
                this.bullets[index] = null;
            } else {
                this.updates.push({
                    type: "bullet",
                    id: index,
                    x: bullet.x,
                    y: bullet.y
                })
            }
        });
    }

    getUpdates() {
        if(this.updates.length) {
            this.updates.push({
                type: "ping",
                t: Date.now(),
            });
        }
        const ret = this.updates;
        this.updates = [];
        if(ret.length) {
            console.log(ret);
        }
        return ret;
    }
}

class Player {
    constructor(game, x, y, index) {
        this.game = game;

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

        this.mouse = {
            x: 0,
            y: 0
        }

        this.weapons = [new Rifle(this)];
        this.equippedWeapon = -1;

        this.lastPunchTime = 0;
        this.punchTime = 300;
    }

    update() {
        let updated = false;
        
        let xd = 0;
        let yd = 0;
        yd -= this.speed * this.moveU;
        yd += this.speed * this.moveD;
        xd -= this.speed * this.moveL;
        xd += this.speed * this.moveR;
        const dir = Math.atan2(yd, xd);
        
        if(xd || yd) {
            this.x += Math.round(this.speed * Math.cos(dir));
            this.y += Math.round(this.speed * Math.sin(dir));
            updated = true;
        }

        const dx = this.mouse.x - this.x;
        const dy = this.mouse.y - this.y;
        const direction = Math.round(Math.atan2(dy, dx) * 180 / Math.PI);
        if(direction != this.direction) {
            this.direction = direction;
            updated = true;
        }

        if(this.newEquip && !(this.newEquip == this.equippedWeapon || this.newEquip * this.equippedWeapon == -3) && !(this.newEquip > 0 && !this.weapons[this.newEquip - 1])) {
            this.equippedWeapon = this.newEquip;
            this.game.updates.push({
                type: "equip",
                id: this.index,
                index: this.equippedWeapon
            });
            this.newEquip = 0;
        }

        return updated;
    }

    click() {
        if(this.weapons[this.equippedWeapon - 1]) { //fired a gun
            const bullets = this.weapons[this.equippedWeapon - 1].fire();
            bullets.forEach((bullet) => {
                this.game.updates.push({
                    type: "new_bullet",
                    id: this.game.bullets.length,
                    x: bullet.x,
                    y: bullet.y,
                    dir: bullet.direction
                });
                this.game.bullets.push(bullet);
            });
        } else { //using fists
            if(Date.now() - this.punchTime > this.lastPunchTime) {
                this.game.updates.push({
                    type: "punch",
                    id: this.index,
                });
                this.lastPunchTime = Date.now();
            }
        }
    }
}


module.exports.Game = Game;
module.exports.Player = Player;
