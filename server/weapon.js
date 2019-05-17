class Gun {
    constructor(name, player, magSize, spread, damage, bulletSpeed, barrelLength, reloadTime) {
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
    }

    fireBullet(direction) {
        const bulletX = Math.round(Math.cos(this.player.direction * Math.PI / 180) * (50) + this.player.x);
        const bulletY = Math.round(Math.sin(this.player.direction * Math.PI / 180) * (50) + this.player.y);
        return new Bullet(bulletX, bulletY, direction, this.speed, this.damage);
    }

    reload() {
        const reloadAmount = Math.min(this.magSize - this.clipSize, this.ammo);
        this.ammo -= reloadAmount;
        this.clipSize += reloadAmount;
    }
}

class Rifle extends Gun {
    constructor(player) {
        super("Rifle", player, 30, 2, 18, 90, 80, 1800);
    }

    fire() {
        if(!this.clipSize) return [];
        this.clipSize--;
        return [this.fireBullet(Math.round((Math.random() - 0.5) * this.spread + this.player.direction))];
    }
}

class Shotgun extends Gun {
    constructor(player) {
        super("Shotgun", player, 5, 35, 4, 75, 80, 400)
    }

    fire() {
        if(!this.clipSize) return[];
        this.clipSize--;
        const ret = [];
        for(let i = 0;i <= 7;i ++) {
            ret.push(this.fireBullet(Math.round(
                this.player.direction + 
                this.spread / 7 * i - 
                this.spread / 2 + 
                (Math.random() * this.spread / 10 - this.spread / 20))));
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
        return this.x < -200 || 1500 < this.x || this.y < -200 || 1080 < this.y;
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