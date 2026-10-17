package www.stormygames;

import www.stormygames.Objeto.Scene;
import www.stormygames.camera.Camera;

public final class GameData {
    private static Scene sceneCurrent = null;

    private static Camera camaraCurrent = null;

    private static int idCurrentActors = 0;

    public static int getIdCurrentActors() {
        return idCurrentActors;
    }

    public static int stepNextIdCurrentActors() {
        GameData.idCurrentActors = GameData.idCurrentActors + 1;
        return GameData.idCurrentActors;
    }

    public static Scene getSceneCurrent() {
        return sceneCurrent;
    }

    public static void setSceneCurrent(Scene sceneCurrent) {
        GameData.sceneCurrent = sceneCurrent;
    }

    public static Camera getCamaraCurrent() {
        return camaraCurrent;
    }
    public static void setCamaraCurrent(Camera camaraCurrent) {
        GameData.camaraCurrent = camaraCurrent;
    }
}