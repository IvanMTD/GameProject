package ru.phoenix.game;

import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.kernel.MainLoop;

public class Game {
    public static void main(String[] args) {
        System.out.println("Program start");
        MainLoop mainLoop = new MainLoop();
        mainLoop.createWindow(WindowConfig.getInstance());
        mainLoop.init();
        mainLoop.start();
    }
}
/*package ru.phoenix.game;

import java.util.Random;

public class Game {
    public static void main(String[] args) {
        Map.getInstance().showMap();
    }
}

class Map{

    private static Map instance = null;

    private Cell[][] cells;

    public Map(){
        init((int)(40.0f + Math.random() * 40.0f));
    }

    private void init(int size){
        cells = new Cell[size][size];
        float[][]heights = new float[size][size];
        Perlin2D perlin = new Perlin2D((long)(1 + Math.random() * 10000000000L));
        float accuracy = 100.0f;
        for(int x=0; x<size; x++){
            for(int y=0; y<size; y++){
                float value = perlin.getNoise(x/accuracy,y/accuracy,8,0.5f);
                int n = (int)(value * 255 + 128) & 255;
                float result = ((float)n / 255.0f);
                heights[x][y] = result;
            }
        }

        float min = heights[0][0];
        float max = heights[0][0];
        for(int x = 0; x < heights.length; x++){
            for(int y = 0; y < heights[0].length; y++){
                float num = heights[x][y];
                if(num < min){
                    min = num;
                }
                if(num > max){
                    max = num;
                }
            }
        }

        float diff = Math.abs(max - min);

        for(int x=0; x<size; x++){
            for(int y=0; y<size; y++){
                float h = heights[x][y] - min;
                float percent = h * 100.0f / diff;
                float H = percent / 100.0f;
                cells[x][y] = new Cell(H * 100.0f);
            }
        }
    }

    public void showMap(){
        for(int x=0; x<cells.length; x++){
            for(int y=0; y<cells[0].length;y++){
                cells[x][y].showCell();
            }
            System.out.println();
        }
    }

    public static Map getInstance() {
        if(instance == null){
            instance = new Map();
        }
        return instance;
    }
}

class Cell{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    int height;

    public Cell(float height){
        this.height = (int)height;
    }

    public void showCell(){
        if(height < 20.0f){
            System.out.print(ANSI_BLUE + "~" + ANSI_RESET);
        }else if(height < 40.0f){
            System.out.print(ANSI_YELLOW + "*" + ANSI_RESET);
        }else if(height < 60.0f){
            System.out.print(ANSI_GREEN+ "#" + ANSI_RESET);
        }else if(height < 80.0f){
            System.out.print(ANSI_RED + "|" + ANSI_RESET);
        }else{
            System.out.print(ANSI_CYAN + "^" + ANSI_RESET);
        }
    }
}

class Perlin2D {
    private byte[] permutationTable;

    public Perlin2D(long seed) {
        Random random = new Random(seed);
        permutationTable = new byte[1024];
        random.nextBytes(permutationTable);
    }

    public float getNoise(float fx, float fy, int octaves, float persistence) {
        float amplitude = 1;
        float max = 0;
        float result = 0;

        while (octaves-- > 0) {
            max += amplitude;
            result += getNoise(fx, fy) * amplitude;
            amplitude *= persistence;
            fx *= 2;
            fy *= 2;
        }

        return result/max;
    }

    private float getNoise(float x, float y) {
        int left = (int)x;
        int top = (int)y;

        float localX = x - left;
        float localY = y - top;

        Vector topLeftGradient     = getPseudoRandomGradientVector(left,   top  );
        Vector topRightGradient    = getPseudoRandomGradientVector(left+1, top  );
        Vector bottomLeftGradient  = getPseudoRandomGradientVector(left,   top+1);
        Vector bottomRightGradient = getPseudoRandomGradientVector(left+1, top+1);

        Vector distanceToTopLeft     = new Vector(localX,   localY);
        Vector distanceToTopRight    = new Vector(localX-1, localY);
        Vector distanceToBottomLeft  = new Vector(localX,   localY-1);
        Vector distanceToBottomRight = new Vector(localX-1, localY-1);

        float tx1 = dot(distanceToTopLeft,     topLeftGradient);
        float tx2 = dot(distanceToTopRight,    topRightGradient);
        float bx1 = dot(distanceToBottomLeft,  bottomLeftGradient);
        float bx2 = dot(distanceToBottomRight, bottomRightGradient);

        // интерполяция:
        float tx = lerp(tx1, tx2, qunticCurve(localX));
        float bx = lerp(bx1, bx2, qunticCurve(localX));

        return lerp(tx, bx, qunticCurve(localY));
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private float dot(Vector a, Vector b) {
        return a.x * b.x + a.y * b.y;
    }

    private float qunticCurve(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private Vector getPseudoRandomGradientVector(int x, int y) {
        // псевдо-случайное число от 0 до 3 которое всегда неизменно при данных x и y
        int v = (int)(((x * 1836311903L) ^ (y * 2971215073L) + 4807526976L) & 1023L);
        v = permutationTable[v] & 3;

        switch (v) {
            case 0:  return new Vector(1, 0);
            case 1:  return new Vector(-1, 0);
            case 2:  return new Vector(0, 1);
            default: return new Vector(0,-1);
        }
    }
}

class Vector{

    float x;
    float y;

    public Vector(float x, float y){
        this.x = x;
        this.y = y;
    }
}*/

