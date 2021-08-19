package ru.phoenix.game.control;

import ru.phoenix.game.scenes.Scene;
import ru.phoenix.game.scenes.logo.LogoScene;
import ru.phoenix.game.scenes.menu.MenuScene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SceneController {

    private static SceneController instance = null;

    private List<Scene> scenes;

    private Scene logoScene;
    private Scene menuScene;

    public SceneController(){
        scenes = new ArrayList<>();
        logoScene = new LogoScene();
        logoScene.init(false);
        menuScene = new MenuScene();
        menuScene.init(true);
        scenes.addAll(Arrays.asList(logoScene,menuScene));
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
