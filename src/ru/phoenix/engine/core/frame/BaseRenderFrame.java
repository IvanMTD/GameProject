package ru.phoenix.engine.core.frame;

import org.lwjgl.BufferUtils;
import ru.phoenix.engine.core.buffer.fbo.FrameBufferObject;
import ru.phoenix.engine.core.buffer.fbo.MultisampleFrameBuffer;
import ru.phoenix.engine.core.buffer.fbo.OutputFrameBuffer;
import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;
import ru.phoenix.engine.core.buffer.util.Collector;
import ru.phoenix.engine.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.control.Input;
import ru.phoenix.engine.core.loader.texture.Texture;
import ru.phoenix.engine.core.loader.texture.Texture2D;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.control.Pixel;
import ru.phoenix.game.scenes.Scene;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static org.lwjgl.opengl.GL30.*;

public class BaseRenderFrame implements Framework {
    private FrameBufferObject multisample;
    private FrameBufferObject render;

    private ObjectConfiguration objectConfiguration;
    private VertexBufferObject ndcVbo;
    private Shader ndcShader;

    private boolean stop;
    private float t1;
    private float t2;
    private int firstNum;
    private int secondNum;

    private Texture firstTexture;
    private Texture secondTexture;
    private Texture[] t;

    public BaseRenderFrame(){
        multisample = new MultisampleFrameBuffer(1);
        render = new OutputFrameBuffer(1);
        ndcVbo = new VertexBufferObject();
        ndcShader = new Shader();

        stop = false;
        t1 = 0.0f;
        t2 = 0.0f;
        firstNum = 0;
        secondNum = 1;

        firstTexture = new Texture2D();
        secondTexture = new Texture2D();
        t = new Texture[4];
        for(int i=0; i<t.length; i++){
            t[i] = new Texture2D();
        }
    }

    public BaseRenderFrame(int num_of_fbo_texture){
        multisample = new MultisampleFrameBuffer(num_of_fbo_texture);
        render = new OutputFrameBuffer(num_of_fbo_texture);
        ndcVbo = new VertexBufferObject();
        ndcShader = new Shader();

        stop = false;
        t1 = 0.0f;
        t2 = 0.0f;
        firstNum = 0;
        secondNum = 1;

        firstTexture = new Texture2D();
        secondTexture = new Texture2D();
        t = new Texture[4];
        for(int i=0; i<t.length; i++){
            t[i] = new Texture2D();
        }
    }

    @Override
    public void init(){
        ndcShader.createVertexShader("VS_NDC.glsl");
        ndcShader.createFragmentShader("FS_NDC.glsl");
        ndcShader.createProgram();

        float[] position = new float[]{ // позиции на экране в НДС
                -1.0f,  1.0f, 0.0f, // левый верхний угол
                -1.0f, -1.0f, 0.0f, // левый нижний угол
                 1.0f, -1.0f, 0.0f, // правый нижний угол
                 1.0f,  1.0f, 0.0f  // правый верхний угол
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

        for(int i=0; i<t.length; i++){
            t[i].setup(Collector.getFloatHeightMap(),GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        }
        firstTexture = t[firstNum];
        secondTexture = t[secondNum];
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
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, render.getFrameBuffer());
        for(int i=0; i<multisample.getTextureSize();i++) {
            glReadBuffer(GL_COLOR_ATTACHMENT0 + i);
            glDrawBuffer(GL_COLOR_ATTACHMENT0 + i);
            glBlitFramebuffer(
                    0, 0, WindowConfig.getInstance().getWidth(), WindowConfig.getInstance().getHeight(),
                    0, 0, WindowConfig.getInstance().getWidth(), WindowConfig.getInstance().getHeight(),
                    GL_COLOR_BUFFER_BIT, GL_LINEAR
            );
        }

        glBindFramebuffer(GL_FRAMEBUFFER, render.getFrameBuffer());
        glReadBuffer(GL_COLOR_ATTACHMENT1);

        int[] viewport = new int[4];
        glGetIntegerv(GL_VIEWPORT,viewport);
        FloatBuffer data = BufferUtils.createFloatBuffer(4);
        glReadPixels(
                (int) Input.getInstance().getCursorPosition().getX(),
                viewport[3] - (int) Input.getInstance().getCursorPosition().getY(),
                1,1,GL_RGBA,GL_FLOAT,data
        );

        Vector3f pixel = new Vector3f(data.get(0),data.get(1),data.get(2));
        Pixel.setPixel(pixel);

        glBindFramebuffer(GL_FRAMEBUFFER,0);

        // ВЫВОД НА НДС ЭКРАН
        if(!stop) {
            logoUpdate();
        }
        ndcShader.useProgram();
        ndcShader.setUniform("t1",t1);
        ndcShader.setUniform("t2",t2);
        // main texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, render.getTexture(0));
        ndcShader.setUniform("main_texture",0);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, firstTexture.getTextureID());
        ndcShader.setUniform("first_texture",1);
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, secondTexture.getTextureID());
        ndcShader.setUniform("second_texture",2);
        ndcVbo.draw();
    }

    private void logoUpdate(){
        t1 += 0.005f;
        t2 += 0.0005f;
        if(t1 >= 1.0f){
            t1 = 0.0f;
            firstTexture = secondTexture;
            secondTexture = t[getNum()];
        }
        if(t2 >= 1.0f){
            stop = true;
        }
    }

    private int getNum(){
        int num = (int)((float)Math.random() * 3.5f);
        if(num == firstNum || num == secondNum){
            num = getNum();
        }
        firstNum = secondNum;
        secondNum = num;
        return num;
    }
}
