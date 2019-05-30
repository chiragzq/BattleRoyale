class Item {
    constructor(name, x, y, isBox, size, angle, vel) { //size is radius of circle or half the length of box
        this.x = x;
        this.y = y;
        this.isBox = isBox;
        this.size = size;
        this.moveAway = vel ? vel : 300;
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
        if(movement < 1) return false;
        this.x = this.x + movement * Math.cos(this.angle);
        this.y = this.y + movement * Math.sin(this.angle);
        return true;
    }
}

class Ammo extends Item {
    constructor(name, x, y, angle, vel) {
        super(name, x, y, true, 15, angle, vel);
        
    }
}

class BlueAmmo extends Ammo {
    constructor(x, y, angle, vel) {
        super("blueAmmo", x, y, angle, vel);
        this.color = "blue";
    }
}

class RedAmmo extends Ammo {
    constructor(x, y, angle, vel) {
        super("redAmmo", x, y, angle, vel);
        this.color= "red";
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
        this.color = "blue";
    }
}

class DroppedShotgun extends DroppedGun {
    constructor(x, y, angle, vel) {
        super("shotgun", x, y, angle, vel);
        this.color = "red";
    }
}

class DroppedPistol extends DroppedGun {
    constructor(x, y, angle, vel) {
        super("pistol", x, y, angle, vel);
        this.color = "blue";
    }
}

class DroppedSniper extends DroppedGun {
    constructor(x, y, angle, vek) {
        super("sniper", x, y, angle, vek);
        this.color = "red";
    }
}

class Bandage extends Item {
    constructor(x, y, angle) {
        super("bandage", x, y, false, 30, angle);
    }
}

class Medkit extends Item {
    constructor(x, y, angle) {
        super("medkit", x, y, false, 30, angle);
    }
}

class Armor extends Item {
    constructor(name, x, y, angle) {
        super(name, x, y, false, 30, angle);
    }
}

class ChestPlateOne extends Armor {
    constructor(x, y, angle) {
        super("chestplate1", x, y, angle);
    }
}

class HelmetOne extends Armor {
    constructor(x, y, angle) {
        super("helmet1", x, y, angle);
    }
}

class Scope extends Item {
    constructor(name, x, y, angle, vel) {
        super(name, x, y, false, 30, angle, vel);
    }
}

class Scope2 extends Scope {
    constructor(x, y, angle, vel) {
        super("scope2", x, y, angle, vel);
    }
}

class Scope4 extends Scope {
    constructor(x, y, angle, vel) {
        super("scope4", x, y, angle, vel);
    }
}

class Scope8 extends Scope {
    constructor(x, y, angle, vel) {
        super("scope8", x, y, angle, vel);
    }
}

class Scope15 extends Scope {
    constructor(x, y, angle, vel) {
        super("scope15", x, y, angle, vel);
    }
}

module.exports.Item = Item;
module.exports.Ammo = Ammo;
module.exports.BlueAmmo = BlueAmmo;
module.exports.RedAmmo = RedAmmo;
module.exports.DroppedGun = DroppedGun;
module.exports.DroppedRifle = DroppedRifle;
module.exports.DroppedShotgun = DroppedShotgun;
module.exports.DroppedPistol = DroppedPistol;
module.exports.DroppedSniper = DroppedSniper;
module.exports.Bandage = Bandage;
module.exports.Medkit = Medkit;
module.exports.Armor = Armor;
module.exports.ChestPlateOne = ChestPlateOne;
module.exports.HelmetOne = HelmetOne;
module.exports.Scope2 = Scope2;
module.exports.Scope4 = Scope4;
module.exports.Scope8 = Scope8;
module.exports.Scope15 = Scope15;