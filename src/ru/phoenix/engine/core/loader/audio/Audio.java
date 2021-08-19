package ru.phoenix.engine.core.loader.audio;

public interface Audio {
    void setup(String path);
    int getAudioID();
    void deleteBuffer();
}
