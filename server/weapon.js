let timeout = setTimeout(()=>{},0);
class Gun {
    constructor(name, player, magSize, spread, damage, bulletSpeed, barrelLength, reloadTime, shootDelay, bulletDistance, bulletFallOff, thickness, color) {
        this.name = name;
        this.player = player;

        this.magSize = magSize;
        this.clipSize = 0;
        this.ammo = 0;

        this.spread = spread;
        this.damage = damage;
        this.speed = bulletSpeed;

        this.barrelLength = barrelLength;

        this.reloadTime = reloadTime;

        this.shootDelay = shootDelay;
        this.lastShootTime = 0;

        this.bulletDistance = bulletDistance;
        this.bulletFallOff = bulletFallOff;
        this.thickness = thickness;
        this.color = color;
    }

    fireBullet(direction) {
        this.lastShootTime = Date.now();
        const bulletX = Math.round(Math.cos(this.player.direction * Math.PI / 180) * (150) + this.player.x);
        const bulletY = Math.round(Math.sin(this.player.direction * Math.PI / 180) * (150) + this.player.y);
        return new Bullet(bulletX, bulletY, direction, this.speed, this.damage, this.bulletDistance, this.bulletFallOff);
    }

    canShoot() {
        return this.clipSize && Date.now() - this.shootDelay > this.lastShootTime;
    }

    reload() {
        if(this.color == "red") {
            const reloadAmount = Math.min(this.magSize - this.clipSize, this.player.redAmmo);
            this.player.redAmmo -= reloadAmount;
            this.clipSize += reloadAmount;
        }
        else {
            const reloadAmount = Math.min(this.magSize - this.clipSize, this.player.blueAmmo);
            this.player.blueAmmo -= reloadAmount;
            this.clipSize += reloadAmount;
        }
    }
}

class Rifle extends Gun {
    constructor(player) {
        super("rifle", player, 20, 3, 22, 70, 80, 2400, 250, 2000, 0.9, 3, "blue");
        this.color = "blue";
    }

    fire() {
        if(!this.canShoot()) return [];
        this.player.speed = 5;
        clearTimeout(timeout);
        timeout = setTimeout(() => {this.player.speed = 13}, this.shootDelay + 100);
        this.clipSize--;
        return [this.fireBullet(Math.round((Math.random() - 0.5) * this.spread + this.player.direction))];
    }
}

class Sniper extends Gun {
    constructor(player) {
        super("sniper", player, 5, 0, 100, 80, 100, 800, 1000, 4000, 0.95, 7, "red");
        this.color = "red";
    }

    fire() {
        if(!this.canShoot()) return [];
        this.clipSize--;
        this.player.speed = 5;
        clearTimeout(timeout);
        timeout = setTimeout(() => {this.player.speed = 13}, this.shootDelay + 100);
        return [this.fireBullet(Math.round((Math.random()- 0.5) * this.spread + this.player.direction))];
    }

    reload() {
        if(this.player.redAmmo && this.clipSize < this.magSize) {
            this.clipSize++;
            this.player.redAmmo--;
        }
    }
}

class Shotgun extends Gun {
    constructor(player) {
        super("shotgun", player, 2, 20, 12, 55, 80, 2800, 300, 250, 0.7, 3, "red")
        //super("Shotgun", player, 20, 70, 20, 75, 80, 100)
        this.color = "red";
    }

    fire() {
        if(!this.canShoot()) return [];
        this.clipSize--;
        this.player.speed = 5;
        clearTimeout(timeout);
        timeout = setTimeout(() => {this.player.speed = 13}, this.shootDelay + 100);
        const ret = [];
        const numBullets = 8;
        for(let i = 0;i <= numBullets;i ++) {
            ret.push(this.fireBullet(Math.round(
                this.player.direction + 
                this.spread / numBullets * i - 
                this.spread / 2 + 
                (Math.random() * this.spread / numBullets / 1.5 - this.spread / numBullets / 3))));
        }
        return ret;
    }


}

class Pistol extends Gun {
    constructor(player) {
        super("pistol", player, 7, 8, 15, 65, 55, 2800, 200, 800, 0.8, 4, "blue");
        this.color = "blue";
    }

    fire() {
        if(!this.canShoot()) return [];
        this.player.speed = 5;
        clearTimeout(timeout);
        timeout = setTimeout(() => {this.player.speed = 13}, this.shootDelay + 100);
        this.clipSize--;
        return [this.fireBullet(Math.round((Math.random() - 0.5) * this.spread + this.player.direction))];
    }
}

class Bullet {
    constructor(x, y, direction, speed, damage, maxDistance, fallOff) {
        this.x = x;
        this.y = y; 
        this.length = 100;

        this.direction = direction;
        this.backX = this.x - Math.round(Math.cos(direction * Math.PI / 180) * this.length);
        this.backY = this.y - Math.round(Math.sin(direction * Math.PI / 180) * this.length);

        this.centerX = this.x - Math.round(Math.cos(direction * Math.PI / 180) * this.length / 2);
        this.centerY = this.y - Math.round(Math.sin(direction * Math.PI / 180) * this.length / 2);

        this.radDirection = direction * Math.PI / 180;
        this.distance = 0;
        this.maxDistance = maxDistance;
        this.speed = speed;
        this.damage = damage;
        this.fallOff = fallOff;
    }

    move() {
        const diffX = this.speed * Math.cos(this.radDirection);
        const diffY = this.speed * Math.sin(this.radDirection);
        this.x += diffX
        this.y += diffY;
        this.distance += this.speed

        this.backX += diffX;
        this.backY += diffY;

        this.centerX += diffX;
        this.centerY += diffY;
    }

    isDead() {
        return this.x < -200 || 4200 < this.x || this.y < -200 || 4200 < this.y || this.distance > this.maxDistance;
    }

    /**
     * Returns the damage of the bullet, taking into account the distance it traveled
     */
    getDamage() {
        return this.damage * Math.pow(this.fallOff, this.distance / 100);
    }
}

module.exports.Pistol = Pistol;
module.exports.Gun = Gun;
module.exports.Bullet = Bullet;
module.exports.Rifle = Rifle;
module.exports.Shotgun = Shotgun;
module.exports.Sniper = Sniper;