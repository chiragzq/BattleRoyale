class Item {
    constructor(x, y, isBox, size) { //size is radius of circle or half the length of box
        this.x = x;
        this.y = y;
        this.isBox = isBox;
        this.size = size;
    }

    collision(xLoc, yLoc, radius) { //Checks if something is at same location with item; xLoc/yLoc is center of object
        if(!this.isBox)
            return ((this.x - xLoc) * (this.x - xLoc) + (this.y - yLoc) * (this.y - yLoc)) <= ((size + radius) * (size + radius));
        else {
            isInX1 = this.x - this.size <= xLoc + radius;
            isInX2 = this.x + this.size >= xLoc - radius;
            isInY1 = this.y + this.size >= yLoc - radius;
            isInY2 = this.y - this.size <= yLoc + radius;
            return isInX1 && isInX2 && isInY1 && isInY2;
        }
    }

    move(xDirection, yDirection) { //This is probably unneeded
        this.x += xDirection;
        this.y += yDirection;
    }
}

class Ammo extends Item {
    constructor(x, y) {
        super(x, y, true, 15);
    }
}

class DroppedGun extends Item {
    constructor(x, y) {
        super(x, y, false, 50);
    }
}

class DroppedRifle extends DroppedGun {
    constructor(x, y) {
        super(x, y);
    }
}

class DroppedShotgun extends DroppedGun {
    constructor(x, y,) {
        super(x, y);
    }
}

class Bandage extends Item {
    constructor(x, y) {
        super(x, y, false, 8);
    }
}

module.exports.Item = Item;
module.exports.Ammo = Ammo;
module.exports.DroppedGun = DroppedGun;
module.exports.DroppedRifle = DroppedRifle;
module.exports.DroppedShotgun = DroppedShotgun;
module.exports.Bandage = Bandage;