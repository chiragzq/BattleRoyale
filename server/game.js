const _weapon = require("./weapon");
const _obstacle = require("./obstacle");

const Rifle = _weapon.Rifle;
const Rock = _obstacle.Rock;
const Bush = _obstacle.Bush;
const Tree = _obstacle.Tree;

/**
 * Manages the state of the game and manage updates between previous game states.
 */
class Game {
    constructor() {
        this.players = [];
        this.bullets = [];
        this.obstacles = [new Rock(200, 400), new Rock(300, 100), new Rock(800, 400), new Bush(100, 100), new Bush(1000, 600), new Tree(500, 600), new Tree(100, 600)];
        this.obstacles.forEach((obstacle) => console.log(obstacle.getSize()));
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
    constructor(game, x, y, index, socket) {
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

        this.weapons = [new Rifle(this), new Rifle(this)];
        this.equippedWeapon = -1;

        this.lastPunchTime = 0;
        this.punchTime = 300;

        this.lastReloadTime = 0;

        this.socket = socket;
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
            this.game.obstacles.some(obstacle => {
                if(obstacle.solid && collisonCircle(this.x, this.y, 25, obstacle.x, obstacle.y, obstacle.getSize())) {
                    [this.x, this.y] = fixCollidedObject(obstacle.x, obstacle.y, obstacle.getSize(), this.x, this.y, 25);
                }
            });
            updated = true;
        }

        const dx = this.mouse.x - this.x;
        const dy = this.mouse.y - this.y;
        const direction = Math.round(Math.atan2(dy, dx) * 180 / Math.PI);
        if(direction != this.direction) {
            this.direction = direction;
            updated = true;
        }

        if(!this.isReloading() && this.lastReloadTime > 0) {
            this.lastReloadTime = 0;
            const reloadedGun = this.weapons[this.equippedWeapon - 1];
            reloadedGun.reload();
            this.socket.emit("ammo", {
                equip: this.equippedWeapon,
                clip: reloadedGun.clipSize,
                spare: reloadedGun.ammo
            });
        }

        if(!this.isReloading() && this.newEquip && !(this.newEquip == this.equippedWeapon || this.newEquip * this.equippedWeapon == -3) && !(this.newEquip > 0 && !this.weapons[this.newEquip - 1])) {
            this.equippedWeapon = this.newEquip;
            this.game.updates.push({
                type: "equip",
                id: this.index,
                index: this.equippedWeapon
            });
        }
        this.newEquip = 0;

        return updated;
    }

    click() {
        if(this.isReloading()) return;
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
            this.socket.emit("ammo", {
                equip: this.equippedWeapon,
                clip: this.weapons[this.equippedWeapon - 1].clipSize,
                spare: this.weapons[this.equippedWeapon - 1].ammo,
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

    isReloading() {
        if(!this.weapons[this.equippedWeapon - 1]) return false;
        return Date.now() - this.lastReloadTime < this.weapons[this.equippedWeapon - 1].reloadTime;
    }

    reload() {
        if(!this.weapons[this.equippedWeapon - 1] || this.isReloading() || !this.weapons[this.equippedWeapon - 1].ammo) return;
        this.lastReloadTime = Date.now();
        this.game.updates.push({
            type: "reload",
            t: this.weapons[this.equippedWeapon - 1].reloadTime,
        });
    }
}

function collisonCircle(x1, y1, r1, x2, y2, r2) {
    return ((x2-x1) * (x2-x1) + (y1-y2) * (y1 - y2)) <= ((r1+r2) * (r1+r2));
}

function collisionCirclePoint(x1, y1, r1, x2, y2) {
    return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) <= r1 * r1;
}

function collisionCircleSquaare(x1, y1, r1, x2, y2, size) {
    const distX = Math.abs(x1 - x2 - size / 2);
    const distY = Math.abs(y1 - y2 - size / 2);
    if (distX > (size / 2 + r1)) return false;
    if (distY > (size / 2 + r1)) return false;
    if (distX <= (size / 2)) return true;
    if (distY <= (size / 2)) return true;
    const dx = distX - size / 2;
    const dy = distY - size / 2;
    return (dx * dx + dy * dy <= (r1 * r1));
}

function fixCollidedObject(x1, y1, r1, x2, y2, r2) { //(x1, y1) is a static object
    const dir = Math.atan2(y2 - y1, x2 - x1);
    return [
        x1 + Math.round(Math.cos(dir) * (r1 + r2)),
        y1 + Math.round(Math.sin(dir) * (r1 + r2))
    ]
}


module.exports.Game = Game;
module.exports.Player = Player;
