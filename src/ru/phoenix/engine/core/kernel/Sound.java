package ru.phoenix.engine.core.kernel;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class Sound {
    private static Sound instance = null;

    private long device;
    private long context;

    public void soundOn(){
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);
        int[] attributes = {0};
        context = alcCreateContext(device, attributes);

        alcMakeContextCurrent(context);
        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        getError();
    }

    public boolean getError(){
        boolean error = false;
        int alError = alcGetError(device);

        if(alError != ALC_NO_ERROR){
            if (alError == AL_INVALID_NAME) {
                System.out.println("OpenAL error! Invalid name!");
            } else if (alError == AL_INVALID_ENUM) {
                System.out.println("OpenAL error! Invalid enum!");
            } else if (alError == AL_INVALID_VALUE) {
                System.out.println("OpenAL error! Invalid value!");
            } else if (alError == AL_INVALID_OPERATION) {
                System.out.println("OpenAL error! Invalid operation!");
            } else if (alError == AL_OUT_OF_MEMORY) {
                System.out.println("OpenAL error! Out of memory!");
            }
            error = true;
        }

        return error;
    }

    public void dispose(){
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    public static Sound getInstance() {
        if(instance == null){
            instance = new Sound();
        }
        return instance;
    }
}
