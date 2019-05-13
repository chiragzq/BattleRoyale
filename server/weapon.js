class Gun {
    constructor(name, player, magSize, spread, damage, bulletSpeed, barrelLength) {
        this.name = name;
        this.player = player;

        this.magSize = magSize;
        this.clipSize = 100;
        this.ammo = 3 * magSize;

        this.spread = spread;
        this.damage = damage;
        this.speed = bulletSpeed;

        this.barrelLength = barrelLength;
    }

    fireBullet(direction) {
        const bulletX = Math.round(Math.cos(this.player.direction * Math.PI / 180) * this.barrelLength + this.player.x);
        const bulletY = Math.round(Math.sin(this.player.direction * Math.PI / 180) * this.barrelLength + this.player.y);
        return new Bullet(bulletX, bulletY, direction, this.speed, this.damage);
    }

    reload() {
        reloadAmount = Math.min(magSize - clipSize, ammo);
        ammo -= reloadAmount;
        clipSize += this.reload;
    }
}

class Rifle extends Gun {
    constructor(player) {
        super("Rifle", player, 30, 15, 12, 100, 80);
    }

    fire() {
        if(!this.clipSize) return [];
        this.clipSize--;
        return [this.fireBullet(Math.round((Math.random() - 0.5) * this.spread + this.player.direction))];
    }
}

class Bullet {
    constructor(x, y, direction, speed, damage) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.radDirection = direction * Math.PI / 180;
        this.distance = 0;
        this.speed = speed;
        this.damage = damage;
    }

    move() {
        this.x += this.speed * Math.cos(this.radDirection);
        this.y += this.speed * Math.sin(this.radDirection);
        this.distance += this.speed
    }

    isOffScreen() {
        return this.x < 0 || 1080 < this.x || this.y < 0 || 1080 < this.y;
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
module.exports.Rifle = Rifle