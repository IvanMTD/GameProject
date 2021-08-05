package ru.phoenix.game.hud;

import ru.phoenix.engine.core.buffer.util.Collector;
import ru.phoenix.engine.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.engine.core.kernel.Window;
import ru.phoenix.engine.core.loader.texture.Texture;
import ru.phoenix.engine.core.loader.texture.Texture2D;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.struct.Projection;
import ru.phoenix.engine.math.variable.Vector3f;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.engine.core.constants.TextureInfo.STATIC_BOARD;

public class HeadUpDisplay {

    private int hudType;
    private int group;
    private float id;

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
    }

    public void init(String texturePath){
        if(hudType == STATIC_BOARD){
            texture.setup(texturePath,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
            vbo.allocate(Collector.collectVertexBufferCenter(texture.getWidth(),texture.getHeight()));
            projection.getModelMatrix().identity();
            projection.setTranslation(position);
            projection.setScaling(Window.getInstance().getRatio());
        }else{
            System.out.println("ERROR IN HUD CLASS INIT");
        }
    }

    public void update(){

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
