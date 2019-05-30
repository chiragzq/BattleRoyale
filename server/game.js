const _weapon = require("./weapon");
const _obstacle = require("./obstacle");
const _item = require("./item");

const Rifle = _weapon.Rifle;
const Shotgun = _weapon.Shotgun;
const Sniper = _weapon.Sniper;
const Pistol = _weapon.Pistol;

const Rock = _obstacle.Rock;
const Bush = _obstacle.Bush;
const Tree = _obstacle.Tree;
const Box = _obstacle.Box;
const Barrel = _obstacle.Barrel;

const DroppedRifle = _item.DroppedRifle;
const DroppedShotgun = _item.DroppedShotgun;
const Ammo = _item.Ammo;
const BlueAmmo = _item.BlueAmmo;
const RedAmmo = _item.RedAmmo;
const DroppedSniper = _item.DroppedSniper;
const DroppedPistol = _item.DroppedPistol;
const DroppedGun = _item.DroppedGun;
const Bandage = _item.Bandage;
const Medkit = _item.Medkit;
const Armor = _item.Medkit;
const ChestPlateOne = _item.ChestPlateOne;
const ChestPlateTwo = _item.ChestPlateTwo;
const ChestPlateThree = _item.ChestPlateThree;
const HelmetOne = _item.HelmetOne;
const HelmetTwo = _item.HelmetTwo;
const HelmetThree = _item.HelmetThree;
const Scope2 = _item.Scope2;
const Scope4 = _item.Scope4;
const Scope8 = _item.Scope8;
const Scope15 = _item.Scope15;

/**
 * Manages the state of the game and manage updates between previous game states.
 */
