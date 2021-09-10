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

public class ButtonSwitch implements HeadUpDisplay {

    private int group;
    private float id;
    private boolean target;
    private boolean action;
    private boolean posChange;

    private Texture texture1;
    private Texture texture2;
    private VertexBufferObject vbo;
    private Projection projection;
    private Vector3f position;

    private SoundManagement clickSound;

    public ButtonSwitch(){
        texture1 = new Texture2D();
        texture2 = new Texture2D();
        vbo = new VertexBufferObject();
        projection = new Projection();
        position = new Vector3f();
        clickSound = new SoundManagement();
    }

    @Override
    public void init(HudDataset dataset) {
        group = dataset.getGroup();
        id = dataset.getId();
        target = false;
        action = false;
        posChange = false;

        texture1.setup(dataset.getTexturePath(), GL_SRGB_ALPHA, GL_CLAMP_TO_EDGE);
        texture2.setup(dataset.getTexturePath2(), GL_SRGB_ALPHA, GL_CLAMP_TO_EDGE);
        vbo.allocate(Collector.collectVertexBufferCenter(texture1.getWidth(),texture1.getHeight(),dataset.getPercentOfWindow()));
        this.position = dataset.getPosition();
        setDefaultProjection();

        clickSound.init(SOUND_BUTTON,AL_FALSE);
    }

    private void setDefaultProjection(){
        projection.getModelMatrix().identity();
        projection.setTranslation(this.position);
        projection.setScaling(1.0f);
    }

    @Override
    public void update() {
        setNewPosition();
        if(!action) {
            actionControl();
        }
    }

    private void setNewPosition(){
        if(posChange){
            posChange = false;
            setDefaultProjection();
        }
    }

    private void actionControl(){
        target = findId() == id;
        if (target) {
            action = GameController.getInstance().getMouseControl().isMouse_1_click();
            if(action){
                clickSound.playSound();
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
    public boolean getAction() {
        return this.action;
    }

    @Override
    public void setAction(boolean action) {
        this.action = action;
    }

    @Override
    public float getId() {
        return id;
    }

    @Override
    public void draw(Shader shader) {
        if(action){
            setUniforms(shader, texture1);
        }else {
            setUniforms(shader, texture2);
        }
        vbo.draw();
    }

    private void setUniforms(Shader shader, Texture texture){
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
        shader.setUniform("model_m",projection.getModelMatrix());
        shader.setUniform("group", group);
        shader.setUniform("id", id);
    }
}
