package www.stormygames.camera;

import www.stormygames.Objeto.Actor;
import www.stormygames.utils.Position;

import static org.lwjgl.opengl.GL11.*;

public class Camera {
    public int idCamera;
    public String nameCamera;
    public int width, height, port_x, port_y, port_w, port_h;
    public float x, y;
    public float friccion = 0.2f;

    public Actor targetActor = null;
    public Position targetPoint = null;
    public Position target = null;


    public Camera() {
        this.idCamera = 0;
        this.nameCamera = "Camara 1";
        this.width = 640;
        this.height = 360;
        this.port_x = 0;
        this.port_y = 0;
        this.port_w = 640;
        this.port_h = 360;
        this.x = 0;
        this.y = 0;
    }

    public Camera(
            int idCamera,
            String nameCamera,
            int width,
            int height,
            float x,
            float y,
            int port_x,
            int port_y,
            int port_w,
            int port_h
            ) {
        this.idCamera = idCamera;
        this.nameCamera = nameCamera;
        this.width = width;
        this.height = height;
        this.port_x = port_x;
        this.port_y = port_y;
        this.port_w = port_w;
        this.port_h = port_h;
        this.x = x;
        this.y = y;
    }

    public void update(){
        glViewport(port_x, port_y, port_w, port_h);

        // Fondo exclusivo de la cámara 1
        // backgroundColor(camara1.x, camara1.y, camara1.x + camara1.width, camara1.y, camara1.x + camara1.width, camara1.y + camara1.height, camara1.x, camara1.y + camara1.height, 0.25f, 0.12f, 0.12f);

        // Proyección Cámara 1
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, 1, -1);

        // Vista Cámara 1
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(-x, -y, 0.0f);


        if (targetPoint != null){
            target = targetPoint;
        }else{
            target = targetActor.getPosition();
        }
        x = target.x() - (width / 2f);
        y = 0 ;

        if (x < 0.0f) x = 0.0f;
        if (y < 0.0f) y = 0.0f;
    }

    public void target(Position p){
        targetActor = null;
        targetPoint = p;
    }
    public void target(Actor actor){
        targetPoint = null;
        targetActor = actor;
    }

}
