package ru.phoenix.engine.core.control;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.kernel.Window;
import ru.phoenix.engine.math.struct.Projection;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;

public class Camera implements Externalizable {

    private static final long serialVersionUID = 1L;

    private transient static Camera instance = null;

    private Projection perspective;
    private Projection ortho;

    public Camera(){
        init();
        GLFWFramebufferSizeCallback framebufferSizeCallback = glfwSetFramebufferSizeCallback(Window.getInstance().getWindow(), (window, width, height) -> {
            if (height == 0) {
                height = 1;
            }
            updateProjection();
            Window.getInstance().setViewport(width, height);
        });
    }

    private void init(){
        perspective = new Projection();
        ortho = new Projection();
    }

    public void update(){

    }

    private void updateProjection(){

    }

    public Projection getPerspective() {
        return perspective;
    }

    public void setPerspective() {
        /*perspective.setPerspective(fov);
        perspective.setView(
                pos.getX(), pos.getY() ,pos.getZ(),
                pos.getX() + front.getX(), pos.getY() + front.getY(),pos.getZ() + front.getZ(),
                up.getX(), up.getY(), up.getZ()
        );*/
    }

    public Projection getOrtho() {
        return ortho;
    }

    public void setOrtho() {
        float left = 0.0f;
        float right = WindowConfig.getInstance().getWidth();
        float top = 0.0f;
        float bottom = WindowConfig.getInstance().getHeight();
        float near = -1.0f;
        float far = 100.0f;
        ortho.setOrtho(left,right,bottom,top,near,far);
    }

    public static Camera getInstance() {
        if(instance == null){
            instance = new Camera();
        }
        return instance;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}
