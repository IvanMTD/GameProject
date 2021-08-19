package ru.phoenix.game.scenes.logo;

import ru.phoenix.engine.core.buffer.util.Time;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.frame.management.BaseFrameManagement;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.control.SceneController;
import ru.phoenix.game.hud.HeadUpDisplay;
import ru.phoenix.game.scenes.Scene;
import ru.phoenix.game.sound.SoundManagement;

import static ru.phoenix.engine.core.constants.GameInfo.LOGO_SCENE;
import static ru.phoenix.engine.core.constants.GameInfo.MENU_SCENE;
import static ru.phoenix.engine.core.constants.SoundInfo.LOGO_SOUND;
import static ru.phoenix.engine.core.constants.TextureInfo.*;

public class LogoScene implements Scene {

    private final float LOGO_SCENE_OVER_TIME = 13.0f;
    private final float BASE_CORRECTION = 10.0f;

    private Shader shaderHUD;

    private HeadUpDisplay mainLogo;
    private HeadUpDisplay lwjglLogo;
    private SoundManagement sound;

    private boolean active;
    private boolean firstStart;

    private float lastSecond;
    private float lastCorrection;

    public LogoScene(){
        shaderHUD = new Shader();
        float x = WindowConfig.getInstance().getWidth() * 0.5f;
        float y = WindowConfig.getInstance().getHeight() * 0.5f;
        mainLogo = new HeadUpDisplay(STATIC_BOARD,new Vector3f(x,y,0.0f),GROUP_A,0.0f);
        x = WindowConfig.getInstance().getWidth() * 0.085f;
        y = WindowConfig.getInstance().getHeight() * 0.96f;
        lwjglLogo = new HeadUpDisplay(STATIC_BOARD, new Vector3f(x,y,0.0f),GROUP_A,0.0f);
        sound = new SoundManagement();
    }

    @Override
    public void init(boolean active){
        this.active = active;
        firstStart = true;
        lastSecond = 0.0f;
        initShader("HUD");
        initializeVariable();
    }

    private void initShader(String shaderName){
        shaderHUD.createVertexShader("VS_" + shaderName + ".glsl");
        shaderHUD.createFragmentShader("FS_" + shaderName + ".glsl");
        shaderHUD.createProgram();
    }

    private void initializeVariable(){
        mainLogo.init(LOGO_SCENE_MAIN_LOGO, 20.0f);
        lwjglLogo.init(LOGO_SCENE_LWJGL_LOGO,15.0f);
        sound.init(LOGO_SOUND);
    }

    @Override
    public void update(){
        if(firstStart){
            lastSecond = Time.getNonStopSecond();
            lastCorrection = 0.0f;
            BaseFrameManagement.setTune(0.0f);
            BaseFrameManagement.setKernel(BaseFrameManagement.KERNEL_BLR);
            BaseFrameManagement.setCorrection(BASE_CORRECTION);
            firstStart = false;
            sound.playSound();
        }else{
            float difference = Time.getNonStopSecond() - lastSecond;

            float timeInterval = 5.0f;
            float delta = LOGO_SCENE_OVER_TIME - timeInterval * 2.0f;
            float blurTune = 800.0f;

            if(difference <= timeInterval){
                float offset = (difference / timeInterval) * (difference / timeInterval);
                BaseFrameManagement.setTune(offset);
                BaseFrameManagement.setCorrection(BASE_CORRECTION + (offset * blurTune));
            }else if(timeInterval < difference && difference < (timeInterval + delta)){
                BaseFrameManagement.setTune(1.0f);
                lastCorrection = BaseFrameManagement.getCorrection();
            }else if((timeInterval + delta) <= difference && difference < LOGO_SCENE_OVER_TIME){
                float offset = ((difference - (timeInterval + delta)) / timeInterval) * ((difference - (timeInterval + delta)) / timeInterval);
                BaseFrameManagement.setTune(1.0f - offset);
                BaseFrameManagement.setCorrection(lastCorrection - (offset * blurTune));
            }else{
                sound.deleteSource();
                BaseFrameManagement.setTune(0.0f);
                BaseFrameManagement.setKernel(BaseFrameManagement.KERNEL_STD);
                BaseFrameManagement.setCorrection(300.0f);
                SceneController.getInstance().activateScene(MENU_SCENE);
            }
        }
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
        mainLogo.draw(shaderHUD);
        lwjglLogo.draw(shaderHUD);
    }

    private void useShader(Shader shader){
        shader.useProgram();
        shader.setUniformBlock("matrices",0);
    }

    @Override
    public int getSceneID(){
        return LOGO_SCENE;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
