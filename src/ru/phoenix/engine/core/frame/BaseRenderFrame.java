package ru.phoenix.engine.core.frame;

import ru.phoenix.engine.core.buffer.fbo.FrameBufferObject;
import ru.phoenix.engine.core.buffer.fbo.MultisampleFrameBuffer;
import ru.phoenix.engine.core.buffer.fbo.OutputFrameBuffer;
import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;
import ru.phoenix.engine.core.buffer.vbo.NormalizedDeviceCoordinates;
import ru.phoenix.engine.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.game.scenes.Scene;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

public class BaseRenderFrame implements Framework {
    private FrameBufferObject multisample;
    private FrameBufferObject render;

    private ObjectConfiguration objectConfiguration;
    private VertexBufferObject ndcVbo;
    private Shader ndcShader;

    public BaseRenderFrame(){
        multisample = new MultisampleFrameBuffer(1);
        render = new OutputFrameBuffer(1);
        ndcVbo = new NormalizedDeviceCoordinates();
        ndcShader = new Shader();
    }

    public BaseRenderFrame(int num_of_fbo_texture){
        multisample = new MultisampleFrameBuffer(num_of_fbo_texture);
        render = new OutputFrameBuffer(num_of_fbo_texture);
        ndcVbo = new NormalizedDeviceCoordinates();
        ndcShader = new Shader();
    }

    @Override
    public void init(){
        ndcShader.createVertexShader("VS_NDC.glsl");
        ndcShader.createFragmentShader("FS_NDC.glsl");
        ndcShader.createProgram();

        float[] position = new float[]{ // позиции на экране в НДС
                -1.0f,  1.0f, // левый верхний угол
                -1.0f, -1.0f, // левый нижний угол
                 1.0f, -1.0f, // правый нижний угол
                 1.0f,  1.0f // правый верхний угол
        };

        float[] texCoord = new float[]{ // координаты на поверхности
                0.0f,  1.0f, // левый верхний угол
                0.0f,  0.0f, // левый нижний угол
                1.0f,  0.0f, // правый нижний угол
                1.0f,  1.0f // правый верхний угол
        };

        int[] indices = new int[]{0,1,2,0,2,3}; // индексы для чтения

        objectConfiguration = new ObjectConfiguration(position,null,texCoord,null,null,null,null,null,indices);

        ndcVbo.allocate(objectConfiguration);
    }

    @Override
    public void draw(Scene scene){
        // ЗАПИСЬ В МУЛЬТИСЕМПЕЛ БУФЕР
        glBindFramebuffer(GL_FRAMEBUFFER, multisample.getFrameBuffer());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        scene.draw();

        glBindFramebuffer(GL_FRAMEBUFFER,0);

        // КОПИРОВАНИЕ ИЗ МУЛЬТИСЕМПЛА В РЕНДЕР БУФЕР
        glBindFramebuffer(GL_READ_FRAMEBUFFER, multisample.getFrameBuffer());
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, this.render.getFrameBuffer());
        for(int i=0; i<multisample.getTextureSize();i++) {
            glReadBuffer(GL_COLOR_ATTACHMENT0 + i);
            glDrawBuffer(GL_COLOR_ATTACHMENT0 + i);
            glBlitFramebuffer(
                    0, 0, WindowConfig.getInstance().getWidth(), WindowConfig.getInstance().getHeight(),
                    0, 0, WindowConfig.getInstance().getWidth(), WindowConfig.getInstance().getHeight(),
                    GL_COLOR_BUFFER_BIT, GL_LINEAR
            );
        }
        glBindFramebuffer(GL_FRAMEBUFFER,0);

        // ВЫВОД НА НДС ЭКРАН
        ndcShader.useProgram();
        // main texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, render.getTexture());
        ndcShader.setUniform("main_texture",0);
        ndcVbo.draw();
    }
}
