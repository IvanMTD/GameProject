package ru.phoenix.game.control;

import ru.phoenix.engine.core.control.Input;
import ru.phoenix.engine.core.kernel.CoreEngine;

import static ru.phoenix.engine.core.constants.GameInfo.*;

public class ActionVerification {

    private int counter;
    private int action;
    private boolean click;
    private int clickRate;

    public ActionVerification(){
        counter = 0;
        clickRate = 0;
        click = false;
        action = NO_ACTION;
    }

    public int actionVerification (int key){
        clickRate = CoreEngine.getFps() / 2;

        if (Input.getInstance().isMousePressed(key)) {
            counter++;
            if(!click) {
                click = true;
                action = HOLD;
            }
        }else if(!Input.getInstance().isMousePressed(key)){
            action = NO_ACTION;
            if(click) {
                if (counter < clickRate) {
                    action = CLICK;
                }
                counter = 0;
                click = false;
            }
        }

        return action;
    }
}
