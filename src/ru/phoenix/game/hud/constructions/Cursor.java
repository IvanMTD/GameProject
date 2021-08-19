package ru.phoenix.game.hud.constructions;

import ru.phoenix.engine.core.control.Input;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.hud.HeadUpDisplay;

import static ru.phoenix.engine.core.constants.TextureInfo.*;

public class Cursor {
    private HeadUpDisplay handCursor;

    public Cursor(){
        handCursor = new HeadUpDisplay(CURSOR, new Vector3f(Input.getInstance().getCursorPosition(),0.0f), GROUP_A, 0.0f);
    }

    public void init(){
        handCursor.init(HAND_CURSOR,2.5f);
    }

    public void update(){
        handCursor.update();
    }

    public void draw(Shader shader){
        handCursor.draw(shader);
    }
}
