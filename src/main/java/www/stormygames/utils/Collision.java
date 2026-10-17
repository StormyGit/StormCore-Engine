package www.stormygames.utils;

import www.stormygames.GameData;
import www.stormygames.Objeto.Actor;
import www.stormygames.Objeto.IActor;

public final class Collision {
    private Collision() {}

    public static boolean rectangle(
            float ax, float ay, float aw, float ah,
            float bx, float by, float bw, float bh) {

        return ax < bx + bw &&
                ax + aw > bx &&
                ay < by + bh &&
                ay + ah > by;
    }


    public boolean collidesWith(Actor self, Actor other) {
        return Collision.rectangle(
                self.x - self.width / 2f,
                self.y - self.height / 2f,
                self.width,
                self.height,

                other.x - other.width / 2f,
                other.y - other.height / 2f,
                other.width,
                other.height
        );
    }

    public static boolean collidesAt(Actor self, Actor other, float nextX, float nextY) {

        float leftA   = nextX - self.width / 2f;
        float rightA  = nextX + self.width / 2f;
        float topA    = nextY - self.height / 2f;
        float bottomA = nextY + self.height / 2f;

        float leftB   = other.x - other.width / 2f;
        float rightB  = other.x + other.width / 2f;
        float topB    = other.y - other.height / 2f;
        float bottomB = other.y + other.height / 2f;

        return leftA < rightB &&
                rightA > leftB &&
                topA < bottomB &&
                bottomA > topB;
    }



}
