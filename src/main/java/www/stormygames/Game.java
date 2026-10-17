package www.stormygames;

import www.stormygames.Objeto.Actor;
import www.stormygames.Objeto.Scene;
import www.stormygames.access.actors.Player;
import www.stormygames.access.scenery.sceneryDev;
import www.stormygames.camera.Camera;
import www.stormygames.utils.Inputs;
import www.stormygames.utils.log;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;

public class Game {

    List<Camera> listCamera = new ArrayList<>();
    List<Actor> listObject = new ArrayList<>();

    private void init(){
        log.LOG_INFO_SYS("inicializar...");
        if (!glfwInit()) {
            log.LOG_ERROR("No se pudo inicializar GLFW");
            throw new IllegalStateException("No se pudo inicializar GLFW");
        }

        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_VISIBLE,    GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,  GameConfig.WIN_RESIZE ? GLFW_TRUE : GLFW_FALSE);

        long window = glfwCreateWindow(
                GameConfig.WIN_WIDTH,
                GameConfig.WIN_HEIGHT,
                GameConfig.WIN_NAME,
                0,
                0
        );

        if (window == 0) {
            log.LOG_ERROR("No se pudo crear la ventana");
            throw new RuntimeException( "No se pudo crear la ventana" );
        }

        GameConfig.setWindow(window);
        Inputs.setWindow(window);
        glfwMakeContextCurrent(GameConfig.GetWindow());

        glfwSwapInterval(GameConfig.VSYNC ? 1 : 0);

        glfwShowWindow(GameConfig.GetWindow());

        glfwSetInputMode(GameConfig.GetWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);

        org.lwjgl.opengl.GL.createCapabilities();

        log.LOG_INFO_SYS("se inicializo todo");
    }
    private void loop() {
        log.LOG_INFO_SYS("loop del juego");

        double lastTime = glfwGetTime();
        double tiempoPorFrame = 1.0 / GameConfig.FPS;

        glClearColor(GameConfig.COLOR_BACKGROUND.r, GameConfig.COLOR_BACKGROUND.g, GameConfig.COLOR_BACKGROUND.b, 1.0f);

        double  fpsTimer    = 0;
        int     fpsContador = 0;

        GameData.setCamaraCurrent(
                new Camera(0,"Cam1", 256, 208, 0, 0, 0, 0, GameConfig.WIN_WIDTH, GameConfig.WIN_HEIGHT)
        );


        GameData.setSceneCurrent( new sceneryDev(0, 640, 240, 16,16, "escenario dev") );

        while (!glfwWindowShouldClose(GameConfig.GetWindow())) {
            Inputs.update();
            //listCamera.getFirst().port_x ++;
            // ====================================================================
            // calculo de DeltaTime y ajustes de FPS

            double currentTime = glfwGetTime();
            float deltaTime = (float) Math.min(0.05, currentTime - lastTime);

            //Limitador de FPS
            if (deltaTime < tiempoPorFrame && !GameConfig.VSYNC) {
                try {
                    // Calculamos cuántos milisegundos faltan para completar el tiempo del frame
                    double tiempoRestante = (tiempoPorFrame - deltaTime) * 1000.0;
                    Thread.sleep((long) tiempoRestante);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Recalculamos el tiempo actual después de haber dormido
                currentTime = glfwGetTime();
                deltaTime = (float) (currentTime - lastTime);
            }

            fpsContador++; // Sumamos un fotograma procesado
            fpsTimer += deltaTime; // Acumulamos el tiempo que tardó este fotograma

            float deltaTimeLogico = Math.min(0.05f, deltaTime);

            lastTime = currentTime;


            if (fpsTimer >= 1.0) {
                String p = String.format("%s + \t| FPS: %d \t| time:  %ds", GameConfig.WIN_NAME, fpsContador, (int) lastTime);
                glfwSetWindowTitle(GameConfig.GetWindow(), p);
                //System.out.println(fpsContador);
                fpsContador = 0;
                fpsTimer = 0.0;
            }


            // Limpiar los buffers del frame anterior
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            GameData.getSceneCurrent().run(deltaTime);

            GameData.getCamaraCurrent().update();

            glfwSwapBuffers(GameConfig.GetWindow());
            glfwPollEvents();
        }

        log.LOG_INFO_SYS("La ventana se ha cerrado.");
    }

    public void run() {
        init();
        loop();
    }



    private void LimitedFPS(float deltaTime, double tiempoPorFrame, double currentTime, double lastTime){

    }


    private void esenario(double deltaTime, boolean isStart){

    }

}
