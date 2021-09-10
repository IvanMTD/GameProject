package ru.phoenix.game.hud.constructions;

import ru.phoenix.engine.core.buffer.util.Collector;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.control.Input;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector2f;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.control.GameController;
import ru.phoenix.game.control.Pixel;
import ru.phoenix.game.hud.HeadUpDisplay;
import ru.phoenix.game.hud.context.Context;
import ru.phoenix.game.hud.subtype.Board;
import ru.phoenix.game.hud.subtype.Button;
import ru.phoenix.game.hud.template.HudDataset;
import ru.phoenix.game.tools.GeneratorID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.phoenix.engine.core.constants.TextureInfo.*;

public class GameWindow {
    private List<HeadUpDisplay> windowInterface;
    private HeadUpDisplay windowHeader;
    private HeadUpDisplay windowFrame;
    private HeadUpDisplay windowField;
    private HeadUpDisplay windowOffButton;

    private Context context;

    private float lastHeaderId;
    private Vector2f lastCursorPos;
    private Vector2f windowRectangle;
    private Vector3f windowCenter;
    private Vector3f workingFieldCenter;
    private Vector2f workingFieldSize;

    private boolean closeWindow;

    public GameWindow(){
        windowInterface = new ArrayList<>();
        windowHeader = new Board();
        windowFrame = new Board();
        windowField = new Board();
        windowOffButton = new Button();

        lastCursorPos = new Vector2f();
    }

    public void init(Context context){
        float id = GeneratorID.getInstance().generateID(GROUP_R);
        float x = WindowConfig.getInstance().getWidth() * 0.5f;
        float y = WindowConfig.getInstance().getHeight() * 0.5f;
        HudDataset dataset = new HudDataset(GROUP_R,id,30.0f,WINDOW_HEADER,new Vector3f(x,y,0.1f));
        windowHeader.init(dataset);
        dataset = new HudDataset(GROUP_A,0.0f,30.0f,WINDOW_FRAME,new Vector3f(x,y,0.1f));
        windowFrame.init(dataset);
        windowRectangle = new Vector2f(Collector.getW() / 2.0f, Collector.getH() / 2.0f);
        windowCenter = new Vector3f(x,y,0.0f);
        x = windowCenter.getX() - (windowRectangle.getX() * 0.0547f);
        y = windowCenter.getY() + (windowRectangle.getY() * 0.2f);
        dataset = new HudDataset(GROUP_A,0.0f,28.0f,WINDOW_FIELD,new Vector3f(x,y,0.09f));
        windowField.init(dataset);
        workingFieldCenter = new Vector3f(x,y,0.0f);
        workingFieldSize = new Vector2f((Collector.getW() * 0.9f) / 2.0f,(Collector.getH() * 0.9f) / 2.0f);
        id = GeneratorID.getInstance().generateID(GROUP_R);
        x = windowCenter.getX() + (windowRectangle.getX() * 0.82f);
        y = windowCenter.getY() - (windowRectangle.getY() * 0.8f);
        dataset = new HudDataset(GROUP_R,id,4.09f,WINDOW_OFF_BUTTON,new Vector3f(x,y,0.11f));
        windowOffButton.init(dataset);

        windowInterface.addAll(Arrays.asList(windowHeader,windowFrame,windowField, windowOffButton));

        lastHeaderId = 0.0f;
        lastCursorPos = Input.getInstance().getCursorPosition();

        context.init(workingFieldCenter,new Vector3f(workingFieldSize,0.0f));
        this.context = context;

        closeWindow = false;
    }

    public void update(){
        if(GameController.getInstance().getMouseControl().isMouse_1_hold()){
            if(lastHeaderId == windowHeader.getId()){
                if(Input.getInstance().isCursorMove()) {
                    Vector3f direction = new Vector3f(Input.getInstance().getCursorPosition().sub(lastCursorPos), 0.0f);
                    Vector3f c = windowCenter.add(direction);
                    float minX = c.getX() - windowRectangle.getX();
                    float maxX = c.getX() + windowRectangle.getX();
                    float minY = c.getY() - windowRectangle.getY();
                    float maxY = c.getY() + windowRectangle.getY();
                    if(0 < minX && maxX < WindowConfig.getInstance().getWidth() && 0 < minY && maxY < WindowConfig.getInstance().getHeight()) {
                        windowCenter = new Vector3f(c);
                        workingFieldCenter = workingFieldCenter.add(direction);
                        for (HeadUpDisplay windowElement : windowInterface) {
                            windowElement.update(direction);
                        }
                        context.update(direction);
                    }else{
                        lastHeaderId = 0.0f;
                    }
                }
            }
        }else{
            lastHeaderId = Pixel.getPixel().getR();
        }

        for(HeadUpDisplay windowElement : windowInterface){
            windowElement.update();
            if(windowElement.getAction()){
                if(windowElement.getId() == windowOffButton.getId()){
                    closeWindow = true;
                }
            }
        }
        context.update();

        lastCursorPos = new Vector2f(Input.getInstance().getCursorPosition());
    }

    public boolean isCloseWindow(){
        boolean closeWindow = this.closeWindow;
        this.closeWindow = false;
        return closeWindow;
    }

    public void draw(Shader shader){
        for(HeadUpDisplay windowElement : windowInterface){
            windowElement.draw(shader);
        }
        context.draw(shader);
    }
}
