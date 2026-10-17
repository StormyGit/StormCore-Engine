package www.stormygames;

import www.stormygames.Objeto.Scene;
import www.stormygames.utils.Color;

public final class GameConfig {

    public static final String WIN_NAME = "game";

    public static final int WIN_HEIGHT = 208*3;
    public static final int WIN_WIDTH = 256*3;
    public static final boolean WIN_RESIZE = false;
    private static long WIN_WINDOW = -1;

    public static final Color COLOR_BACKGROUND = new Color(0.57f, 0.56f, 1f);
    public static final Color COLOR_DRAWS = new Color(1.0f, 1.0f, 1.0f);

    public static final boolean VSYNC = true;
    public static final int FPS = 60;

    public static final boolean PIXEL_INTERPOLATION = false;

    public static final boolean ACTOR_SHOW_HIXBOX = true;

    public static final boolean SHOW_LOGS = true;

    private GameConfig() {}

    public static void setWindow(long window){
        if (WIN_WINDOW == -1) WIN_WINDOW = window;
    }
    public static long GetWindow(){
        return WIN_WINDOW;
    }
}