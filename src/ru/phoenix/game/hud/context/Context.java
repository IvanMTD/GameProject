package ru.phoenix.game.hud.context;

import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;

public interface Context {
    void init(Vector3f wfCenter, Vector3f wfHalfRectangle);
    void update();
    void update(Vector3f direction);
    void draw(Shader shader);
}
