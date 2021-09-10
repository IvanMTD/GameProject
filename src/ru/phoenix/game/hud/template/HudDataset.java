package ru.phoenix.game.hud.template;

import ru.phoenix.engine.math.variable.Vector3f;

import static ru.phoenix.engine.core.constants.TextureInfo.GROUP_A;

public class HudDataset {
    private final int group;
    private final float id;
    private final float percentOfWindow;

    private final String texturePath;
    private final String texturePath2;
    private final Vector3f position;

    public HudDataset(int group, float id, float percentOfWindow, String texturePath, String texturePath2, Vector3f position) {
        this.group = group;
        this.id = id;
        this.percentOfWindow = percentOfWindow;
        this.texturePath = texturePath;
        this.texturePath2 = texturePath2;
        this.position = position;
    }

    public HudDataset(int group, float id, float percentOfWindow, String texturePath, Vector3f position) {
        this.group = group;
        this.id = id;
        this.percentOfWindow = percentOfWindow;
        this.texturePath = texturePath;
        this.texturePath2 = "";
        this.position = position;
    }

    public HudDataset(float percentOfWindow, String texturePath, Vector3f position){
        this.group = GROUP_A;
        this.id = 0.0f;
        this.percentOfWindow = percentOfWindow;
        this.texturePath = texturePath;
        this.texturePath2 = "";
        this.position = position;
    }

    public HudDataset(HudDataset hudDataset){
        this.group = hudDataset.getGroup();
        this.id = hudDataset.getId();
        this.percentOfWindow = hudDataset.getPercentOfWindow();
        this.texturePath = hudDataset.getTexturePath();
        this.texturePath2 = hudDataset.getTexturePath2();
        this.position = hudDataset.getPosition();
    }

    public int getGroup() {
        return group;
    }

    public float getId() {
        return id;
    }

    public float getPercentOfWindow() {
        return percentOfWindow;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public String getTexturePath2() {
        return texturePath2;
    }

    public Vector3f getPosition() {
        return position;
    }
}
