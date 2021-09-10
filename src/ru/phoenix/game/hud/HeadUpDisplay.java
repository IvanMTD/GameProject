package ru.phoenix.game.hud;

import ru.phoenix.engine.core.shader.Shader;
import ru.phoenix.engine.math.variable.Vector2f;
import ru.phoenix.engine.math.variable.Vector3f;
import ru.phoenix.game.hud.template.HudDataset;

public interface HeadUpDisplay {
    void init(HudDataset dataset);
    void update();
    void update(Vector3f direction);
    boolean getAction();
    void setAction(boolean action);
    float getId();
    void draw(Shader shader);
}
