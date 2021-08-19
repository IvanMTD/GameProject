package ru.phoenix.game.sound;

import ru.phoenix.engine.core.loader.audio.Audio;
import ru.phoenix.engine.core.loader.audio.AudioBase;
import ru.phoenix.engine.math.variable.Vector3f;

import static org.lwjgl.openal.AL10.*;

public class SoundManagement {

    private int source;
    private Audio audio;
    private Vector3f position;

    public SoundManagement(){
        source = alGenSources();
        audio = new AudioBase();
        position = new Vector3f();
    }

    public void init(String path){
        audio.setup(path);
        alSourcei(source, AL_BUFFER, audio.getAudioID());
        alSourcei(source,AL_LOOPING, AL_TRUE);
    }

    public void playSound(){
        alSourcePlay(source);
    }

    public void deleteSource(){
        audio.deleteBuffer();
        alDeleteSources(source);
    }
}
