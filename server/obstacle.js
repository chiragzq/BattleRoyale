class Obstacle {
    constructor(x, y, size) {
        this.x = x;
        this.y = y;
        this.health = 100;
        this.size = size;
    }
}

class Bush extends Obstacle {
    constructor(x, y) {
        super(x, y, 100);
    }
}

class Rock extends Obstacle {
    constructor(x, y) {
        super(x, y, 100);
    }
}
