package ru.phoenix.engine.core.buffer.fbo;

public interface FrameBufferObject {
    int getFrameBuffer();
    int getTexture();
    int getTexture(int index);
    int getTextureSize();
    void delete();
}
