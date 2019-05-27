class Item {
    constructor(name, x, y, isBox, size, angle) { //size is radius of circle or half the length of box
        this.x = x;
        this.y = y;
        this.isBox = isBox;
        this.size = size;
        this.moveAway = 500;
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
            movement = (this.moveAway/(dis) * 3);
        }
        if(movement < 10) return false;
        this.x = this.x + movement * Math.cos(this.angle);
        this.y = this.y + movement * Math.sin(this.angle);
        return true;
    }
}

class Ammo extends Item {
    constructor(x, y, angle) {
        super("ammo", x, y, true, 20, angle);
    }
}

class DroppedGun extends Item {
    constructor(name, x, y, angle) {
        super(name, x, y, false, 65, angle);
    }
}

class DroppedRifle extends DroppedGun {
    constructor(x, y, angle) {
        super("rifle", x, y, angle);
    }
}

class DroppedShotgun extends DroppedGun {
    constructor(x, y, angle) {
        super("shotgun", x, y, angle);
    }
}

class DroppedPistol extends DroppedGun {
    constructor(x, y, angle) {
        super("pistol", x, y, angle);
    }
}

class DroppedSniper extends DroppedGun {
    constructor(x, y, angle) {
        super("sniper", x, y, angle);
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
module.exports.DroppedPistol = DroppedPistol;
module.exports.DroppedSniper = DroppedSniper;
module.exports.Bandage = Bandage;