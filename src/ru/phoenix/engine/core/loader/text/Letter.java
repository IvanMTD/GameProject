package ru.phoenix.engine.core.loader.text;

import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;
import ru.phoenix.engine.core.buffer.util.Collector;
import ru.phoenix.engine.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.struct.Projection;
import ru.phoenix.engine.math.variable.Vector3f;

import static ru.phoenix.engine.core.constants.TextureInfo.GROUP_A;

public class Letter {
    private VertexBufferObject vbo;
    private Projection projection;


    public Letter(){
        vbo = new VertexBufferObject();
        projection = new Projection();
    }

    public void init(Vector3f position, ObjectConfiguration objectConfiguration){
        projection.getModelMatrix().identity();
        projection.setTranslation(position);
        vbo.allocate(objectConfiguration);
    }

    public void update(Vector3f position, float letterSize){
        projection.getModelMatrix().identity();
        projection.setTranslation(position);
        projection.setScaling(letterSize);
    }

    public void draw(Shader shader){
        setUniforms(shader);
        vbo.draw();
    }

    private void setUniforms(Shader shader){
        shader.setUniform("model_m",projection.getModelMatrix());
        shader.setUniform("group", GROUP_A);
        shader.setUniform("id",0.0f);
    }
}
