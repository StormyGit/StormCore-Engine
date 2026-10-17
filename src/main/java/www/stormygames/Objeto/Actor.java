package www.stormygames.Objeto;

import www.stormygames.GameConfig;
import www.stormygames.GameData;
import www.stormygames.utils.*;

import java.util.List;

public abstract class Actor extends Access implements IActor {
    public float x, y;
    private float xStart, yStart;
    public int width, height;
    public ActorTipo type;
    private List<IActor> listActorSave;

    public Actor(float x, float y) {
        this.x = x;
        this.y = y;
        this.xStart = x;
        this.yStart = y;
        this.AccessIndex = AccessType.Actor;
        this.id = GameData.stepNextIdCurrentActors();
        log.LOG_INFO(AccessIndex, this.id,"Inst new: " + this.id);
    }

    public void start(){}
    public void update(double deltaTime){}
    public void render(){}

    public void initActor(int width, int height, ActorTipo type){
        this.width = width;
        this.height = height;
        this.type = type;

        if (GameData.getSceneCurrent() == null){
            throw new IllegalArgumentException("NO HAY SCENE POR LEER!!!!!!!!");
        }



        log.LOG_INFO(AccessIndex, this.id,"Inst init finalice");
    }


    protected void moveAndSlide(double dx, double dy) {

        // Obtener actores de la escena actual
        List<Actor> actors = GameData.getSceneCurrent().getSolidActors();

        // ===========================
        // MOVIMIENTO HORIZONTAL
        // ===========================

        double nextX = x + dx;

        for (IActor obj : actors) {

            if (!(obj instanceof Actor other)) continue;

            // Ignorar al mismo actor
            if (other == this) continue;


            float halfW = width / 2f;
            float halfH = height / 2f;

            float otherHalfW = other.width / 2f;
            float otherHalfH = other.height / 2f;

            // Comprobar superposición vertical
            boolean overlapY =
                    y - halfH < other.y + otherHalfH &&
                            y + halfH > other.y - otherHalfH;

            if (!overlapY) continue;

            // Movimiento a la derecha
            if (dx > 0) {

                float right = x + halfW;
                float wallLeft = other.x - otherHalfW;

                if (right <= wallLeft &&
                        nextX + halfW > wallLeft) {

                    nextX = Math.min(
                            nextX,
                            wallLeft - halfW
                    );
                }
            }

            // Movimiento a la izquierda
            else if (dx < 0) {

                float left = x - halfW;
                float wallRight = other.x + otherHalfW;

                if (left >= wallRight &&
                        nextX - halfW < wallRight) {

                    nextX = Math.max(
                            nextX,
                            wallRight + halfW
                    );
                }
            }
        }
        TileMap map = GameData.getSceneCurrent().getTileMap();

        if (map != null) {

            float allowedX = map.resolveX(
                    x, y,  width, height,
                    (float) nextX - x
            );

            nextX = x + allowedX;
        }

        x = (float) nextX;

        // ===========================
        // MOVIMIENTO VERTICAL
        // ===========================

        double nextY = y + dy;

        for (IActor obj : actors) {

            if (!(obj instanceof Actor other)) continue;
            if (other == this) continue;

            float halfW = width / 2f;
            float halfH = height / 2f;

            float otherHalfW = other.width / 2f;
            float otherHalfH = other.height / 2f;

            // Comprobar superposición horizontal
            boolean overlapX =
                    x - halfW < other.x + otherHalfW &&
                            x + halfW > other.x - otherHalfW;

            if (!overlapX) continue;

            // Movimiento hacia abajo
            if (dy > 0) {

                float bottom = y + halfH;
                float wallTop = other.y - otherHalfH;

                if (bottom <= wallTop &&
                        nextY + halfH > wallTop) {

                    nextY = Math.min(
                            nextY,
                            wallTop - halfH
                    );
                }
            }

            // Movimiento hacia arriba
            else if (dy < 0) {

                float top = y - halfH;
                float wallBottom = other.y + otherHalfH;

                if (top >= wallBottom &&
                        nextY - halfH < wallBottom) {

                    nextY = Math.max(
                            nextY,
                            wallBottom + halfH
                    );
                }
            }
        }
        if (map != null) {

            float allowedY = map.resolveY(
                    x, y, width, height,
                    (float) nextY - y
            );

            nextY = y + allowedY;
        }


        y = (float) nextY;
    }

    protected void render_dev(){
        if (GameConfig.ACTOR_SHOW_HIXBOX) {
            Draws.rectangleCentered(x, y, width, height);
            Draws.rectangleCentered(x, y, 1, 1);
            Draws.colorReset();
        }
    }

    public int getId(){ return id;}

    public ActorTipo getActorType() { return type; }

    public Position getPosition() {
        return new Position(x, y);
    }

}
