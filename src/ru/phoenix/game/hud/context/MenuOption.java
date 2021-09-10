package ru.phoenix.game.hud.context;

import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.hud.HeadUpDisplay;
import ru.phoenix.game.hud.subtype.ButtonSwitch;
import ru.phoenix.game.hud.template.HudDataset;
import ru.phoenix.game.tools.GeneratorID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.phoenix.engine.core.constants.TextureInfo.*;

public class MenuOption implements Context{

    private List<HeadUpDisplay> context;

    private HeadUpDisplay gameSwitchButton;
    private HeadUpDisplay graphicSwitchButton;
    private HeadUpDisplay soundSwitchButton;

    private Vector3f wfCenter;
    private Vector3f wfHalfRectangle;

    public MenuOption(){
        context = new ArrayList<>();

        gameSwitchButton = new ButtonSwitch();
        graphicSwitchButton = new ButtonSwitch();
        soundSwitchButton = new ButtonSwitch();
    }

    @Override
    public void init(Vector3f wfCenter, Vector3f wfHalfRectangle) {
        this.wfCenter = new Vector3f(wfCenter);
        this.wfHalfRectangle = new Vector3f(wfHalfRectangle);

        float id = GeneratorID.getInstance().generateID(GROUP_R);
        float x = wfCenter.getX() - (wfHalfRectangle.getX() * 2.0f / 3.0f);
        float y = wfCenter.getY() - (wfHalfRectangle.getY() * 0.8f);
        HudDataset dataset = new HudDataset(GROUP_R, id, 8.0f, BUTTON_ON, BUTTON_OFF, new Vector3f(x,y,0.091f));
        gameSwitchButton.init(dataset);
        gameSwitchButton.setAction(true);
        id = GeneratorID.getInstance().generateID(GROUP_R);
        x = wfCenter.getX();
        y = wfCenter.getY() - (wfHalfRectangle.getY() * 0.8f);
        dataset = new HudDataset(GROUP_R, id, 8.0f, BUTTON_ON, BUTTON_OFF, new Vector3f(x,y,0.091f));
        graphicSwitchButton.init(dataset);
        id = GeneratorID.getInstance().generateID(GROUP_R);
        x = wfCenter.getX() + (wfHalfRectangle.getX() * 2.0f / 3.0f);
        y = wfCenter.getY() - (wfHalfRectangle.getY() * 0.8f);
        dataset = new HudDataset(GROUP_R, id, 8.0f, BUTTON_ON, BUTTON_OFF, new Vector3f(x,y,0.091f));
        soundSwitchButton.init(dataset);

        context.addAll(Arrays.asList(gameSwitchButton,graphicSwitchButton,soundSwitchButton));
    }

    @Override
    public void update() {
        for(HeadUpDisplay element : context){
            element.update();
            if(element.getAction()){
                element.setAction(true);
                for(int i=0; i<context.size(); i++){
                    if(context.get(i).getId() != element.getId()){
                        context.get(i).setAction(false);
                    }
                }
            }
        }
    }

    @Override
    public void update(Vector3f direction) {
        for(HeadUpDisplay element : context){
            element.update(direction);
        }
    }

    @Override
    public void draw(Shader shader) {
        shader.setUniform("isWindowContext",1);
        shader.setUniform("wfCenter", wfCenter);
        shader.setUniform("wfHalfRectangle", wfHalfRectangle);

        for(HeadUpDisplay element : context){
            element.draw(shader);
        }

        shader.setUniform("isWindowContext",0);
    }
}
