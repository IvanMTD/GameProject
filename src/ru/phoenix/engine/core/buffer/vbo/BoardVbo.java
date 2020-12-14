package ru.phoenix.engine.core.buffer.vbo;

import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class BoardVbo implements VertexBufferObject{

    private final int vao;
    private final List<Integer> vbo;
    private final int ibo;
    private int size;

    public BoardVbo(){
        vao = glGenVertexArrays();
        vbo = new ArrayList<>();
        for(int i=0; i<2; i++){
            vbo.add(glGenBuffers());
        }
        ibo = glGenBuffers();
        size = 0;
    }

    @Override
    public void allocate(ObjectConfiguration objectConfiguration) {
        size = objectConfiguration.getIndices().length;
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
        glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getPositions(), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo.get(1));
        glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getTextureCoords(), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, objectConfiguration.getIndices(), GL_STATIC_DRAW);
        glBindVertexArray(0);
    }

    @Override
    public void draw() {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    @Override
    public void delete() {
        glBindVertexArray(vao);
        for (Integer temp : vbo) {
            glDeleteBuffers(temp);
        }
        glDeleteBuffers(ibo);
        glDeleteVertexArrays(vao);
        glBindVertexArray(0);
    }
}
