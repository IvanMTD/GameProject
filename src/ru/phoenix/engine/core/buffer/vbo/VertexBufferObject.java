package ru.phoenix.engine.core.buffer.vbo;

import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;

public interface VertexBufferObject {
    void allocate(ObjectConfiguration objectConfiguration);
    void draw();
    void delete();
}