class Game {
    constructor(io) {
        this.players = [];
        this.bullets = [];
        this.items = [];
        this.obstacles = [];
        generateRandomMap(this);
        

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
                    health: player.health,
                    totalHealth: player.totalHealth
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
                if(!obstacle.isDead() && (obstacle instanceof Box ? collisionBulletBox(obstacle.x, obstacle.y, obstacle.getSize(), bullet) : collisionCircleBullet(obstacle.x, obstacle.y, obstacle.getSize(), bullet))) {
                    obstacle.hurt(bullet.getDamage());
                    if(obstacle.isDead() && obstacle instanceof Barrel) {
                        const project = obstacle.spawnBullets(128);
                        project.forEach((bullet) => {
                            this.updates.push({
                                type: "new_bullet",
                                id: this.bullets.length,
                                x: bullet.x,
                                y: bullet.y,
                                thickness: 3,
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
                        health: player.health,
                        totalHealth: player.totalHealth
                    });
                    if(player.isDead()) {
                        console.log("DEAD\n\n\n\n")
                        killPlayer(player, this);
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
        this.items.forEach((item, index3) => {
            if(item != null){
                if(item.move()) {
                    this.updates.push({
                        type: "item",
                        id: index3,
                        x: item.x,
                        y: item.y
                    })
                }
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
        this.totalHealth = 100;

        this.helmet = 0;
        this.chestplate = 0;
        this.redAmmo = 0;
        this.blueAmmo = 0;

        this.moveU = false;
        this.moveL = false;
        this.moveD = false;
        this.moveR = false;
        this.currentScope = 1;


        this.bandages = 0;
        this.medkits = 0;

        this.speed = 13;

        this.mouse = {
            x: 0,
            y: 0
        }

        this.weapons = [];
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
                blue: this.blueAmmo,
                red: this.redAmmo
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
                    if(!obstacle) return;
                    if(!obstacle.isDead() && collisionCircle(hand.x, hand.y, handRadius, obstacle.x, obstacle.y, obstacle.getSize())) {
                        obstacle.hurt(18);
                        if(obstacle.isDead() && obstacle instanceof Box) {
                            let moveAngle = Math.random() * 360;
                            let dropItem = getRandomItem(obstacle.x, obstacle.y, moveAngle);
                            
                            if(dropItem instanceof DroppedGun) {
                                
                                var droppedAmmo = new BlueAmmo(obstacle.x, obstacle.y, moveAngle + Math.random() * 360);;
                                if(dropItem.color === "red")
                                    droppedAmmo = new RedAmmo(obstacle.x, obstacle.y, moveAngle + Math.random() * 360);
                                this.game.io.emit("new_dropped_item", {
                                    type: droppedAmmo.type,
                                    x: obstacle.x,
                                    y: obstacle.y,
                                    id: this.game.items.length,
                                    color: droppedAmmo.color
                                });
                                this.game.items.push(droppedAmmo);

                                let dropAmmo = new BlueAmmo(obstacle.x, obstacle.y, moveAngle + Math.random() * 360);;
                                if(dropItem.color === "red")
                                    dropAmmo = new RedAmmo(obstacle.x, obstacle.y, moveAngle + Math.random() * 360);

                                this.game.io.emit("new_dropped_item", {
                                    type: dropAmmo.type,
                                    x: obstacle.x,
                                    y: obstacle.y,
                                    id: this.game.items.length,
                                    color: dropAmmo.color
                                });
                                this.game.items.push(dropAmmo);
                            }
                            console.log(dropItem);
                            this.game.io.emit("new_dropped_item", {
                                type: dropItem.type,
                                x: obstacle.x,
                                y: obstacle.y,
                                id: this.game.items.length
                            });
                            this.game.items.push(dropItem);
                        }
                        if(obstacle.isDead() && obstacle instanceof Barrel) {
                            const project = obstacle.spawnBullets(128);
                            project.forEach((bullet) => {
                                this.game.updates.push({
                                    type: "new_bullet",
                                    id: this.game.bullets.length,
                                    x: bullet.x,
                                    y: bullet.y,
                                    thickness: 3,
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
                            health: player.health,
                            totalHealth: player.totalHealth
                        });
                        if(player.isDead()) {
                            killPlayer(player, this.game);
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

    pickUp() {
        this.game.items.some((item, index) => {
            if(item != null && item.collision(this.x, this.y, 25)) {
                if(item.type.indexOf("Ammo") != -1) {
                    if(item.color == "blue") {
                    this.blueAmmo += 60;
                    if( this.weapons[this.equippedWeapon - 1]!= null &&  this.weapons[this.equippedWeapon - 1].color == "blue") {
                        this.weapons[this.equippedWeapon - 1].ammo = this.blueAmmo;
                        this.socket.emit("ammo", {
                            equip: this.equippedWeapon,
                            clip: this.weapons[this.equippedWeapon - 1].clipSize,
                            spare: this.weapons[this.equippedWeapon - 1].ammo,
                            blue: this.blueAmmo,
                            red: this.redAmmo
                        });
                    }
                    else {
                        this.socket.emit("player_updates", {
                            type: "blueAmmo",
                            num: this.blueAmmo
                        });
                    }
                    }
                    else {
                        this.redAmmo += 60;
                    if( this.weapons[this.equippedWeapon - 1]!= null &&  this.weapons[this.equippedWeapon - 1] == "red") {
                        this.weapons[this.equippedWeapon - 1].ammo = this.redAmmo;
                        this.socket.emit("ammo", {
                            equip: this.equippedWeapon,
                            clip: this.weapons[this.equippedWeapon - 1].clipSize,
                            spare: this.weapons[this.equippedWeapon - 1].ammo,
                            blue: this.blueAmmo,
                            red: this.redAmmo
                        });
                    }
                    else {
                        this.socket.emit("player_updates", {
                            type: "redAmmo",
                            num: this.redAmmo
                        })
                    }
                    }
                    this.game.updates.push({
                        type: "remove_item",
                        id: index
                    });
                    this.game.items[index] = null;
                    return true;
                }
                else if(item.type == "medkit" || item.type == "bandage") {
                    this.game.updates.push({
                        type: "remove_item",
                        id: index,
                    });
                    if(item.type == "bandage") {
                        this.bandages++;
                        console.log(this.bandages);
                        this.socket.emit("player_updates", {
                           type: "new_bandage",
                           nums: this.bandages
                        });
                    }
                    else{
                        this.medkits ++;
                        console.log(this.medkits);
                        this.socket.emit("player_updates", {
                            type: "new_medkit",
                            nums: this.medkits
                         });
                    }
                    this.game.items[index] = null;
                    return true;
                }
                else if(item.type.indexOf("helmet") != -1 || item.type.indexOf("chestplate") != -1) {
                    var helm;
                    var largness;
                    
                    if(item.type.indexOf("helmet") != -1) {
                        helm = true;
                        largness = item.type.substring(6);
                        if(this.helmet >= largness)
                            return false;
                        else {
                            this.totalHealth += (largness - this.helmet) * 25;
                            this.health += (largness - this.helmet) * 25;
                        }
                    }
                    else {
                        helm = false;
                        largness = item.type.substring(10);
                        if(this.chestplate >= largness)
                            return false;
                        else {
                            this.totalHealth += (largness - this.chestplate) * 25;
                            this.health += (largness - this.chestplate) * 25;
                        }
                    }

                    this.game.updates.push({
                        type: "remove_item",
                        id: index
                    });


                    if(helm) {
                        this.socket.emit("player_updates", {
                            type: "helmet",
                            id: index,
                            level: largness
                        });
                    }
                    else {
                        this.socket.emit("player_updates", {
                            type: "chestplate",
                            id: index,
                            level: largness
                        });
                    }


                    this.game.items[index] = null;
                    return true;
                }
                else if(item.type.indexOf("scope") != -1) {
                    if(this.currentScope >= parseInt(item.type.substring(5)))
                        return false;
                    this.game.updates.push({
                        type: "remove_item",
                        id: index
                    });
                    this.socket.emit("player_updates", {
                        type: "scope",
                        level: parseInt(item.type.substring(5))
                    });
                    this.game.items[index] = null;
                    return true;
                }
                let pickup;
                if(item.type == "rifle") {
                    pickup = new Rifle(this);
                } else if(item.type == "shotgun") {
                    pickup = new Shotgun(this);
                } else if(item.type == "sniper"){
                    pickup = new Sniper(this);
                } else {
                    pickup = new Pistol(this);
                }
                if(!this.weapons[0]) {
                    this.game.updates.push({
                        type: "pickup_weapon",
                        gt: item.type,
                        id: this.index,
                        index:1,
                        spare: pickup.ammo
                    });
                    this.weapons[0] = pickup;
                } else if(!this.weapons[1]) {
                    this.game.updates.push({
                        type: "pickup_weapon",
                        gt: item.type,
                        id: this.index,
                        index: 2,
                        spare: pickup.ammo
                    });
                    this.weapons[1] = pickup;
                } else if(this.weapons[this.equippedWeapon - 1]) {
                    //item exchange
                    const oldGun = this.weapons[this.equippedWeapon - 1];
                    this.weapons[this.equippedWeapon - 1] = pickup;
                    this.game.updates.push({
                        type: "pickup_weapon",
                        gt: item.type,
                        id: this.index,
                        index: this.equippedWeapon,
                        spare: pickup.ammo
                    });
                    let moveAngle = Math.random() * 360;
                    let newDrop;
                    if(oldGun.name == "rifle") {
                        newDrop = new DroppedRifle(this.x, this.y, moveAngle);
                    } else if(oldGun.name == "shotgun") {
                        newDrop = new DroppedShotgun(this.x, this.y, moveAngle);
                    } else if(oldGun.name == "sniper") {
                        newDrop = new DroppedSniper(this.x, this.y, moveAngle);
                    } else {
                        newDrop = new DroppedPistol(this.x, this.y, moveAngle);
                    }
                    this.game.io.emit("new_dropped_item", {
                        type: newDrop.type,
                        x: this.x,
                        y: this.y,
                        id: this.game.items.length
                    });
                    this.game.io.emit("ammo", {
                        item: this.equippedWeapon,
                        clip: this.weapons[this.equippedWeapon - 1].clipSize,
                        blue: this.blueAmmo,
                        red: this.redAmmo
                    });
                    this.game.items.push(newDrop);
                    for(let i = 0;i < parseInt(oldGun.ammo / oldGun.magSize / 1.5);i ++) {
                        const ammo = new Ammo(this.x, this.y, Math.random() * 360, Math.random() * 800)
                        this.game.io.emit("new_dropped_item", {
                            type: ammo.type,
                            x: this.x,
                            y: this.y,
                            id: this.game.items.length
                        });
                        this.game.items.push(ammo);
                    }
                } else {
                    return false;
                }
                this.game.updates.push({
                    type: "remove_item",
                    id: index
                });
                this.game.items[index] = null;
                return true;
            }   
        });
    }

    useBandage() {
        if(this.bandages > 0) {
            this.bandages-= 1;
            this.health += 25;
            this.health = Math.min(this.health, this.totalHealth);
            this.socket.emit("player_updates", {
                type: "new_bandage",
                nums: this.bandages
            });
        }

    }
    useMedkit() {
        if(this.medkits > 0) {
            this.medkits -= 1;
            this.health = this.totalHealth;
            this.socket.emit("player_updates", {
                type: "new_medkit",
                nums: this.medkits
            });
        }
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
                    thickness: this.weapons[this.equippedWeapon - 1].thickness,
                    dir: bullet.direction
                });
                this.game.bullets.push(bullet);
            });
            var ammo = this.blueAmmo;
            if(this.weapons[this.equippedWeapon - 1].color == "red")
                ammo = this.redAmmo;
            this.socket.emit("ammo", {
                equip: this.equippedWeapon,
                clip: this.weapons[this.equippedWeapon - 1].clipSize,
                spare: ammo,
                blue: this.blueAmmo,
                red: this.redAmmo
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
        if(weapon) {
        if(weapon.color == "red")
        {
            if(!weapon || this.isReloading() || !this.redAmmo || weapon.clipSize == weapon.magSize || Date.now() - weapon.shootDelay <= weapon.lastShootTime) return;
            this.lastReloadTime = Date.now();
            this.socket.emit("reload", this.weapons[this.equippedWeapon - 1].reloadTime);
        }
        else {
            if(!weapon || this.isReloading() || !this.blueAmmo || weapon.clipSize == weapon.magSize || Date.now() - weapon.shootDelay <= weapon.lastShootTime) return;
            this.lastReloadTime = Date.now();
            this.socket.emit("reload", this.weapons[this.equippedWeapon - 1].reloadTime);
        }
        }
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

function generateRandomMap(game) {
    const ret = game.obstacles;
    let bushes = 60;
    let trees = 60;
    let rocks = 60;
    let boxes = 60;
    let barrels = 30;
    let items = 20;
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
    while(items--) {
        var itemz = getRandomItem(Math.random() * 4000, Math.random() * 4000, Math.random() * 360);
        if(itemz instanceof DroppedGun)  {
            if(itemz.color == "red") {
                game.items.push(new RedAmmo(itemz.x - 37, itemz.y + 37, itemz.angle, 0));
                game.items.push(new RedAmmo(itemz.x + 37, itemz.y + 37, itemz.angle, 0));
            }
            else {
                game.items.push(new BlueAmmo(itemz.x - 37, itemz.y + 37, itemz.angle, 0));
                game.items.push(new BlueAmmo(itemz.x +37, itemz.y + 37, itemz.angle, 0));
            }
        }
        game.items.push(itemz);
    }
    return ret;
}


function collisionCircle(x1, y1, r1, x2, y2, r2) {
    return ((x2-x1) * (x2-x1) + (y1-y2) * (y1 - y2)) <= ((r1+r2) * (r1+r2));
}

function collisionCircleBullet(x1, y1, r1, bullet) {
    return collisionCirclePoint(x1, y1, r1, bullet.backX, bullet.backY) ||
    collisionCirclePoint(x1, y1, r1, bullet.centerX, bullet.centerY) ||
    collisionCirclePoint(x1, y1, r1, bullet.x, bullet.y);
}

function collisionCirclePoint(x1, y1, r1, x2, y2) {
    return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) <= (r1 + 5) * (r1 + 5);
}

function collisionCircleSquare(x1, y1, r1, x2, y2, size) {
    const nearX = Math.max(x2 - size / 2, Math.min(x1, x2 + size / 2));
    const nearY = Math.max(y2 - size / 2, Math.min(y1, y2 + size / 2));
    const dx = x1 - nearX;
    const dy = y1 - nearY;
    return (dx * dx + dy * dy <= (r1 * r1));
}

function collisionBulletBox(x, y, size1, bullet) {
    return collisionSquarePoint(x, y, size1, bullet.backX, bullet.backY) ||
    collisionSquarePoint(x, y, size1, bullet.centerX, bullet.centerY) ||
    collisionSquarePoint(x, y, size1, bullet.x, bullet.y)
}

function collisionSquarePoint(x1, y1, size, x2, y2) {
    return x1 - size / 2 < x2 && x2 < x1 + size / 2 && y1 - size / 2 < y2 && y2 < y1 + size / 2;
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

    if(Math.abs(nearX - x1) == Math.abs(nearY - y1)) {
        //corner
        return fixCollidedObject(x1, y1, size * Math.sqrt(2) / 2, x2, y2, r2);
    }

    let xOff = nearX - x1;
    let yOff = nearY - y1;
    if(Math.abs(xOff) > Math.abs(yOff)) {
        xOff = (size / 2 + r2) * Math.sign(xOff);
    } else {
        yOff = (size / 2 + r2) * Math.sign(yOff)
    }
    return [Math.round(x1 + xOff * 1.02), Math.round(y1 + yOff * 1.02)];
}

function killPlayer(player, game) {
    game.io.emit("delete_player", player.index); 
    player.weapons.forEach((weapon) => {
        if(weapon == null) return;
        let dropWeapon;
        if(weapon.name == "rifle") {
            dropWeapon = new DroppedRifle(player.x, player.y, Math.random() * 360, Math.random() * 400 + 800);
        } else if(weapon.name == "shotgun") {
            dropWeapon = new DroppedShotgun(player.x, player.y, Math.random() * 360, Math.random() * 400 + 800);
        } else if(weapon.name == "sniper"){
            dropWeapon = new DroppedSniper(player.x, player.y, Math.random() * 360, Math.random() * 400 + 800);
        } else {
            dropWeapon = new DroppedPistol(player.x, player.y, Math.random() * 360, Math.random() * 400 + 800);
        }
        game.io.emit("new_dropped_item", {
            type: dropWeapon.type,
            x: player.x,
            y: player.y,
            id: game.items.length
        });
        game.items.push(dropWeapon);
        for(let i = 0;i < parseInt(weapon.ammo / weapon.magSize / 1.5);i ++) {
            const ammo = new Ammo(player.x, player.y, Math.random() * 360, Math.random() * 800)
            game.io.emit("new_dropped_item", {
                type: ammo.type,
                x: player.x,
                y: player.y,
                id: game.items.length
            });
            game.items.push(ammo);
        }
    });
    delete game.players[player.index];
}

function getRandomItem(x, y, angle) {
    const chance = Math.random() * 100;
    let dropItem;
    if(chance < 2)
        dropItem = new DroppedSniper(x, y, angle, 0);
    else if(chance < 9)
        dropItem = new DroppedRifle(x, y, angle, 0);
    else if(chance < 16)
        dropItem = new DroppedShotgun(x, y, angle, 0);
    else if (chance < 31)
        dropItem = new DroppedPistol(x, y, angle, 0);
    else if (chance < 40)
        dropItem = new Scope2(x, y, angle, 0);
    else if(chance < 46)
        dropItem = new Scope4(x, y, angle, 0);
    else if(chance < 49)
        dropItem = new Scope8(x, y, angle, 0);
    else if(chance < 49.5)
        dropItem = new Scope15(x, y, angle, 0);
    else if (chance < 60)
        dropItem = new Bandage(x, y, angle, 0);
    else if (chance < 63)
        dropItem = new Medkit(x, y, angle, 0);
    else if (chance < 73)
        dropItem = new ChestPlateOne(x, y, angle, 0);
    else if (chance < 76)
        dropItem = new ChestPlateTwo(x, y, angle, 0);
    else if(chance < 77)
        dropItem = new ChestPlateThree(x, y, angle, 0);
    else if (chance < 87)
        dropItem = new HelmetOne(x, y, angle);
    else if(chance < 90)
        dropItem = new HelmetTwo(x, y, angle);
    else if(chance < 92)
        dropItem = new HelmetThree(x, y, angle);
    else if(chance < 96)
        dropItem = new BlueAmmo(x, y, angle, 0);
    else
        dropItem = new RedAmmo(x, y, angle, 0);
    return dropItem;
}

module.exports.Game = Game;
module.exports.Player = Player;
