class Item {
    constructor(name, x, y, isBox, size, angle, vel) { //size is radius of circle or half the length of box
        this.x = x;
        this.y = y;
        this.isBox = isBox;
        this.size = size;
        this.moveAway = vel ? vel : 500;
        this.start = Date.now();
        this.angle = angle;

        this.type = name;
    }

    collision(xLoc, yLoc, radius) { //Checks if something is at same location with item; xLoc/yLoc is center of object
        if(!this.isBox)
            return ((this.x - xLoc) * (this.x - xLoc) + (this.y - yLoc) * (this.y - yLoc)) <= ((this.size + radius) * (this.size + radius));
        else {
            var isInX1 = this.x - this.size <= xLoc + radius;
            var isInX2 = this.x + this.size >= xLoc - radius;
            var isInY1 = this.y + this.size >= yLoc - radius;
            var isInY2 = this.y - this.size <= yLoc + radius;
            return isInX1 && isInX2 && isInY1 && isInY2;
        }
    }

    move(xDirection, yDirection) { //This is probably unneeded <-- I agree
        this.x += xDirection;
        this.y += yDirection;
    }

    move() {
        let movement = 0;
        if(Date.now() - this.start < this.moveAway) {
            let dis = Date.now() - this.start;
            if (Date.now() - this.start < 100)
                dis = 100;
            movement = (this.moveAway/(dis) * 2);
        }
        if(movement < 1) return false;
        this.x = this.x + movement * Math.cos(this.angle);
        this.y = this.y + movement * Math.sin(this.angle);
        return true;
    }
}

class Ammo extends Item {
    constructor(x, y, angle, vel) {
        super("ammo", x, y, true, 20, angle, vel);
    }
}

class DroppedGun extends Item {
    constructor(name, x, y, angle, vel) {
        super(name, x, y, false, 65, angle, vel);
    }
}

class DroppedRifle extends DroppedGun {
    constructor(x, y, angle, vel) {
        super("rifle", x, y, angle, vel);
    }
}

class DroppedShotgun extends DroppedGun {
    constructor(x, y, angle, vel) {
        super("shotgun", x, y, angle, vel);
    }
}

class Bandage extends Item {
    constructor(x, y, angle) {
        super(x, y, false, 8, angle);
    }
}

module.exports.Item = Item;
module.exports.Ammo = Ammo;
module.exports.DroppedGun = DroppedGun;
module.exports.DroppedRifle = DroppedRifle;
module.exports.DroppedShotgun = DroppedShotgun;
module.exports.Bandage = Bandage;