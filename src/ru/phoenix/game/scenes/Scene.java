package ru.phoenix.game.scenes;

public interface Scene {
    void init();
    void update();
    void draw();
    int getSceneID();
}
