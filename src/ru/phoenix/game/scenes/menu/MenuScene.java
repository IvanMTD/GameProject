package ru.phoenix.game.scenes.menu;

import ru.phoenix.engine.core.buffer.util.Time;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.frame.management.BaseFrameManagement;
import ru.phoenix.engine.core.kernel.Window;
import ru.phoenix.engine.core.loader.text.Alphabet;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.hud.constructions.Button;
import ru.phoenix.game.hud.constructions.Cursor;
import ru.phoenix.game.scenes.Scene;
import ru.phoenix.game.sound.SoundManagement;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static ru.phoenix.engine.core.constants.GameInfo.MENU_SCENE;
import static ru.phoenix.engine.core.constants.SoundInfo.MAIN_THEME_SOUND;
import static ru.phoenix.engine.core.constants.System.ENGLISH_LANGUAGE;
import static ru.phoenix.engine.core.constants.System.RUSSIAN_LANGUAGE;

public class MenuScene implements Scene {

    private final int OPEN_SCENE = 1;
    private final int CLOSE_SCENE = 2;
    private final int SCENE = 3;
    private int sceneControl;

    private Shader shaderHUD;
    private SoundManagement mainThemeSound;
    private Cursor cursor;
    private Button quitButton;

    private boolean active;
    private boolean firstStart;

    private float lastSecond;



    public MenuScene(){
        sceneControl = OPEN_SCENE;
        shaderHUD = new Shader();
        mainThemeSound = new SoundManagement();
        cursor = new Cursor();
        float x = WindowConfig.getInstance().getWidth() * 0.9f;
        float y = WindowConfig.getInstance().getHeight() * 0.89f;
        quitButton = new Button(new Vector3f(x,y,0.0f),0.1f);
    }

    @Override
    public void init(boolean active) {
        this.active = active;
        this.firstStart = true;
        initShader("HUD");
        initializeVariable();
    }

    private void initShader(String shaderName){
        shaderHUD.createVertexShader("VS_" + shaderName + ".glsl");
        shaderHUD.createFragmentShader("FS_" + shaderName + ".glsl");
        shaderHUD.createProgram();
    }

    private void initializeVariable(){
        lastSecond = 0.0f;
        mainThemeSound.init(MAIN_THEME_SOUND);
        cursor.init();
        quitButton.init();
    }

    @Override
    public void update() {
        cursor.update();
        float timeInterval = 8.0f;
        if(firstStart){
            mainThemeSound.playSound();
            firstStart = false;
            lastSecond = Time.getNonStopSecond();
        }else{
            if(sceneControl == OPEN_SCENE){
                if(openScene(timeInterval)){
                    sceneControl = SCENE;
                }
            }else if(sceneControl == SCENE){
                quitButton.update();
            }else if(sceneControl == CLOSE_SCENE){
                if(closeScene(timeInterval)){
                    glfwSetWindowShouldClose(Window.getInstance().getWindow(), true);
                }
            }
        }
    }

    private boolean openScene(float timeInterval){
        boolean open = false;

        float difference = Time.getNonStopSecond() - lastSecond;

        if(difference <= timeInterval){
            float offset = (difference / timeInterval) * (difference / timeInterval);
            BaseFrameManagement.setTune(offset);
        }else{
            open = true;
        }

        return open;
    }

    private boolean closeScene(float timeInterval){
        boolean close = false;

        float difference = Time.getNonStopSecond() - lastSecond;

        if(difference <= timeInterval){
            float offset = (difference / timeInterval) * (difference / timeInterval);
            BaseFrameManagement.setTune(1.0f - offset);
        }else{
            close = true;
        }

        return close;
    }

    @Override
    public void run() {
        this.active = true;
    }

    @Override
    public void stop() {
        this.active = false;
    }

    @Override
    public void draw() {
        useShader(shaderHUD);
        cursor.draw(shaderHUD);
        float laterSize = WindowConfig.getInstance().getWidth() * 0.0001f;
        float distance = Alphabet.getInstance().getLetterOffset() * (laterSize - laterSize * 0.12f);
        quitButton.draw(shaderHUD,"Quit", laterSize,distance);
        drawText();
    }

    private void drawText(){
        float x = WindowConfig.getInstance().getWidth() * 0.01f;
        float y = WindowConfig.getInstance().getHeight() * 0.98f;
        Vector3f pos = new Vector3f(x,y,0.0f);
        float laterSize = WindowConfig.getInstance().getWidth() * 0.0001f;
        float distance = Alphabet.getInstance().getLetterOffset() * (laterSize - laterSize * 0.12f);
        if(WindowConfig.getInstance().getLanguage() == RUSSIAN_LANGUAGE){
            Alphabet.getInstance().drawText("версия 1.0а",shaderHUD,Alphabet.getInstance().LEFT_ALIGNMENT,pos,laterSize,distance);
        }else if(WindowConfig.getInstance().getLanguage() == ENGLISH_LANGUAGE){
            Alphabet.getInstance().drawText("version 1.0a",shaderHUD,Alphabet.getInstance().LEFT_ALIGNMENT,pos,laterSize,distance);
        }
    }

    private void useShader(Shader shader){
        shader.useProgram();
        shader.setUniformBlock("matrices",0);
    }

    @Override
    public int getSceneID() {
        return MENU_SCENE;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
