package ru.phoenix.game.hud.subtype;

import ru.phoenix.engine.core.buffer.util.Collector;
import ru.phoenix.engine.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.engine.core.loader.texture.Texture;
import ru.phoenix.engine.core.loader.texture.Texture2D;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.struct.Projection;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.hud.HeadUpDisplay;
import ru.phoenix.game.hud.template.HudDataset;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class Board implements HeadUpDisplay {
    private int group;
    private float id;
    private boolean posChange;

    private Texture texture;
    private VertexBufferObject vbo;
    private Projection projection;
    private Vector3f position;

    public Board(){
        texture = new Texture2D();
        vbo = new VertexBufferObject();
        projection = new Projection();
        position = new Vector3f();
    }

    @Override
    public void init(HudDataset dataset) {
        this.group = dataset.getGroup();
        this.id = dataset.getId();
        this.posChange = false;

        texture.setup(dataset.getTexturePath(),GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        vbo.allocate(Collector.collectVertexBufferCenter(texture.getWidth(),texture.getHeight(),dataset.getPercentOfWindow()));
        position = dataset.getPosition();

        setDefaultProjection();
    }

    private void setDefaultProjection(){
        projection.getModelMatrix().identity();
        projection.setTranslation(this.position);
        projection.setScaling(1.0f);
    }

    @Override
    public void update() {
        if(posChange){
            posChange = false;
            setDefaultProjection();
        }
    }

    @Override
    public void update(Vector3f direction) {
        posChange = true;
        this.position = this.position.add(direction);
    }

    @Override
    public boolean getAction() {
        return false;
    }

    @Override
    public void setAction(boolean action){

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
