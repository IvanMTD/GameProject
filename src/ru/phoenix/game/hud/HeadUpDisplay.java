package ru.phoenix.game.hud;

import ru.phoenix.engine.core.buffer.util.Collector;
import ru.phoenix.engine.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.engine.core.control.Input;
import ru.phoenix.engine.core.loader.texture.Texture;
import ru.phoenix.engine.core.loader.texture.Texture2D;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.struct.Projection;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.control.GameController;
import ru.phoenix.game.control.Pixel;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.engine.core.constants.TextureInfo.*;

public class HeadUpDisplay {

    private int hudType;
    private int group;
    private float id;
    private boolean target;
    private boolean action;

    private Texture texture;
    private VertexBufferObject vbo;
    private Projection projection;
    private Vector3f position;

    public HeadUpDisplay(int hudType, Vector3f position, int group, float id){
        this.hudType = hudType;
        this.group = group;
        this.id = id;
        this.position = new Vector3f(position);
        projection = new Projection();
        texture = new Texture2D();
        vbo = new VertexBufferObject();
        target = false;
        action = false;
    }

    public void init(String texturePath, float percentOfWindow){
        if(hudType == STATIC_BOARD){
            texture.setup(texturePath,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
            vbo.allocate(Collector.collectVertexBufferCenter(texture.getWidth(),texture.getHeight(),percentOfWindow));
            setDefaultProjection();
        }else if(hudType == STATIC_BUTTON){
            texture.setup(texturePath,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
            vbo.allocate(Collector.collectVertexBufferCenter(texture.getWidth(),texture.getHeight(),percentOfWindow));
            setDefaultProjection();
        }else if(hudType == CURSOR){
            texture.setup(texturePath,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
            vbo.allocate(Collector.collectVertexBufferLeftUp(texture.getWidth(),texture.getHeight(),percentOfWindow));
            setDefaultProjection();
        }else{
            System.out.println("ERROR IN HUD CLASS INIT");
        }
    }

    public void update(){
        if(hudType == STATIC_BUTTON){
            target = Pixel.getPixel().getR() == id;
            if(target){
                action = GameController.getInstance().getMouseControl().isMouse_1_click();
            }
        }else if(hudType == CURSOR){
            projection.getModelMatrix().identity();
            projection.getModelMatrix().setTranslation(new Vector3f(Input.getInstance().getCursorPosition(), 0.0f));
        }
    }

    public void setDefaultProjection(){
        projection.getModelMatrix().identity();
        projection.setTranslation(this.position);
        projection.setScaling(1.0f);
    }

    public void setAction(boolean action){
        this.action = action;
    }

    public boolean isAction(){
        return action;
    }

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
