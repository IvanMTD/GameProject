package ru.phoenix.game.hud.constructions;

import ru.phoenix.engine.core.buffer.util.Time;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.loader.text.Alphabet;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.hud.HeadUpDisplay;

import static ru.phoenix.engine.core.constants.TextureInfo.*;

public class Button {
    private HeadUpDisplay buttonUp;
    private HeadUpDisplay buttonDown;

    private Vector3f position;

    private boolean push;
    private float lastSecond;

    public Button(Vector3f position,float id){
        this.position = new Vector3f(position);
        buttonUp = new HeadUpDisplay(STATIC_BUTTON,position,GROUP_R,id);
        buttonDown = new HeadUpDisplay(STATIC_BUTTON,position,GROUP_A,0.0f);
    }

    public void init(){
        push = false;
        buttonUp.init(BUTTON_UP,15.0f);
        buttonDown.init(BUTTON_DOWN,15.0f);
    }

    public void update(){
        if(push){
            if(Time.getNonStopSecond() - lastSecond < 0.25f){

            }else{
                push = false;
            }
        }else{
           buttonUp.update();
           if(push = buttonUp.isAction()) {
               lastSecond = Time.getNonStopSecond();
               buttonUp.setAction(false);
           }
        }
    }

    public void draw(Shader shader, String text, float letterSize, float distanceBetweenLetter){
        if(push){
            buttonDown.draw(shader);
        }else{
            buttonUp.draw(shader);
        }
        drawText(shader,text,letterSize,distanceBetweenLetter);
    }

    private void drawText(Shader shader, String text, float letterSize, float distanceBetweenLetter){
        Vector3f pos = new Vector3f(position.add(new Vector3f(0.0f,0.0f,0.1f)));
        Alphabet.getInstance().drawText(text,shader,Alphabet.getInstance().LEFT_ALIGNMENT, pos, letterSize, distanceBetweenLetter);
    }
}
