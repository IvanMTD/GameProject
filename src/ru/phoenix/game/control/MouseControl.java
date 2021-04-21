package ru.phoenix.game.control;

import ru.phoenix.engine.core.configuration.ActiveButtons;

import static ru.phoenix.engine.core.constants.GameInfo.CLICK;
import static ru.phoenix.engine.core.constants.GameInfo.HOLD;

public class MouseControl {

    private boolean mouse_1_click; private boolean mouse_1_hold;
    private boolean mouse_2_click; private boolean mouse_2_hold;

    private ActionVerification mouse_1;
    private ActionVerification mouse_2;

    public MouseControl(){
        mouse_1_click = false;
        mouse_2_click = false;
        mouse_1_hold = false;
        mouse_2_hold = false;

        mouse_1 = new ActionVerification();
        mouse_2 = new ActionVerification();
    }

    public void update(){
        mouse_1_click = mouse_1.actionVerification(ActiveButtons.getInstance().getMouse_1()) == CLICK;
        mouse_1_hold = mouse_1.actionVerification(ActiveButtons.getInstance().getMouse_1()) == HOLD;
        mouse_2_click = mouse_2.actionVerification(ActiveButtons.getInstance().getMouse_2()) == CLICK;
        mouse_2_hold = mouse_2.actionVerification(ActiveButtons.getInstance().getMouse_2()) == HOLD;
    }

    public boolean isMouse_1_click() {
        return mouse_1_click;
    }

    public boolean isMouse_1_hold() {
        return mouse_1_hold;
    }

    public boolean isMouse_2_click() {
        return mouse_2_click;
    }

    public boolean isMouse_2_hold() {
        return mouse_2_hold;
    }
}
