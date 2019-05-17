const _weapon = require("./weapon");
const _obstacle = require("./obstacle");

const Rifle = _weapon.Rifle;
const Shotgun = _weapon.Shotgun;

const Rock = _obstacle.Rock;
const Bush = _obstacle.Bush;
const Tree = _obstacle.Tree;

/**
 * Manages the state of the game and manage updates between previous game states.
 */
class Game {
    constructor(io) {
        this.players = [];
        this.bullets = [];
        this.obstacles = generateRandomMap();

        this.updates = [];

        this.io = io;
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
            if(bullet.isOffScreen()) {
                this.updates.push({
                    type: "remove_bullet",
                    id: index
                });
                this.bullets[index] = null;
            } else if(this.obstacles.some((obstacle, index2) => {
                if(obstacle.solid && collisionCircleBullet(obstacle.x, obstacle.y, obstacle.size, bullet)) {
                    obstacle.hurt(bullet.getDamage());
                    this.updates.push({
                        type: "obstacle",
                        id: index2,
                        h: obstacle.health
                    });
                    return true;
                }
                return false;
            })) {
                this.updates.push({
                    type: "remove_bullet",
                    id: index
                });
                this.bullets[index] = null;
            } else if(this.players.some((player, index2) => {
                if(player && collisionCircleBullet(player.x, player.y, 25, bullet)) {
                    player.hurt(bullet.getDamage());
                    if(player.isDead()) {
                        console.log("DEAD\n\n\n\n")
                        this.io.emit("delete_player", index2); 
                        delete this.players[player.index];
                    }
                    return true;
                }
                return false;
            })) {
                this.updates.push({
                    type: "remove_bullet",
                    id: index
                });
                this.bullets[index] = null;
            } else {
                bullet.move();
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

        this.speed = 15;

        this.mouse = {
            x: 0,
            y: 0
        }

        this.weapons = [new Rifle(this), new Shotgun(this)];
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
            if(this.isOffScreen()) {
                this.x -= Math.round(this.speed * Math.cos(dir));
                this.y -= Math.round(this.speed * Math.sin(dir));
            }
            this.game.obstacles.some(obstacle => {
                if(obstacle.solid && collisonCircle(this.x, this.y, 25, obstacle.x, obstacle.y, obstacle.getSize())) {
                    [this.x, this.y] = fixCollidedObject(obstacle.x, obstacle.y, obstacle.getSize(), this.x, this.y, 25);
                }
            });
            updated = true;
        }

        const dx = this.mouse.x - 1280 / 2;
        const dy = this.mouse.y - 720 / 2;
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
            this.reload();
        }

        if(this.newEquip && !(this.newEquip == this.equippedWeapon || this.newEquip * this.equippedWeapon == -3) && !(this.newEquip > 0 && !this.weapons[this.newEquip - 1])) {
            this.equippedWeapon = this.newEquip;
            this.game.updates.push({
                type: "equip",
                id: this.index,
                index: this.equippedWeapon
            });
            this.socket.emit("reload", 0); //cancel reload
            this.lastReloadTime = 0;
        }
        this.newEquip = 0;

        return updated;
    }

    click() {
        if(this.isReloading()) {
            this.lastReloadTime = 0;
            this.socket.emit("reload", 0); //cancel reload
        }
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
        const weapon = this.weapons[this.equippedWeapon - 1];
        if(!weapon || this.isReloading() || !weapon.ammo || weapon.clipSize == weapon.magSize) return;
        this.lastReloadTime = Date.now();
        this.socket.emit("reload", this.weapons[this.equippedWeapon - 1].reloadTime);
    }

    hurt(damage) {
        if(this.isDead())return;
        this.health -= damage;
        this.health = Math.max(this.health, 0);
    }

    isDead() {
        return !this.health;
    }

    isOffScreen() {
        return this.x < 0 || 2000 < this.x || this.y < 0 || 2000 < this.y;
    }
}

function generateRandomMap() {
    const ret = [];
    let bushes = 25;
    let trees = 25;
    let rocks = 25;
    while(bushes--) {
        ret.push(new Bush(Math.round(Math.random() * 2000), Math.round(Math.random() * 2000)));
    }
    while(trees--) {
        ret.push(new Tree(Math.round(Math.random() * 2000), Math.round(Math.random() * 2000)));
    }
    while(rocks--) {
        ret.push(new Rock(Math.round(Math.random() * 2000), Math.round(Math.random() * 2000)));
    }
    return ret;
}

function collisonCircle(x1, y1, r1, x2, y2, r2) {
    return ((x2-x1) * (x2-x1) + (y1-y2) * (y1 - y2)) <= ((r1+r2) * (r1+r2));
}

function collisionCircleBullet(x1, y1, r1, bullet) {
    return collisionCirclePoint(x1, y1, r1, bullet.x, bullet.y) ||
    collisionCirclePoint(x1, y1, r1, bullet.backX, bullet.backY) ||
    collisionCirclePoint(x1, y1, r1, bullet.centerX, bullet.centerY);
}

function collisionCirclePoint(x1, y1, r1, x2, y2) {
    return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) <= r1 * r1;
}

function collisionCircleSquare(x1, y1, r1, x2, y2, size) {
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
