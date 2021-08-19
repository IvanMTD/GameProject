package ru.phoenix.game;

import ru.phoenix.engine.core.configuration.WindowConfig;
import ru.phoenix.engine.core.kernel.CoreEngine;

public class Game {
    public static void main(String[] args) {
        System.out.println("Program start");
        CoreEngine coreEngine = new CoreEngine();
        coreEngine.createWindow(WindowConfig.getInstance());
        coreEngine.soundOn();
        coreEngine.init();
        coreEngine.start();
    }
}

