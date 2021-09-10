package ru.phoenix.engine.core.buffer.util;

import ru.phoenix.engine.core.buffer.template.ObjectConfiguration;
import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.math.struct.Perlin2D;

public class Collector {
    private static float w;
    private static float h;
    // VERTEX COLLECTOR
    public static ObjectConfiguration collectVertexBufferCenter(int textureWidth, int textureHeight, float percentOfWindow){

        float windowWidth = WindowConfig.getInstance().getWidth();

        float newTextureWidth = windowWidth * percentOfWindow / 100.0f;
        float diffTextureWidth = newTextureWidth * 100.0f / textureWidth;
        float newTextureHeight = textureHeight * diffTextureWidth / 100.0f;
        float hw = newTextureWidth / 2.0f;
        float hh = newTextureHeight / 2.0f;
        Collector.w = newTextureWidth;
        Collector.h = newTextureHeight;

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

    public static ObjectConfiguration collectVertexBufferLeftUp(int textureWidth, int textureHeight, float percentOfWindow){

        float windowWidth = WindowConfig.getInstance().getWidth();

        float nw = windowWidth * percentOfWindow / 100.0f;
        float diffTextureWidth = nw * 100.0f / textureWidth;
        float nh = textureHeight * diffTextureWidth / 100.0f;

        Collector.w = nw;
        Collector.h = nh;

        float[] position = new float[]{
                0.0f,  0.0f,  0.0f,
                0.0f,    nh,  0.0f,
                  nw,    nh,  0.0f,
                  nw,  0.0f,  0.0f
        };

        float[] textureCords = new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        int[] indices = new int[]{
                0, 1, 2,
                0, 2, 3
        };

        return new ObjectConfiguration(position,textureCords,indices);
    }

    public static ObjectConfiguration collectVertexBufferElement(int textureWidth, int textureHeight, float percentOfWindow, int elementNum){

        /* elements in cube
        -------------
        | 7 | 8 | 9 |
        _____________
        | 4 | 5 | 6 |
        -------------
        | 1 | 2 | 3 |
        -------------
         */

        float windowWidth = WindowConfig.getInstance().getWidth();

        float newTextureWidth = windowWidth * percentOfWindow / 100.0f;
        float diffTextureWidth = newTextureWidth * 100.0f / textureWidth;
        float newTextureHeight = textureHeight * diffTextureWidth / 100.0f;
        float hw = newTextureWidth / 2.0f;
        float hh = newTextureHeight / 2.0f;
        Collector.w = newTextureWidth;
        Collector.h = newTextureHeight;

        /*
        p0← ← ←p3
        ↓       ↑
        ↓       ↑
        p1→ → →p2
         */

        float[] position = new float[]{
                -hw, -hh, 0.0f,
                -hw,  hh, 0.0f,
                 hw,  hh, 0.0f,
                 hw, -hh, 0.0f
        };

        float[] textureCords;

        if(elementNum == 1){
            textureCords = new float[]{
                    0.0f,                       1.0f / 3.0f,
                    0.0f,                       0.0f,
                    1.0f / 3.0f,                0.0f,
                    1.0f / 3.0f,                1.0f / 3.0f
            };
        }else if(elementNum == 2){
            textureCords = new float[]{
                    1.0f / 3.0f,                1.0f / 3.0f,
                    1.0f / 3.0f,                0.0f,
                    1.0f / 3.0f * 2.0f,         0.0f,
                    1.0f / 3.0f * 2.0f,         1.0f / 3.0f
            };
        }else if(elementNum == 3){
            textureCords = new float[]{
                    1.0f / 3.0f * 2.0f,         1.0f / 3.0f,
                    1.0f / 3.0f * 2.0f,         0.0f,
                    1.0f,                       0.0f,
                    1.0f,                       1.0f / 3.0f
            };
        }else if(elementNum == 4){
            textureCords = new float[]{
                    0.0f,                       1.0f / 3.0f * 2.0f,
                    0.0f,                       1.0f / 3.0f,
                    1.0f / 3.0f,                1.0f / 3.0f,
                    1.0f / 3.0f,                1.0f / 3.0f * 2.0f
            };
        }else if(elementNum == 5){
            textureCords = new float[]{
                    1.0f / 3.0f,                1.0f / 3.0f * 2.0f,
                    1.0f / 3.0f,                1.0f / 3.0f,
                    1.0f / 3.0f * 2.0f,         1.0f / 3.0f,
                    1.0f / 3.0f * 2.0f,         1.0f / 3.0f * 2.0f
            };
        }else if(elementNum == 6){
            textureCords = new float[]{
                    1.0f / 3.0f * 2.0f,         1.0f / 3.0f * 2.0f,
                    1.0f / 3.0f * 2.0f,         1.0f / 3.0f,
                    1.0f,                       1.0f / 3.0f,
                    1.0f,                       1.0f / 3.0f * 2.0f
            };
        }else if(elementNum == 7){
            textureCords = new float[]{
                    0.0f,                       1.0f,
                    0.0f,                       1.0f / 3.0f * 2.0f,
                    1.0f / 3.0f,                1.0f / 3.0f * 2.0f,
                    1.0f / 3.0f,                1.0f
            };
        }else if(elementNum == 8){
            textureCords = new float[]{
                    1.0f / 3.0f,                1.0f,
                    1.0f / 3.0f,                1.0f / 3.0f * 2.0f,
                    1.0f / 3.0f * 2.0f,         1.0f / 3.0f * 2.0f,
                    1.0f / 3.0f * 2.0f,         1.0f
            };
        }else if(elementNum == 9){
            textureCords = new float[]{
                    1.0f / 3.0f * 2.0f,         1.0f,
                    1.0f / 3.0f * 2.0f,         1.0f / 3.0f * 2.0f,
                    1.0f,                       1.0f / 3.0f * 2.0f,
                    1.0f,                       1.0f
            };
        }else{
            textureCords = new float[]{
                    0.0f, 1.0f,
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f
            };
        }

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

    public static float getW() {
        return w;
    }

    public static float getH() {
        return h;
    }
}
