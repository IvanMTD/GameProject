package ru.phoenix.engine.core.loader.audio;

import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.alBufferi;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class AudioBase implements Audio {

    private int audioID;

    public AudioBase(){
        // Запрос места для буфера
        audioID = alGenBuffers();
    }

    @Override
    public void setup(String path) {
        ShortBuffer rawAudioBuffer;

        int channels;
        int sampleRate;

        try (MemoryStack stack = stackPush()) {
            // Allocate space to store return information from the function
            IntBuffer channelsBuffer   = stack.mallocInt(1);
            IntBuffer sampleRateBuffer = stack.mallocInt(1);

            rawAudioBuffer = stb_vorbis_decode_filename(path, channelsBuffer, sampleRateBuffer);

            // Retreive the extra information that was stored in the buffers by the function
            channels = channelsBuffer.get(0);
            sampleRate = sampleRateBuffer.get(0);
        }

        // Найдите правильный формат OpenAL
        int format = -1;
        if(channels == 1) {
            format = AL_FORMAT_MONO16;
        } else if(channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        // Отправить данные в OpenAL
        alBufferData(audioID, format, rawAudioBuffer, sampleRate);

        // Освободите память, выделенную STB
        free(rawAudioBuffer);
    }

    @Override
    public int getAudioID() {
        return audioID;
    }

    @Override
    public void deleteBuffer() {
        alDeleteBuffers(audioID);
    }
}
