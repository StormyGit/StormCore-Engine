package www.stormygames.Objeto;

import www.stormygames.utils.Position;

public interface IActor {
    public void start();
    public void update(double deltaTime);
    public void render();
    public Position getPosition();
}
