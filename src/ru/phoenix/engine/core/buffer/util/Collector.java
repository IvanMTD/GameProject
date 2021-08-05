package ru.phoenix.engine.core.buffer.util;

import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.math.struct.Perlin2D;

public class Collector {
    // VERTEX COLLECTOR
    public static ObjectConfiguration collectVertexBufferCenter(int width, int height){

        float hw = width / 2.0f;
        float hh = height / 2.0f;

        float[] position = new float[]{
               -hw,  hh, 0.0f,
               -hw, -hh, 0.0f,
                hw, -hh, 0.0f,
                hw,  hh, 0.0f
        };

        float[] textureCords = new float[]{
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };

        int[] indices = new int[]{
                0, 1, 2,
                0, 2, 3
        };

        return new ObjectConfiguration(position,textureCords,indices);
    }

    // HEIGHTMAP COLLECTOR
    public static float[][] getFloatHeightMap(){
        Perlin2D perlin2D = new Perlin2D((long)(1 + Math.random() * 10000000000L));
        float[][]heightMap = new float[WindowConfig.getInstance().getWidth()][WindowConfig.getInstance().getHeight()];
        for(int x = 1; x <= heightMap.length; x++){
            for(int y = 1; y <= heightMap[0].length; y++){
                float X = (float)x / (float)heightMap.length;
                float Y = (float)y / (float)heightMap[0].length;
                float num = perlin2D.getNoise(X,Y,8,0.5f) - 0.5f;
                heightMap[x-1][y-1] = num;
            }
        }

        float min = heightMap[0][0];
        float max = heightMap[0][0];
        for(int x = 0; x < heightMap.length; x++){
            for(int y = 0; y < heightMap[0].length; y++){
                float num = heightMap[x][y];
                if(num < min){
                    min = num;
                }
                if(num > max){
                    max = num;
                }
            }
        }

        float diff = Math.abs(max - min);

        for(int x=0; x<heightMap.length; x++){
            for(int y=0; y<heightMap[0].length; y++){
                heightMap[x][y] = (heightMap[x][y] - min) / diff;
            }
        }

        return heightMap;
    }
}
