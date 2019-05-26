const _weapon = require("./weapon");
const Bullet = _weapon.Bullet;

class Obstacle {
    constructor(x, y, size, solid, health) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.size = size;
        this.solid = solid;
    }
    
    getType() {
        return null;
    }

    hurt(damage) {
        this.health -= damage;
        this.health = Math.max(this.health, 0);
        if(!this.health) this.solid = false;
    }

    isDead() {
        return !this.health;
    }
}

class Bush extends Obstacle {
    constructor(x, y) {
        super(x, y, 30, false, 70);
    }

    getSize() {
        return this.size;
    }

    getType() {
        return "bush";
    }
}

class Rock extends Obstacle {
    constructor(x, y) {
        super(x, y, 37, true, 200);
        this.hurt(Math.round(Math.random() * 60));
    }

    getSize() {
        return Math.round(this.size * Math.sqrt(this.health / 200));
    }

    getType() {
        return "rock";
    }

    hurt(damage) {
        this.health -= damage;
        this.health = Math.max(this.health, 0);
        if(this.health < 25) this.solid = false;
    }

    isDead() {
        return this.health < 25;
    }
}

class Tree extends Obstacle {
    constructor(x, y) {
        super(x, y, 30, true, 120);
        this.hurt(Math.round(Math.random() * 36));
    }

    getSize() {
        return Math.round(this.size * Math.sqrt(this.health / 120));
    }

    getType() {
        return "tree";
    }

    hurt(damage) {
        this.health -= damage;
        this.health = Math.max(this.health, 0);
        if(this.health < 25) this.solid = false;
    }

    isDead() {
        return this.health < 25;
    }
}

class Box extends Obstacle {
    constructor(x, y) {
        super(x, y, 100, true, 100);
    }

    getSize() {
        return Math.round(this.size * Math.sqrt(this.health / 100));
    }

    getType() {
        return "box";
    }

    hurt(damage) {
        this.health -= damage;
        this.health = Math.max(this.health, 0);
        if(this.health < 25) this.solid = false;
    }
}

class Barrel extends Obstacle {
    constructor(x, y) {
        super(x, y, 30, true);
    }

    getSize() {
        return Math.round(this.size * Math.sqrt(this.health / 100));
    }

    getType() {
        return "barrel";
    }

    hurt(damage) {
        this.health -= damage;
        this.health = Math.max(this.health, 0);
    }

    spawnBullets(z) {
        var bullet = [];
        for(var i = 0; i < z; i++) {
            bullet.push(new Bullet(this.x, this.y, Math.random() * 360, 48, 50));
        }
        return bullet;
    }
}

module.exports.Obstacle = Obstacle;
module.exports.Bush = Bush;
module.exports.Rock = Rock;
module.exports.Tree = Tree;
module.exports.Box = Box;
module.exports.Barrel = Barrel;
