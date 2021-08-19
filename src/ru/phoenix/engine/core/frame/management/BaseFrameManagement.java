package ru.phoenix.engine.core.frame.management;

import ru.phoenix.engine.core.configuration.WindowConfig;

public class BaseFrameManagement {
    public static final int KERNEL_STD = 0;
    public static final int KERNEL_BLR = 1;
    public static final int KERNEL_INV = 2;
    public static final int KERNEL_SHR = 3;

    private static int kernel = KERNEL_STD;
    private static float correction = 300.0f;
    private static float gamma = WindowConfig.getInstance().getGamma();
    private static float contrast = WindowConfig.getInstance().getContrast();
    private static float tune = 1.0f;

    public static void setDefault(){
        BaseFrameManagement.kernel = KERNEL_STD;
        BaseFrameManagement.correction = 300.0f;
        BaseFrameManagement.gamma = WindowConfig.getInstance().getGamma();
        BaseFrameManagement.contrast = WindowConfig.getInstance().getContrast();
        BaseFrameManagement.tune = 1.0f;
    }

    public static int getKernel() {
        return kernel;
    }

    public static void setKernel(int kernel) {
        BaseFrameManagement.kernel = kernel;
    }

    public static float getCorrection() {
        return correction;
    }

    public static void setCorrection(float correction) {
        BaseFrameManagement.correction = correction;
    }

    public static float getGamma() {
        return gamma;
    }

    public static void setGamma(float gamma) {
        BaseFrameManagement.gamma = gamma;
    }

    public static float getContrast() {
        return contrast;
    }

    public static void setContrast(float contrast) {
        BaseFrameManagement.contrast = contrast;
    }

    public static float getTune() {
        return tune;
    }

    public static void setTune(float tune) {
        BaseFrameManagement.tune = tune;
    }
}
