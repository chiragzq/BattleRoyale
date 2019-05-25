const _weapon = require("./weapon");
const Bullet = _weapon.Bullet;

class Obstacle {
    constructor(x, y, size, solid) {
        this.x = x;
        this.y = y;
        this.health = 100;
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
        super(x, y, 30, false);
        this.health = 70;
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
        super(x, y, 30, true);
    }

    getSize() {
        return Math.round(this.size * Math.sqrt(this.health / 100));
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
        super(x, y, 30, true);
    }

    getSize() {
        return Math.round(this.size * Math.sqrt(this.health / 100));
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
        super(x, y, 100, true);
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
            bullet.push(new Bullet(this.x, this.y, Math.random() * 360, 10, 50));
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
