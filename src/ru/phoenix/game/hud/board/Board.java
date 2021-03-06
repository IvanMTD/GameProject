package ru.phoenix.game.hud.board;

import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;
import ru.phoenix.engine.core.buffer.vbo.HudVbo;
import ru.phoenix.engine.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.control.Input;
import ru.phoenix.engine.core.loader.texture.Texture;
import ru.phoenix.engine.core.loader.texture.Texture2D;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.struct.Projection;
import ru.phoenix.engine.math.variable.Vector2f;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.control.GameController;
import ru.phoenix.game.hud.HeadUpDisplay;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.engine.core.constants.TextureInfo.GROUP_R;

public class Board implements HeadUpDisplay {

    private String texturePath;
    private int width;
    private int height;
    private int activeFieldX;
    private int activeFieldY;
    private Vector2f mouseMoveOffset;
    private boolean boardMoveLock;

    private Texture texture;
    private VertexBufferObject vbo;
    private Vector3f position;
    private Projection projection;

    public Board(String texturePath, Vector3f position){
        this.texturePath = texturePath;
        width = WindowConfig.getInstance().getWidth();
        height = WindowConfig.getInstance().getHeight();
        mouseMoveOffset = new Vector2f();
        texture = new Texture2D();
        vbo = new HudVbo();
        this.position = new Vector3f(position);
        projection = new Projection();
        boardMoveLock = false;
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

        activeFieldX = (int)offsetW * 2;
        activeFieldY = (int)offsetH * 2;

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
    public void update(){
        if(GameController.getInstance().getMouseControl().isMouse_1_hold()){
            float x = Input.getInstance().getCursorPosition().getX();
            float y = Input.getInstance().getCursorPosition().getY();

            float lx = position.getX() - (activeFieldX >> 1);
            float rx = position.getX() + (activeFieldX >> 1);

            float uy = position.getY() + (activeFieldY >> 1);
            float dy = position.getY() - (activeFieldY >> 1);


            boolean collision = false;

            if(!boardMoveLock){
                collision = ((lx <= x) && (x <= rx)) && ((dy <= y) && (y <= uy));
            }

            if(!collision){
                boardMoveLock = true;
            }else{
                if(Input.getInstance().isCursorMove()) {
                    Vector3f mp = new Vector3f(mouseMoveOffset, position.getZ());
                    Vector3f bp = new Vector3f(Input.getInstance().getCursorPosition(),position.getZ());
                    Vector3f np = new Vector3f(bp.sub(mp));
                    projection.getModelMatrix().identity();
                    position = new Vector3f(position.add(np));
                    projection.setTranslation(position);
                    boardMoveLock = false;
                }
            }
        }else{
            boardMoveLock = false;
        }

        mouseMoveOffset = new Vector2f(Input.getInstance().getCursorPosition());
    }

    @Override
    public void draw(Shader shader){
        setUniforms(shader);
        vbo.draw();
    }

    private void setUniforms(Shader shader){
        shader.setUniform("model_m",projection.getModelMatrix());
        shader.setUniform("group",GROUP_R);
        shader.setUniform("id",0.1f);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
    }
}
