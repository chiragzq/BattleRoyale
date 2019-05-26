let timeout = setTimeout(()=>{},0);
class Gun {
    constructor(name, player, magSize, spread, damage, bulletSpeed, barrelLength, reloadTime, shootDelay) {
        this.name = name;
        this.player = player;

        this.magSize = magSize;
        this.clipSize = 0;
        this.ammo = 3 * magSize;

        this.spread = spread;
        this.damage = damage;
        this.speed = bulletSpeed;

        this.barrelLength = barrelLength;

        this.reloadTime = reloadTime;

        this.shootDelay = shootDelay;
        this.lastShootTime = 0;
    }

    fireBullet(direction) {
        this.lastShootTime = Date.now();
        const bulletX = Math.round(Math.cos(this.player.direction * Math.PI / 180) * (50) + this.player.x);
        const bulletY = Math.round(Math.sin(this.player.direction * Math.PI / 180) * (50) + this.player.y);
        return new Bullet(bulletX, bulletY, direction, this.speed, this.damage);
    }

    canShoot() {
        return this.clipSize && Date.now() - this.shootDelay > this.lastShootTime;
    }

    reload() {
        const reloadAmount = Math.min(this.magSize - this.clipSize, this.ammo);
        this.ammo -= reloadAmount;
        this.clipSize += reloadAmount;
    }
}

class Rifle extends Gun {
    constructor(player) {
        super("Rifle", player, 20, 3, 22, 90, 80, 2400, 250);
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

class Shotgun extends Gun {
    constructor(player) {
        super("Shotgun", player, 5, 35, 8, 75, 80, 700, 300)
        //super("Shotgun", player, 20, 70, 20, 75, 80, 100)
    }

    fire() {
        if(!this.canShoot()) return [];
        this.clipSize--;
        this.player.speed = 5;
        clearTimeout(timeout);
        timeout = setTimeout(() => {this.player.speed = 13}, this.shootDelay + 400);
        const ret = [];
        const numBullets = 7;
        for(let i = 0;i <= numBullets;i ++) {
            ret.push(this.fireBullet(Math.round(
                this.player.direction + 
                this.spread / numBullets * i - 
                this.spread / 2 + 
                (Math.random() * this.spread / numBullets / 1.5 - this.spread / numBullets / 3))));
        }
        return ret;
    }

    reload() { 
        if(this.ammo && this.clipSize < this.magSize) {
            this.clipSize++;
            this.ammo--;
        }
    }
}

class Bullet {
    constructor(x, y, direction, speed, damage) {
        this.x = x;
        this.y = y;
        this.length = 75;

        this.direction = direction;
        this.backX = x + Math.round(Math.cos(direction * Math.PI / 180) * this.length);
        this.backX = y + Math.round(Math.sin(direction * Math.PI / 180) * this.length);

        this.centerX = x + Math.round(Math.cos(direction * Math.PI / 180) * this.length / 2);
        this.centerY = y + Math.round(Math.sin(direction * Math.PI / 180) * this.length / 2);

        this.radDirection = direction * Math.PI / 180;
        this.distance = 0;
        this.speed = speed;
        this.damage = damage;
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

    isOffScreen() {
        return this.x < -200 || 2200 < this.x || this.y < -200 || 2200 < this.y;
    }

    /**
     * Returns the damage of the bullet, taking into account the distance it traveled
     */
    getDamage() {
        return this.damage * 52 / (41 + 11 * Math.exp(0.002 * this.distance));
    }
}

module.exports.Gun = Gun;
module.exports.Bullet = Bullet;
module.exports.Rifle = Rifle;
module.exports.Shotgun = Shotgun;