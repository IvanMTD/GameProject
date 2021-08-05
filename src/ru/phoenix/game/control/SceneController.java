package ru.phoenix.game.control;

import ru.phoenix.game.scenes.Scene;
import ru.phoenix.game.scenes.logo.LogoScene;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.engine.core.constants.GameInfo.LOGO_SCENE;

public class SceneController {

    private static SceneController instance = null;

    private List<Scene> scenes;

    private Scene logoScene;

    public SceneController(){
        scenes = new ArrayList<>();
        logoScene = new LogoScene(LOGO_SCENE);
        logoScene.init(true);
        scenes.add(logoScene);
    }

    public Scene getCurrentScene(){
        Scene currentScene = null;
        for(Scene scene : scenes){
            if(scene.isActive()){
                currentScene = scene;
                break;
            }
        }
        return currentScene;
    }

    public void activateScene(int indicator){
        for(Scene scene : scenes){
            scene.stop();
            if(scene.getSceneID() == indicator){
                scene.run();
            }
        }
    }

    public static SceneController getInstance(){
        if(instance == null){
            instance = new SceneController();
        }
        return instance;
    }
}
