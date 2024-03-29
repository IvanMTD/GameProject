package ru.phoenix.engine.core.kernel;

import org.lwjgl.glfw.GLFWErrorCallback;
import ru.phoenix.engine.core.buffer.ubo.ProjectionUniforms;
import ru.phoenix.engine.core.buffer.ubo.UniformBufferObject;
import ru.phoenix.engine.core.buffer.util.Time;
import ru.phoenix.engine.core.configuration.ActiveButtons;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.control.Input;
import ru.phoenix.game.control.GameController;
import ru.phoenix.game.control.SceneController;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static ru.phoenix.engine.core.constants.System.NANOSECOND;

public class CoreEngine {
    private Render render;
    private UniformBufferObject uboProjection;

    private static int fps;
    private static final float framerate = 200;
    private static final float frameTime = 1.0f / framerate;
    private boolean isRunning;

    public void createWindow(WindowConfig windowConfig){
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        Window.getInstance().create(windowConfig);
        // init variable
        render = new Render();
        uboProjection = new ProjectionUniforms();
    }

    public void soundOn(){
        Sound.getInstance().soundOn();
    }

    public void init(){
        setDefaultParam();
        ActiveButtons.getInstance();
        render.init();
        uboProjection.allocate(0);
        SceneController.getInstance();
    }

    private void setDefaultParam(){
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void start(){
        if(isRunning){
            return;
        }
        run();
    }

    private void run(){
        this.isRunning = true;

        int frames = 0;
        long frameCounter = 0;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while(isRunning) {
            boolean renderAndUpdate = false;

            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double)NANOSECOND;
            frameCounter += passedTime;

            while (unprocessedTime > frameTime) {
                renderAndUpdate = true;
                unprocessedTime -= frameTime;
                if (Window.getInstance().isCloseRequested()) {
                    stop();
                }

                if (frameCounter >= NANOSECOND) {
                    setFps(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (renderAndUpdate) {
                update();
                render();
                frames++;
            }else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        cleanUp();
    }

    private void stop() {
        if(!isRunning) {
            return;
        }
        isRunning = false;
    }

    private void update() {
        Time.update();
        Window.getInstance().titleUpdate(getFps());
        Input.getInstance().update();
        GameController.getInstance().update();
        uboProjection.update();
        SceneController.getInstance().getCurrentScene().update();
    }

    private void render(){
        render.rendering(SceneController.getInstance().getCurrentScene());
    }

    private void cleanUp() {
        System.out.println("All data is clean.");
        Window.getInstance().dispose();
        Sound.getInstance().dispose();
        glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    private static void setFps(int fps) {
        CoreEngine.fps = fps;
    }
}
