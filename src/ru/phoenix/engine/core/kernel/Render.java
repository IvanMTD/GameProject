package ru.phoenix.engine.core.kernel;

import ru.phoenix.engine.core.frame.BaseRenderFrame;
import ru.phoenix.engine.core.frame.Framework;
import ru.phoenix.game.scenes.Scene;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;

public class Render {

    private Framework baseRenderFrame;

    public Render(){
        baseRenderFrame = new BaseRenderFrame();
    }

    public void init(){
        baseRenderFrame.init();
    }

    public void rendering(Scene scene){
        clearScreen();
        baseRenderFrame.draw(scene);
        Window.getInstance().render();
    }

    private void clearScreen(){
        glClearColor(1.0f,1.0f,1.0f,1.0f);
        glClearDepth(1.0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }
}