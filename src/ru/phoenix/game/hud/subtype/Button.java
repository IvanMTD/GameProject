package ru.phoenix.game.hud.subtype;

import ru.phoenix.engine.core.buffer.util.Collector;
import ru.phoenix.engine.core.buffer.util.Time;
import ru.phoenix.engine.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.engine.core.loader.texture.Texture;
import ru.phoenix.engine.core.loader.texture.Texture2D;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.struct.Projection;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.control.GameController;
import ru.phoenix.game.control.Pixel;
import ru.phoenix.game.hud.HeadUpDisplay;
import ru.phoenix.game.hud.template.HudDataset;
import ru.phoenix.game.sound.SoundManagement;

import static org.lwjgl.openal.AL10.AL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.engine.core.constants.SoundInfo.SOUND_BUTTON;
import static ru.phoenix.engine.core.constants.TextureInfo.*;

public class Button implements HeadUpDisplay {
    private int group;
    private float id;
    private boolean target;
    private boolean action;
    private boolean animation;
    private boolean posChange;
    private float lastSecond;

    private Texture texture;
    private VertexBufferObject vbo;
    private Projection projection;
    private Vector3f position;

    private SoundManagement clickSound;

    public Button(){
        texture = new Texture2D();
        vbo = new VertexBufferObject();
        projection = new Projection();
        position = new Vector3f();
        clickSound = new SoundManagement();
    }

    @Override
    public void init(HudDataset dataset){
        this.group = dataset.getGroup();
        this.id = dataset.getId();
        this.target = false;
        this.action = false;
        this.animation = false;
        this.posChange = false;
        this.lastSecond = 0.0f;

        texture.setup(dataset.getTexturePath(),GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        vbo.allocate(Collector.collectVertexBufferCenter(texture.getWidth(),texture.getHeight(),dataset.getPercentOfWindow()));
        position = dataset.getPosition();
        clickSound.init(SOUND_BUTTON,AL_FALSE);

        setDefaultProjection();
    }

    private void setDefaultProjection(){
        projection.getModelMatrix().identity();
        projection.setTranslation(this.position);
        projection.setScaling(1.0f);
    }

    @Override
    public void update(){
        setNewPosition();
        actionControl();
    }

    private void setNewPosition(){
        if(posChange){
            posChange = false;
            setDefaultProjection();
        }
    }

    private void actionControl(){
        if(animation){
            if(Time.getNonStopSecond() - lastSecond > 0.25f){
                setDefaultProjection();
                animation = false;
                action = true;
            }
        }else {
            target = findId() == id;
            if (target) {
                animation = GameController.getInstance().getMouseControl().isMouse_1_click();
                if(animation) {
                    clickSound.playSound();
                    lastSecond = Time.getNonStopSecond();
                    projection.getModelMatrix().identity();
                    projection.setTranslation(this.position);
                    projection.setScaling(0.9f);
                }
            }
        }
    }

    private float findId(){
        float id;
        if(group == GROUP_R){
            id = Pixel.getPixel().getR();
        }else if(group == GROUP_G){
            id = Pixel.getPixel().getG();
        }else if(group == GROUP_B){
            id = Pixel.getPixel().getB();
        }else{
            id = 0.0f;
        }
        return id;
    }

    @Override
    public void update(Vector3f direction) {
        this.position = this.position.add(direction);
        posChange = true;
    }

    @Override
    public boolean getAction(){
        boolean action = this.action;
        this.action = false;
        return action;
    }

    @Override
    public void setAction(boolean action) {

    }

    @Override
    public float getId() {
        return id;
    }

    @Override
    public void draw(Shader shader) {
        setUniforms(shader);
        vbo.draw();
    }

    private void setUniforms(Shader shader){
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
        shader.setUniform("model_m",projection.getModelMatrix());
        shader.setUniform("group", group);
        shader.setUniform("id", id);
    }
}
