package www.stormygames.utils;

import www.stormygames.GameConfig;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor4f;

public final class Draws {

    // Evitar que se creen instancias
    private Draws() {}

    // Dibujar línea
    public static void line(float x1, float y1, float x2, float y2) {
        glBegin(GL_LINES);
            glVertex2f(x1, y1);
            glVertex2f(x2, y2);
        glEnd();
    }

    // Dibujar rectángulo sin relleno
    public static void rectangle(float x, float y, float width, float height) {

        glBegin(GL_LINE_LOOP);
            glVertex2f(x, y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x, y + height);
        glEnd();
    }

    // Dibujar rectángulo centrado
    public static void rectangleCentered(float x, float y, float width, float height) {
        rectangle(
                x - width / 2f,
                y - height / 2f,
                width,
                height
        );
    }

    // Dibujar rectángulo con relleno
    public static void fillRectangle( float x, float y, float width, float height) {
        glBegin(GL_QUADS);
            glVertex2f(x, y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x, y + height);
        glEnd();
    }

    // Dibujar círculo
    public static void circle(float x, float y, float radius) {

        int segments = 24;

        glBegin(GL_LINE_LOOP);

        for (int i = 0; i < segments; i++) {
            double angle = 2.0 * Math.PI * i / segments;

            float px = x + (float) Math.cos(angle) * radius;
            float py = y + (float) Math.sin(angle) * radius;

            glVertex2f(px, py);
        }

        glEnd();
    }

    public static void colorReset(){
        glColor4f(GameConfig.COLOR_DRAWS.r,GameConfig.COLOR_DRAWS.g,GameConfig.COLOR_DRAWS.b,1f);
    }

    public static void color(float r,float g,float b){
        glColor4f(r,g,b,1.0f);
    }
    public static void color(float r,float g,float b, float a){
        glColor4f(r,g,b,a);
    }
    public static void alpha(float a) {
        float[] currentColor = new float[4];
        glGetFloatv(GL_CURRENT_COLOR, currentColor);
        glColor4f( currentColor[0], currentColor[1], currentColor[2], a);
    }
}