package ru.phoenix.game.hud.board;

import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;
import ru.phoenix.engine.core.buffer.vbo.HudVbo;
import ru.phoenix.engine.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.engine.core.configuration.WindowConfig;
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
import static ru.phoenix.engine.core.constants.System.*;

public class Board implements HeadUpDisplay {

    private int resolutionID;
    private String texturePath;
    private int width;
    private int height;

    private Texture texture;
    private VertexBufferObject vbo;
    private Vector3f position;
    private Projection projection;

    public Board(int resolutionID,String texturePath, Vector3f position){
        this.texturePath = texturePath;
        width = WindowConfig.getInstance().getWidth();
        height = WindowConfig.getInstance().getHeight();
        this.resolutionID = resolutionID;
        texture = new Texture2D();
        vbo = new HudVbo();
        this.position = new Vector3f(position);
        projection = new Projection();

        switch (resolutionID){
            case RESOLUTION_640_480:
                width = 640;
                height = 480;
                break;
            case RESOLUTION_800_600:
                width = 800;
                height = 600;
                break;
            case RESOLUTION_1024_768:
                width = 1024;
                height = 768;
                break;
            case RESOLUTION_1280_720:
                width = 1280;
                height = 720;
                break;
            case RESOLUTION_1680_1050:
                width = 1680;
                height = 1050;
                break;
            case RESOLUTION_1920_1080:
                width = 1920;
                height = 1080;
                break;
            default:
                width = WindowConfig.getInstance().getWidth();
                height = WindowConfig.getInstance().getHeight();
                break;
        }
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

        System.out.println(offsetW + " " + offsetH);

        float[] pos = new float[]{
                -offsetW,  offsetH, 0.0f,
                -offsetW, -offsetH, 0.0f,
                offsetW, -offsetH, 0.0f,
                offsetW,  offsetH, 0.0f
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
    public void update(){

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

    @Override
    public int getResolutionID(){
        return resolutionID;
    }
}
