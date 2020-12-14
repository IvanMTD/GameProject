package ru.phoenix.engine.core.kernel;

import org.lwjgl.glfw.GLFWErrorCallback;
import ru.phoenix.engine.core.buffer.ubo.ProjectionUniforms;
import ru.phoenix.engine.core.buffer.ubo.UniformBufferObject;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.control.Input;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static ru.phoenix.engine.core.constants.System.NANOSECOND;

public class MainLoop {

    private Render render;
    private UniformBufferObject uboProjection;

    private int fps;
    private static final float framerate = 200;
    private static final float frameTime = 1.0f / framerate;
    private boolean isRunning;

    public void createWindow(WindowConfig windowConfig){
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        Window.getInstance().create(windowConfig);
        // init vatiable
        render = new Render();
        uboProjection = new ProjectionUniforms();
    }

    public void init(){
        setDefaultParam();
        render.init();
        uboProjection.allocate(0);
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
            boolean render = false;

            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double)NANOSECOND;
            frameCounter += passedTime;

            while (unprocessedTime > frameTime) {
                render = true;
                unprocessedTime -= frameTime;
                if (Window.getInstance().isCloseRequested()) {
                    stop();
                }
                update();
                if (frameCounter >= NANOSECOND) {
                    setFps(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render) {
                render();
                frames++;
            } else {
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
        Window.getInstance().titleUpdate(getFps());
        Input.getInstance().update();
    }

    private void render(){
        render.rendering();
    }

    private void cleanUp() {
        Window.getInstance().dispose();
        glfwTerminate();
    }

    public int getFps() {
        return fps;
    }

    private void setFps(int fps) {
        this.fps = fps;
    }
}