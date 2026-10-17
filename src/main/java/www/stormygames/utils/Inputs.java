
package www.stormygames.utils;

import static org.lwjgl.glfw.GLFW.*;

public final class Inputs {

    private static long window = -1;

    private static final int KEY_COUNT = GLFW_KEY_LAST + 1;

    private static final boolean[] current = new boolean[KEY_COUNT];
    private static final boolean[] previous = new boolean[KEY_COUNT];

    private Inputs() {}

    public static void setWindow(long win) {
        if (window == -1) window = win;
    }

    // Actualizar una vez por frame
    public static void update() {

        for (int key = GLFW_KEY_SPACE; key < KEY_COUNT; key++) {
            previous[key] = current[key];

            current[key] =
                    glfwGetKey(window, key) == GLFW_PRESS;
        }
    }

    // Mientras está presionada
    public static boolean keyDown(int key) {
        return validKey(key) && current[key];
    }

    // Mientras está suelta
    public static boolean keyUp(int key) {
        return validKey(key) && !current[key];
    }

    // Solo el primer frame de la pulsación
    public static boolean keyPressed(int key) {
        return validKey(key)
                && current[key]
                && !previous[key];
    }

    // Solo el frame en que se suelta
    public static boolean keyReleased(int key) {
        return validKey(key)
                && !current[key]
                && previous[key];
    }

    // Mouse
    public static boolean mouseDown(int button) {
        return glfwGetMouseButton(window, button) == GLFW_PRESS;
    }

    public static boolean mouseUp(int button) {
        return glfwGetMouseButton(window, button) == GLFW_RELEASE;
    }

    private static boolean validKey(int key) {
        return key >= GLFW_KEY_SPACE && key < KEY_COUNT;
    }
}
