const _weapon = require("./weapon");
const _obstacle = require("./obstacle");
const _item = require("./item");

const Rifle = _weapon.Rifle;
const Shotgun = _weapon.Shotgun;

const Rock = _obstacle.Rock;
const Bush = _obstacle.Bush;
const Tree = _obstacle.Tree;
const Box = _obstacle.Box;
const Barrel = _obstacle.Barrel;

const DroppedRifle = _item.DroppedRifle;
const DroppedShotgun = _item.DroppedShotgun;
const Bandage = _item.Bandage;

/**
 * Manages the state of the game and manage updates between previous game states.
 */
class Game {
    constructor(io) {
        this.players = [];
        this.bullets = [];
        this.obstacles = generateRandomMap();
        
        this.items = [];

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
            if(bullet.isDead()) {
                this.updates.push({
                    type: "remove_bullet",
                    id: index
                });
                this.bullets[index] = null;
            } else if(this.obstacles.some((obstacle, index2) => {
                const solid = obstacle.solid;
                if(!obstacle.isDead() && collisionCircleBullet(obstacle.x, obstacle.y, obstacle.size, bullet)) {
                    obstacle.hurt(bullet.getDamage());
                    if(obstacle.isDead() && obstacle instanceof Barrel) {
                        const project = obstacle.spawnBullets(128);
                        project.forEach((bullet) => {
                            this.updates.push({
                                type: "new_bullet",
                                id: this.bullets.length,
                                x: bullet.x,
                                y: bullet.y,
                                dir: bullet.direction
                            });
                            this.bullets.push(bullet);
                        });
                    }
                    this.updates.push({
                        type: "obstacle",
                        id: index2,
                        h: obstacle.health
                    });
                    return solid;
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
                    this.updates.push({
                        type: "player",
                        id: player.index,
                        x: player.x,
                        y: player.y,
                        dir: player.direction,
                        health: player.health
                    });
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

    generateRandomMap() {
        this.obstacles = generateRandomMap();
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

        this.speed = 13;

        this.mouse = {
            x: 0,
            y: 0
        }

        this.weapons = [new Rifle(this), new Shotgun(this)];
        this.equippedWeapon = -1;

        this.lastPunchTime = 0;
        this.punchTime = 350;

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
            this.fixOffScreen();
            
            let max = 1000;
            while(max-- && this.game.obstacles.some(obstacle => {
                if(obstacle instanceof Box) {
                    if(obstacle.solid && collisionCircleSquare(this.x, this.y, 25, obstacle.x, obstacle.y, obstacle.getSize())) {
                        [this.x, this.y] = fixCollidedObjectSquare(obstacle.x, obstacle.y, obstacle.getSize(), this.x, this.y, 25);
                        console.log("collision");
                        return true;
                    }
                } else if(obstacle.solid && collisionCircle(this.x, this.y, 25, obstacle.x, obstacle.y, obstacle.getSize())) {
                    [this.x, this.y] = fixCollidedObject(obstacle.x, obstacle.y, obstacle.getSize(), this.x, this.y, 25);
                    console.log("collision");

                    return true;
                }
                
                return false;
            })){}
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

        if(!this.didPunchCollision && Date.now() - this.punchTime / 2 > this.lastPunchTime) {
            const handRadius = 9;
            const handOffset = Math.PI / 30;
            const handExtend = 20;

            let hands = [{
                x: this.x + (25 + handExtend) * Math.cos(this.direction * Math.PI / 180 + handOffset),
                y: this.y + (25 + handExtend) * Math.sin(this.direction * Math.PI / 180 + handOffset)
            }, {
                x: this.x + (25 + handExtend) * Math.cos(this.direction * Math.PI / 180 - handOffset),
                y: this.y + (25 + handExtend) * Math.sin(this.direction * Math.PI / 180 - handOffset)
            }]

            let collided = false;
            hands.forEach((hand) => {
                if(collided) return;
                this.game.obstacles.some((obstacle, index) => {
                    if(!obstacle.isDead() && collisionCircle(hand.x, hand.y, handRadius, obstacle.x, obstacle.y, obstacle.getSize())) {
                        obstacle.hurt(18);
                        if(obstacle.isDead() && obstacle instanceof Barrel) {
                            const project = obstacle.spawnBullets(12);
                            project.forEach((bullet) => {
                                this.game.updates.push({
                                    type: "new_bullet",
                                    id: this.game.bullets.length,
                                    x: bullet.x,
                                    y: bullet.y,
                                    dir: bullet.direction
                                }); 
                                this.game.bullets.push(bullet);
                            });
                        }
                        this.game.updates.push({
                            type: "obstacle",
                            id: index,
                            h: obstacle.health
                        });
                        return collided = true;
                    }
                });
                if(collided) return;
                this.game.players.some((player) => {
                    if(collisionCircle(hand.x, hand.y, handRadius, player.x, player.y, 25)) {
                        player.hurt(18);
                        this.game.updates.push({
                            type: "player",
                            id: player.index,
                            x: player.x,
                            y: player.y,
                            dir: player.direction,
                            health: player.health
                        });
                        if(player.isDead()) {
                            this.game.io.emit("delete_player", player.index); 
                            delete this.game.players[player.index];
                        }
                        return collided = true;
                    }
                });
            });
            this.didPunchCollision = true;
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
            this.lastPunchTime = 0;
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
                this.didPunchCollision = false;
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

    fixOffScreen() {
        this.x = Math.max(this.x, 0);
        this.y = Math.max(this.y, 0);
        this.x = Math.min(this.x, 4000);
        this.y = Math.min(this.y, 4000);
    }
}

function generateRandomMap() {
    const ret = [];
    let bushes = 50;
    let trees = 50;
    let rocks = 50;
    let boxes = 20;
    let barrels = 20;
    while(bushes--) {
        ret.push(new Bush(Math.round(Math.random() * 4000), Math.round(Math.random() * 4000)));
    }
    while(trees--) {
        ret.push(new Tree(Math.round(Math.random() * 4000), Math.round(Math.random() * 4000)));
    }
    while(rocks--) {
        ret.push(new Rock(Math.round(Math.random() * 4000), Math.round(Math.random() * 4000)));
    }
    while(boxes--) {
        ret.push(new Box(Math.round(Math.random() * 4000), Math.round(Math.random() * 4000)));
    }
    while(barrels--) {
        ret.push(new Barrel(Math.round(Math.random() * 2000), Math.round(Math.random() * 2000)));
    }
    return ret;
}

function collisionCircle(x1, y1, r1, x2, y2, r2) {
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
    const nearX = Math.max(x2 - size / 2, Math.min(x1, x2 + size / 2));
    const nearY = Math.max(y2 - size / 2, Math.min(y1, y2 + size / 2));
    const dx = x1 - nearX;
    const dy = y1 - nearY;
    return (dx * dx + dy * dy <= (r1 * r1));
}

function fixCollidedObject(x1, y1, r1, x2, y2, r2) { //(x1, y1) is a static circle
    const dir = Math.atan2(y2 - y1, x2 - x1);
    return [
        Math.round(x1 + Math.round(Math.cos(dir) * (r1 + r2)) * 1.02),
        Math.round(y1 + Math.round(Math.sin(dir) * (r1 + r2)) * 1.02)
    ]
}

function fixCollidedObjectSquare(x1, y1, size, x2, y2, r2) { //(x1, y1) is a static square
    const nearX = Math.max(x1 - size / 2, Math.min(x2, x1 + size / 2));
    const nearY = Math.max(y1 - size / 2, Math.min(y2, y1 + size / 2));

    let xOff = nearX - x1;
    let yOff = nearY - y1;
    if(Math.abs(xOff) > Math.abs(yOff)) {
        xOff = (size / 2 + r2) * Math.sign(xOff);
    } else {
        yOff = (size / 2 + r2) * Math.sign(yOff)
    }
    return [Math.round(x1 + xOff * 1.02), Math.round(y1 + yOff * 1.02)];
}


module.exports.Game = Game;
module.exports.Player = Player;
