package ru.phoenix.game.hud.constructions;

import ru.phoenix.engine.core.buffer.util.Time;
import ru.phoenix.engine.core.control.Input;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.control.GameController;
import ru.phoenix.game.hud.HeadUpDisplay;
import ru.phoenix.game.hud.subtype.Cursor;
import ru.phoenix.game.hud.template.HudDataset;

import static ru.phoenix.engine.core.constants.TextureInfo.*;

public class GameCursor {
    private HeadUpDisplay handCursor1;
    private HeadUpDisplay handCursor2;

    private float lastSecond;

    public GameCursor(){
        handCursor1 = new Cursor();
        handCursor2 = new Cursor();
    }

    public void init(){
        handCursor1.init(new HudDataset(2.5f, HAND_CURSOR_1,new Vector3f(Input.getInstance().getCursorPosition(),1.0f)));
        handCursor2.init(new HudDataset(2.5f, HAND_CURSOR_2,new Vector3f(Input.getInstance().getCursorPosition(),1.0f)));
    }

    public void update(){
        handCursor1.update();
        handCursor2.update();
    }

    public void draw(Shader shader){
        if (GameController.getInstance().getMouseControl().isMouse_1_click() || GameController.getInstance().getMouseControl().isMouse_1_hold()) {
            handCursor1.draw(shader);
            lastSecond = Time.getNonStopSecond();
        } else {
            if(Time.getNonStopSecond() - lastSecond < 0.025f){
                handCursor1.draw(shader);
            }else {
                handCursor2.draw(shader);
            }
        }
    }
}
