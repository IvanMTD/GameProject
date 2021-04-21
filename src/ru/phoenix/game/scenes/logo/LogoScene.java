package ru.phoenix.game.scenes.logo;

import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.hud.HeadUpDisplay;
import ru.phoenix.game.hud.board.Board;
import ru.phoenix.game.hud.cursor.Cursor;
import ru.phoenix.game.scenes.Scene;

import static ru.phoenix.engine.core.constants.TextureInfo.HAND_CURSOR;
import static ru.phoenix.engine.core.constants.TextureInfo.LOGO_SCENE_MAIN_LOGO;

public class LogoScene implements Scene {
    private int sceneID;

    private Shader shaderHUD;

    public LogoScene(int sceneID){
        this.sceneID = sceneID;
        shaderHUD = new Shader();
    }

    @Override
    public void init(){
        initShader("HUD");
    }

    private void initShader(String shaderName){
        shaderHUD.createVertexShader("VS_" + shaderName + ".glsl");
        shaderHUD.createFragmentShader("FS_" + shaderName + ".glsl");
        shaderHUD.createProgram();
    }

    @Override
    public void update(){

    }

    @Override
    public void draw(){
        useShader(shaderHUD);
    }

    private void useShader(Shader shader){
        shader.useProgram();
        shader.setUniformBlock("matrices",0);
    }

    @Override
    public int getSceneID(){
        return sceneID;
    }
}
