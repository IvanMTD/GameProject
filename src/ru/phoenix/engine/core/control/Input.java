package ru.phoenix.engine.core.control;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import ru.phoenix.engine.core.kernel.Window;
import ru.phoenix.engine.math.variable.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

public class Input {
    private static Input instance = null;

    private boolean[] keys;
    private boolean[] buttons;

    private boolean cursorMove;
    private Vector2f cursorPosition;
    private float scrollOffset;

    private Input(){
        init();
        glfwSetKeyCallback(Window.getInstance().getWindow(), new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if(key >= 0) {
                    if (action == GLFW_PRESS) {
                        keys[key] = true;
                    } else if (action == GLFW_RELEASE) {
                        keys[key] = false;
                    }
                }
            }
        });

        glfwSetMouseButtonCallback(Window.getInstance().getWindow(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW_PRESS) {
                    buttons[button] = true;
                } else if (action == GLFW_RELEASE) {
                    buttons[button] = false;
                }
            }
        });

        glfwSetCursorPosCallback(Window.getInstance().getWindow(), new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                cursorPosition.setX((float) xpos);
                cursorPosition.setY((float) ypos);
                setCursorMove(true);
            }
        });

        glfwSetScrollCallback(Window.getInstance().getWindow(), new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                setScrollOffset((float) yoffset);
            }
        });
    }

    private void init(){
        cursorPosition = new Vector2f();
        scrollOffset = 0.0f;
        keys = new boolean[1024];
        buttons = new boolean[10];
        glfwSetInputMode(Window.getInstance().getWindow(),GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    }

    public void update(){
        setCursorMove(false);
        setScrollOffset(0.0f);
        glfwPollEvents();
    }

    public boolean isPressed(int index){
        return keys[index];
    }

    public boolean isMousePressed(int index){
        return buttons[index];
    }

    public void setCursorMove(boolean value){
        cursorMove = value;
    }

    public boolean isCursorMove(){
        return cursorMove;
    }

    public Vector2f getCursorPosition(){
        return cursorPosition;
    }

    public float getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(float scroolOffset) {
        this.scrollOffset = scroolOffset;
    }

    public static Input getInstance(){
        if(instance == null){
            instance = new Input();
        }
        return instance;
    }
}
