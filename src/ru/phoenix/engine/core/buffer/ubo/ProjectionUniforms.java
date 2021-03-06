package ru.phoenix.engine.core.buffer.ubo;

import ru.phoenix.engine.core.buffer.util.BufferUtil;
import ru.phoenix.engine.core.control.Camera;
import ru.phoenix.engine.math.variable.Matrix4f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL30.glBindBufferRange;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

public class ProjectionUniforms implements UniformBufferObject {
    private final int ubo;
    private int index;

    public ProjectionUniforms(){
        ubo = glGenBuffers();
    }

    @Override
    public void allocate(int index){
        this.index = index;
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferData(GL_UNIFORM_BUFFER, Matrix4f.SIZE * 3, GL_STATIC_DRAW);
        glBindBufferBase(GL_UNIFORM_BUFFER, index, ubo);
        glBindBufferRange(GL_UNIFORM_BUFFER, index, ubo, 0, Matrix4f.SIZE * 4);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, BufferUtil.createFlippedBuffer(Camera.getInstance().getPerspective().getProjection()));
        glBufferSubData(GL_UNIFORM_BUFFER, Matrix4f.SIZE, BufferUtil.createFlippedBuffer(Camera.getInstance().getOrtho().getProjection()));
        glBufferSubData(GL_UNIFORM_BUFFER, Matrix4f.SIZE * 2, BufferUtil.createFlippedBuffer(Camera.getInstance().getPerspective().getViewMatrix()));
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    @Override
    public void update(){
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferSubData(GL_UNIFORM_BUFFER, Matrix4f.SIZE * 2, BufferUtil.createFlippedBuffer(Camera.getInstance().getPerspective().getViewMatrix()));
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    @Override
    public void totalUpdate(){
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferData(GL_UNIFORM_BUFFER, Matrix4f.SIZE * 3, GL_STATIC_DRAW);
        glBindBufferBase(GL_UNIFORM_BUFFER, index, ubo);
        glBindBufferRange(GL_UNIFORM_BUFFER, index, ubo, 0, Matrix4f.SIZE * 4);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, BufferUtil.createFlippedBuffer(Camera.getInstance().getPerspective().getProjection()));
        glBufferSubData(GL_UNIFORM_BUFFER, Matrix4f.SIZE, BufferUtil.createFlippedBuffer(Camera.getInstance().getOrtho().getProjection()));
        glBufferSubData(GL_UNIFORM_BUFFER, Matrix4f.SIZE * 2, BufferUtil.createFlippedBuffer(Camera.getInstance().getPerspective().getViewMatrix()));
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    @Override
    public int getIndex(){
        return this.index;
    }
}
