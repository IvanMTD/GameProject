package ru.phoenix.game.scenes.logo;

import ru.phoenix.engine.core.buffer.util.Collector;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.loader.texture.Texture;
import ru.phoenix.engine.core.loader.texture.Texture2D;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.hud.HeadUpDisplay;
import ru.phoenix.game.scenes.Scene;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.engine.core.constants.TextureInfo.*;

public class LogoScene implements Scene {
    private int sceneID;

    private Shader shaderHUD;

    private HeadUpDisplay logo;

    private boolean active;

    public LogoScene(int sceneID){
        this.sceneID = sceneID;
        shaderHUD = new Shader();
        float x = WindowConfig.getInstance().getWidth() / 2.0f;
        float y = WindowConfig.getInstance().getHeight() / 2.0f;
        logo = new HeadUpDisplay(STATIC_BOARD,new Vector3f(x,y,0.0f),GROUP_A,0.0f);
    }

    @Override
    public void init(boolean active){
        this.active = active;
        initShader("HUD");
        initializeVariable();
    }

    private void initShader(String shaderName){
        shaderHUD.createVertexShader("VS_" + shaderName + ".glsl");
        shaderHUD.createFragmentShader("FS_" + shaderName + ".glsl");
        shaderHUD.createProgram();
    }

    private void initializeVariable(){
        logo.init(LOGO_SCENE_MAIN_LOGO);
    }

    @Override
    public void update(){

    }

    @Override
    public void run() {
        active = true;
    }

    @Override
    public void stop() {
        active = false;
    }

    @Override
    public void draw(){
        useShader(shaderHUD);
        logo.draw(shaderHUD);
    }

    private void useShader(Shader shader){
        shader.useProgram();
        shader.setUniformBlock("matrices",0);
    }

    @Override
    public int getSceneID(){
        return sceneID;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
