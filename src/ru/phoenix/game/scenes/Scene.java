package ru.phoenix.game.scenes;

public interface Scene {
    void init(boolean active);
    void update();
    void run();
    void stop();
    void draw();
    int getSceneID();
    boolean isActive();
}
