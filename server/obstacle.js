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
}

class Bush extends Obstacle {
    constructor(x, y) {
        super(x, y, 30, false);
    }

    getSize() {
        return this.health ? this.size : 0;
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
}

module.exports.Obstacle = Obstacle;
module.exports.Bush = Bush;
module.exports.Rock = Rock;
module.exports.Tree = Tree;
