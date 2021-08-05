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

