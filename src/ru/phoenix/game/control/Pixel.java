package ru.phoenix.game.control;

import ru.phoenix.engine.math.variable.Vector3f;

import java.util.Objects;

public class Pixel {
    private static Vector3f pixel;

    public static void setPixel(Vector3f pixelInfo){
        pixel = new Vector3f(pixelInfo);
    }

    public static Vector3f getPixel(){
        return Objects.requireNonNullElseGet(pixel, Vector3f::new);
    }
}
