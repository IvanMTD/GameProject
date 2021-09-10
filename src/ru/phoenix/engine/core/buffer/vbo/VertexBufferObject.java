package ru.phoenix.engine.core.buffer.vbo;

import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;
import ru.phoenix.engine.core.buffer.util.BufferUtil;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class VertexBufferObject {
    private int vao;
    private List<Integer> vbo;
    private int ibo;

    private int sizeIndices;
    private int sizeInstance;
    private int size;

    private boolean skybox;
    private boolean animated;
    private boolean tangent;
    private boolean instances;

    private final int drawInfo;
    private final int drawMethod;

    private ObjectConfiguration objectConfig;

    public VertexBufferObject(){
        vao = glGenVertexArrays();
        vbo = new ArrayList<>();
        for(int i=0; i<10; i++){
            vbo.add(glGenBuffers());
        }
        ibo = glGenBuffers();

        sizeIndices = 0;
        sizeInstance = 0;

        animated = false;
        tangent = false;
        instances = false;

        drawInfo = GL_STATIC_DRAW;
        drawMethod = GL_TRIANGLES;
        skybox = false;
    }

    public VertexBufferObject(int drawInfo, int drawMethod){
        vao = glGenVertexArrays();
        vbo = new ArrayList<>();
        for(int i=0; i<10; i++){
            vbo.add(glGenBuffers());
        }
        ibo = glGenBuffers();

        sizeIndices = 0;
        sizeInstance = 0;

        animated = false;
        tangent = false;
        instances = false;

        this.drawInfo = drawInfo;
        this.drawMethod = drawMethod;
        skybox = false;
    }

    public void allocate(ObjectConfiguration objectConfiguration){
        objectConfig = new ObjectConfiguration(objectConfiguration);
        if(objectConfiguration.getIndices() != null) {
            int v4size = (int) Math.pow(Float.BYTES, 2);

            setBufferControl(objectConfiguration);

            sizeIndices = objectConfiguration.getIndices().length;
            sizeInstance = objectConfiguration.getInstances() != null ? objectConfiguration.getInstances().length : 0;

            if (animated) {
                glBindVertexArray(vao);
                // Записываем все позиции в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
                if (objectConfiguration.getPositions() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getPositions(), drawInfo);
                }
                glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                // Записываем все нормали в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(1));
                if (objectConfiguration.getNormals() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getNormals(), drawInfo);
                }
                glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
                // Записываем все текстуры в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(2));
                if (objectConfiguration.getTextureCords() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getTextureCords(), drawInfo);
                }
                glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
                // Записываем все тангенсы в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(3));
                if (objectConfiguration.getTangents() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getTangents(), drawInfo);
                }
                glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
                // Записываем все битангенсы в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(4));
                if (objectConfiguration.getBiTangents() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getBiTangents(), drawInfo);
                }
                glVertexAttribPointer(4, 3, GL_FLOAT, false, 0, 0);
                // Записываем все айди костей
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(5));
                if (objectConfiguration.getBoneIds() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getBoneIds(), drawInfo);
                }
                glVertexAttribPointer(5, 4, GL_FLOAT, false, 0, 0);
                // Записываем все веса костей
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(6));
                if (objectConfiguration.getBoneWeights() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getBoneWeights(), drawInfo);
                }
                glVertexAttribPointer(6, 4, GL_FLOAT, false, 0, 0);

                if (objectConfiguration.getInstances() != null) {
                    glBindBuffer(GL_ARRAY_BUFFER, vbo.get(7));
                    glBufferData(GL_ARRAY_BUFFER, BufferUtil.createFlippedBuffer(objectConfiguration.getInstances()), drawInfo);
                    glVertexAttribPointer(7, 4, GL_FLOAT, false, Float.BYTES * v4size, 0);
                    glVertexAttribPointer(8, 4, GL_FLOAT, false, Float.BYTES * v4size, v4size);
                    glVertexAttribPointer(9, 4, GL_FLOAT, false, Float.BYTES * v4size, 2 * v4size);
                    glVertexAttribPointer(10, 4, GL_FLOAT, false, Float.BYTES * v4size, 3 * v4size);
                    glBindBuffer(GL_ARRAY_BUFFER, 0);
                    glVertexAttribDivisor(7, 1);
                    glVertexAttribDivisor(8, 1);
                    glVertexAttribDivisor(9, 1);
                    glVertexAttribDivisor(10, 1);
                }

                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, objectConfiguration.getIndices(), drawInfo);
                glBindVertexArray(0);
            } else {
                glBindVertexArray(vao);
                // Записываем все позиции в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
                if (objectConfiguration.getPositions() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getPositions(), drawInfo);
                }
                glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                // Записываем все нормали в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(1));
                if (objectConfiguration.getNormals() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getNormals(), drawInfo);
                }
                glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
                // Записываем все текстуры в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(2));
                if (objectConfiguration.getTextureCords() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getTextureCords(), drawInfo);
                }
                glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
                // Записываем все тангенсы в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(3));
                if (objectConfiguration.getTangents() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getTangents(), drawInfo);
                }
                glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
                // Записываем все битангенсы в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(4));
                if (objectConfiguration.getBiTangents() != null) {
                    glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getBiTangents(), drawInfo);
                }
                glVertexAttribPointer(4, 3, GL_FLOAT, false, 0, 0);

                if (objectConfiguration.getInstances() != null) {
                    glBindBuffer(GL_ARRAY_BUFFER, vbo.get(7));
                    glBufferData(GL_ARRAY_BUFFER, BufferUtil.createFlippedBuffer(objectConfiguration.getInstances()), drawInfo);
                    glVertexAttribPointer(7, 4, GL_FLOAT, false, Float.BYTES * v4size, 0);
                    glVertexAttribPointer(8, 4, GL_FLOAT, false, Float.BYTES * v4size, v4size);
                    glVertexAttribPointer(9, 4, GL_FLOAT, false, Float.BYTES * v4size, 2 * v4size);
                    glVertexAttribPointer(10, 4, GL_FLOAT, false, Float.BYTES * v4size, 3 * v4size);
                    glBindBuffer(GL_ARRAY_BUFFER, 0);
                    glVertexAttribDivisor(7, 1);
                    glVertexAttribDivisor(8, 1);
                    glVertexAttribDivisor(9, 1);
                    glVertexAttribDivisor(10, 1);
                }

                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, objectConfiguration.getIndices(), drawInfo);
                glBindVertexArray(0);
            }
        }else{
            skybox = true;
            size = objectConfiguration.getPositions().length / 3;
            glBindVertexArray(vao);

            glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
            glBufferData(GL_ARRAY_BUFFER, objectConfiguration.getPositions(), GL_STATIC_DRAW);

            glVertexAttribPointer(0,3,GL_FLOAT,false,Float.BYTES * 3,0);
            glEnableVertexAttribArray(0);
        }
    }

    public void draw(){
        if(skybox){
            glBindVertexArray(vao);
            glDrawArrays(GL_TRIANGLES, 0, size);
            glBindVertexArray(0);
        }else {
            if (instances) {
                if (animated) {
                    glBindVertexArray(vao);
                    glEnableVertexAttribArray(0);
                    glEnableVertexAttribArray(1);
                    glEnableVertexAttribArray(2);
                    glEnableVertexAttribArray(3);
                    glEnableVertexAttribArray(4);
                    glEnableVertexAttribArray(5);
                    glEnableVertexAttribArray(6);
                    glEnableVertexAttribArray(7);
                    glEnableVertexAttribArray(8);
                    glEnableVertexAttribArray(9);
                    glEnableVertexAttribArray(10);
                    glDrawElementsInstanced(drawMethod, sizeIndices, GL_UNSIGNED_INT, 0, sizeInstance);
                    glDisableVertexAttribArray(0);
                    glDisableVertexAttribArray(1);
                    glDisableVertexAttribArray(2);
                    glDisableVertexAttribArray(3);
                    glDisableVertexAttribArray(4);
                    glDisableVertexAttribArray(5);
                    glDisableVertexAttribArray(6);
                    glDisableVertexAttribArray(7);
                    glDisableVertexAttribArray(8);
                    glDisableVertexAttribArray(9);
                    glDisableVertexAttribArray(10);
                    glBindVertexArray(0);
                } else {
                    glBindVertexArray(vao);
                    glEnableVertexAttribArray(0);
                    glEnableVertexAttribArray(1);
                    glEnableVertexAttribArray(2);
                    glEnableVertexAttribArray(3);
                    glEnableVertexAttribArray(4);
                    glEnableVertexAttribArray(7);
                    glEnableVertexAttribArray(8);
                    glEnableVertexAttribArray(9);
                    glEnableVertexAttribArray(10);
                    glDrawElementsInstanced(drawMethod, sizeIndices, GL_UNSIGNED_INT, 0, sizeInstance);
                    glDisableVertexAttribArray(0);
                    glDisableVertexAttribArray(1);
                    glDisableVertexAttribArray(2);
                    glDisableVertexAttribArray(3);
                    glDisableVertexAttribArray(4);
                    glDisableVertexAttribArray(7);
                    glDisableVertexAttribArray(8);
                    glDisableVertexAttribArray(9);
                    glDisableVertexAttribArray(10);
                    glBindVertexArray(0);
                }
            } else {
                if (animated) {
                    glBindVertexArray(vao);
                    glEnableVertexAttribArray(0);
                    glEnableVertexAttribArray(1);
                    glEnableVertexAttribArray(2);
                    glEnableVertexAttribArray(3);
                    glEnableVertexAttribArray(4);
                    glEnableVertexAttribArray(5);
                    glEnableVertexAttribArray(6);
                    glDrawElements(drawMethod, sizeIndices, GL_UNSIGNED_INT, 0);
                    glDisableVertexAttribArray(0);
                    glDisableVertexAttribArray(1);
                    glDisableVertexAttribArray(2);
                    glDisableVertexAttribArray(3);
                    glDisableVertexAttribArray(4);
                    glDisableVertexAttribArray(5);
                    glDisableVertexAttribArray(6);
                    glBindVertexArray(0);
                } else {
                    glBindVertexArray(vao);
                    glEnableVertexAttribArray(0);
                    glEnableVertexAttribArray(1);
                    glEnableVertexAttribArray(2);
                    glEnableVertexAttribArray(3);
                    glEnableVertexAttribArray(4);
                    glDrawElements(drawMethod, sizeIndices, GL_UNSIGNED_INT, 0);
                    glDisableVertexAttribArray(0);
                    glDisableVertexAttribArray(1);
                    glDisableVertexAttribArray(2);
                    glDisableVertexAttribArray(3);
                    glDisableVertexAttribArray(4);
                    glBindVertexArray(0);
                }
            }
        }
    }

    private void setBufferControl(ObjectConfiguration objectConfiguration){
        if(objectConfiguration.getTangents() != null && objectConfiguration.getBiTangents() != null){
            this.tangent = true;
        }
        if(objectConfiguration.getBoneIds() != null && objectConfiguration.getBoneWeights() != null){
            this.animated = true;
        }
        if(objectConfiguration.getInstances() != null){
            this.instances = true;
        }
    }

    public void setNewPos(float[] position){

        float[] p1 = position;
        float[] p2 = objectConfig.getPositions();
        float[] pos = new float[p1.length];

        for(int i=0; i<p1.length; i++){
            pos[i] = p1[i] + p2[i];
        }

        objectConfig.setPositions(pos);

        glBindVertexArray(vao);
        glDeleteBuffers(vbo.get(0));
        vbo.set(0,glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
        glBufferData(GL_ARRAY_BUFFER, pos, drawInfo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }

    public float[] getCurrentPosition(){
        return objectConfig.getPositions();
    }

    public boolean isAnimated() {
        return animated;
    }

    public boolean isTangent() {
        return tangent;
    }

    public boolean isInstances() {
        return instances;
    }

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
