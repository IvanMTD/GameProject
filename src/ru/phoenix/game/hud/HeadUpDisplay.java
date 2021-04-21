package ru.phoenix.game.hud;

import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector3f;

public interface HeadUpDisplay {
    void init();
    void update();
    void draw(Shader shader);
}
