package ru.phoenix.game.control;

public class GameController {
    private static GameController instance = null;

    private MouseControl mouseControl;

    public GameController() {
        mouseControl = new MouseControl();
    }

    public void update(){
        mouseControl.update();
    }

    public MouseControl getMouseControl(){
        return mouseControl;
    }

    public static GameController getInstance() {
        if(instance == null){
            instance = new GameController();
        }
        return instance;
    }
}
