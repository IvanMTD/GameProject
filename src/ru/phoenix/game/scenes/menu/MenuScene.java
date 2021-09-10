package ru.phoenix.game.scenes.menu;

import ru.phoenix.engine.core.buffer.util.Collector;
import ru.phoenix.engine.core.buffer.util.Time;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.frame.management.BaseFrameManagement;
import ru.phoenix.engine.core.kernel.Window;
import ru.phoenix.engine.core.loader.text.Alphabet;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.hud.HeadUpDisplay;
import ru.phoenix.game.hud.constructions.GameCursor;
import ru.phoenix.game.hud.constructions.GameWindow;
import ru.phoenix.game.hud.context.Context;
import ru.phoenix.game.hud.context.MenuOption;
import ru.phoenix.game.hud.subtype.Board;
import ru.phoenix.game.hud.subtype.Button;
import ru.phoenix.game.hud.template.HudDataset;
import ru.phoenix.game.scenes.Scene;
import ru.phoenix.game.sound.SoundManagement;
import ru.phoenix.game.tools.GeneratorID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static ru.phoenix.engine.core.constants.GameInfo.MENU_SCENE;
import static ru.phoenix.engine.core.constants.SoundInfo.MAIN_THEME_SOUND;
import static ru.phoenix.engine.core.constants.System.ENGLISH_LANGUAGE;
import static ru.phoenix.engine.core.constants.System.RUSSIAN_LANGUAGE;
import static ru.phoenix.engine.core.constants.TextureInfo.*;

public class MenuScene implements Scene {

    private final int OPEN_SCENE            = 0x01;
    private final int CLOSE_SCENE           = 0x02;
    private final int SCENE                 = 0x03;
    private final int SCENE_OPTION          = 0x04;
    private int sceneControl;

    private Shader shaderHUD;
    private SoundManagement mainThemeSound;
    private GameCursor gameCursor;

    private List<HeadUpDisplay> buttons;
    private HeadUpDisplay playButton;
    private HeadUpDisplay optionButton;
    private HeadUpDisplay quitButton;

    private GameWindow optionWindow;

    private boolean active;
    private boolean firstStart;

    private float lastSecond;



    public MenuScene(){
        shaderHUD = new Shader();
        mainThemeSound = new SoundManagement();
        gameCursor = new GameCursor();
        buttons = new ArrayList<>();
        playButton = new Button();
        optionButton = new Button();
        quitButton = new Button();

        optionWindow = new GameWindow();
    }

    @Override
    public void init(boolean active) {
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
        sceneControl = OPEN_SCENE;
        this.firstStart = true;
        lastSecond = 0.0f;
        mainThemeSound.init(MAIN_THEME_SOUND,AL_TRUE);
        gameCursor.init();
        initButtons();
        optionWindow.init(new MenuOption());
    }

    private void initButtons(){
        float id = GeneratorID.getInstance().generateID(GROUP_R);
        float x = WindowConfig.getInstance().getWidth() - 50.0f;
        float y = WindowConfig.getInstance().getHeight() - 50.0f;
        HudDataset dataset = new HudDataset(GROUP_R, id, 3.0f, BUTTON_QUIT,new Vector3f(x,y,0.0f));
        quitButton.init(dataset);
        id = GeneratorID.getInstance().generateID(GROUP_R);
        y = y - (Collector.getH() * 1.5f);
        dataset = new HudDataset(GROUP_R, id, 3.0f, BUTTON_OPTION,new Vector3f(x,y,0.0f));
        optionButton.init(dataset);
        id = GeneratorID.getInstance().generateID(GROUP_R);
        y = y - (Collector.getH() * 1.5f);
        dataset = new HudDataset(GROUP_R, id, 3.0f, BUTTON_PLAY,new Vector3f(x,y,0.0f));
        playButton.init(dataset);
        buttons.addAll(Arrays.asList(playButton,optionButton,quitButton));
    }

    @Override
    public void update() {
        gameCursor.update();
        float timeInterval = 4.0f;
        if(firstStart){
            //mainThemeSound.playSound();
            firstStart = false;
            lastSecond = Time.getNonStopSecond();
        }else{
            if(sceneControl == OPEN_SCENE){
                if(openScene(timeInterval)){
                    sceneControl = SCENE;
                }
            }else if(sceneControl == SCENE){
                for(HeadUpDisplay button : buttons){
                    button.update();
                    if(button.getAction()){
                        if(button.getId() == optionButton.getId()){
                            sceneControl = SCENE_OPTION;
                        }else if(button.getId() == quitButton.getId()){
                            sceneControl = CLOSE_SCENE;
                            lastSecond = Time.getNonStopSecond();
                            break;
                        }
                    }
                }
            }else if(sceneControl == SCENE_OPTION){
                optionWindow.update();
                if(optionWindow.isCloseWindow()){
                    sceneControl = SCENE;
                }
            }else if(sceneControl == CLOSE_SCENE){
                if(closeScene(timeInterval / 4.0f)){
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
        gameCursor.draw(shaderHUD);
        for(HeadUpDisplay button : buttons){
            button.draw(shaderHUD);
        }
        drawText();
        if(sceneControl == SCENE_OPTION){
            optionWindow.draw(shaderHUD);
        }
    }

    private void drawText(){
        float x = 20.0f;
        float y = WindowConfig.getInstance().getHeight() - 20.0f;
        Vector3f pos = new Vector3f(x,y,0.0f);
        float laterSize = WindowConfig.getInstance().getWidth() * 0.0001f;
        float distance = Alphabet.getInstance().getLetterOffset() * (laterSize - laterSize * 0.12f);
        if(WindowConfig.getInstance().getLanguage() == RUSSIAN_LANGUAGE){
            Alphabet.getInstance().drawText("Версия 1.0а",shaderHUD,Alphabet.getInstance().LEFT_ALIGNMENT,pos,laterSize,distance);
        }else if(WindowConfig.getInstance().getLanguage() == ENGLISH_LANGUAGE){
            Alphabet.getInstance().drawText("Version 1.0a",shaderHUD,Alphabet.getInstance().LEFT_ALIGNMENT,pos,laterSize,distance);
        }
    }

    private void useShader(Shader shader){
        shader.useProgram();
        shader.setUniformBlock("matrices",0);
        shader.setUniform("isWindowContext",0);
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
