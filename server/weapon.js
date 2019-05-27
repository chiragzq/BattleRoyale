let timeout = setTimeout(()=>{},0);
class Gun {
    constructor(name, player, magSize, spread, damage, bulletSpeed, barrelLength, reloadTime, shootDelay, bulletDistance, bulletFallOff, thickness) {
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

        this.bulletDistance = bulletDistance;
        this.bulletFallOff = bulletFallOff;
        this.thickness = thickness;
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
        const reloadAmount = Math.min(this.magSize - this.clipSize, this.ammo);
        this.ammo -= reloadAmount;
        this.clipSize += reloadAmount;
    }
}

class Rifle extends Gun {
    constructor(player) {
        super("Rifle", player, 20, 3, 22, 70, 80, 2400, 250, 2000, 0.9, 3);
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
        super("Sniper", player, 5, 0, 95, 80, 90, 1000, 500, 500, 0.99, 7);
    }

    fire() {
        if(!this.canShoot()) return [];
        this.clipSize--;
        this.player.speed = 5;
        clearTimeout(timeout);
        timeout = setTimeout(() => {this.player.speed = 13}, this.shootDelay + 250);
        return [this.fireBullet(Math.round((Math.random()- 0.5) * this.spread + this.player.direction))];
    }

    reload() {
        if(this.ammo && this.clipSize < this.magSize) {
            this.clipSize++;
            this.ammo--;
        }
    }
}

class Shotgun extends Gun {
    constructor(player) {
        super("Shotgun", player, 5, 35, 8, 55, 80, 700, 300, 250, 0.82, 3)
        //super("Shotgun", player, 20, 70, 20, 75, 80, 100)
    }

    fire() {
        if(!this.canShoot()) return [];
        this.clipSize--;
        this.player.speed = 5;
        clearTimeout(timeout);
        timeout = setTimeout(() => {this.player.speed = 13}, this.shootDelay + 200);
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

module.exports.Gun = Gun;
module.exports.Bullet = Bullet;
module.exports.Rifle = Rifle;
module.exports.Shotgun = Shotgun;
module.exports.Sniper = Sniper;