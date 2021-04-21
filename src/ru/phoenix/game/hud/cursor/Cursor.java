package ru.phoenix.game.hud.cursor;

import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;
import ru.phoenix.engine.core.buffer.vbo.HudVbo;
import ru.phoenix.engine.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.control.Input;
import ru.phoenix.engine.core.loader.texture.Texture;
import ru.phoenix.engine.core.loader.texture.Texture2D;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.struct.Projection;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.hud.HeadUpDisplay;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class Cursor implements HeadUpDisplay {
    private String texturePath;
    private int width;
    private int height;

    private Texture texture;
    private VertexBufferObject vbo;
    private Vector3f position;
    private Projection projection;

    public Cursor(String texturePath, Vector3f position){
        this.texturePath = texturePath;
        width = WindowConfig.getInstance().getWidth();
        height = WindowConfig.getInstance().getHeight();
        texture = new Texture2D();
        vbo = new HudVbo();
        this.position = new Vector3f(position);
        projection = new Projection();
    }

    @Override
    public void init(){
        texture.setup(texturePath,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        int textureWidth = texture.getWidth();
        int textureHeight = texture.getHeight();

        int etalonWidth = 1920;
        int etalonHeight = 1080;

        float proportion = (float)etalonWidth / (float)width;
        float offsetW = ((float)textureWidth / proportion) / 2.0f;
        float offsetH = ((float)textureHeight / proportion) / 2.0f;
        float offsetF = 0.0f;

        float[] pos = new float[]{
                -offsetW,  offsetH, offsetF,
                -offsetW, -offsetH, offsetF,
                offsetW, -offsetH, offsetF,
                offsetW,  offsetH, offsetF
        };

        float[] tex = new float[]{
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };

        int[] indices = new int[]{
                0,1,2,
                0,2,3
        };

        ObjectConfiguration objectConfiguration = new ObjectConfiguration();
        objectConfiguration.setPositions(pos);
        objectConfiguration.setTextureCoords(tex);
        objectConfiguration.setIndices(indices);
        vbo.allocate(objectConfiguration);
        projection.setTranslation(this.position);
    }

    @Override
    public void update() {
        if(Input.getInstance().isCursorMove()) {
            this.position = new Vector3f(Input.getInstance().getCursorPosition(),this.position.getZ());
            projection.getModelMatrix().identity();
            projection.setTranslation(this.position);
        }
    }

    @Override
    public void draw(Shader shader){
        setUniforms(shader);
        vbo.draw();
    }

    private void setUniforms(Shader shader){
        shader.setUniform("model_m",projection.getModelMatrix());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
    }
}
