package ru.phoenix.engine.core.configuration;

import ru.phoenix.engine.core.loader.text.Alphabet;
import ru.phoenix.engine.core.loader.text.Letter;

import java.awt.*;
import java.io.*;

import static ru.phoenix.engine.core.constants.System.ENGLISH_LANGUAGE;

public class WindowConfig implements Externalizable {

    private static final long serialVersionUID = 1L;

    private static transient WindowConfig instance = null;

    private boolean fullScreen;
    private int width;
    private int height;
    private int samples;
    private float gamma;
    private float contrast;
    private float zNear;
    private float zFar;
    private int language;

    public WindowConfig() throws IOException, ClassNotFoundException {
        File direct = new File("./data/save/config");
        File file = new File(direct,"WindowConfig.ser");
        if(file.exists()){
            System.out.println("\"Window Config\" file exist");
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream("./data/save/config/WindowConfig.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            readExternal(objectInputStream);
            objectInputStream.close();
        }else{
            System.out.println("\"Window Config\" file not exist");
            setDefault();
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream("./data/save/config/WindowConfig.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            writeExternal(objectOutputStream);
            objectOutputStream.close();
        }
        setDevelopConfig();
    }

    public void setDevelopConfig(){
        setFullScreen(false);
        setWidth(1280);
        setHeight(720);
        setSamples(4);
        setGamma(2.2f);
        setContrast(0.0f);
        setzNear(0.01f);
        setzFar(300.0f);
        setLanguage(ENGLISH_LANGUAGE);
    }

    public void setDefault(){
        setFullScreen(true);
        setWidth(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth());
        setHeight(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());
        setSamples(4);
        setGamma(2.2f);
        setContrast(0.0f);
        setzNear(0.01f);
        setzFar(300.0f);
        setLanguage(ENGLISH_LANGUAGE);
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSamples() {
        return samples;
    }

    public void setSamples(int samples) {
        this.samples = samples;
    }

    public float getGamma() {
        return gamma;
    }

    public void setGamma(float gamma) {
        this.gamma = gamma;
    }

    public float getContrast() {
        return contrast;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    public float getzNear() {
        return zNear;
    }

    public void setzNear(float zNear) {
        this.zNear = zNear;
    }

    public float getzFar() {
        return zFar;
    }

    public void setzFar(float zFar) {
        this.zFar = zFar;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public void setExtendedLanguage(int language){
        this.language = language;
        Alphabet.getInstance().setCurrentLanguage();
    }

    public void save() throws IOException {
        File direct = new File("./data/save/config");
        File file = new File(direct,"WindowConfig.ser");
        if(file.exists()){
            file.delete();
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream("./data/save/config/WindowConfig.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            writeExternal(objectOutputStream);
            objectOutputStream.close();
        }else{
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream("./data/save/config/WindowConfig.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            writeExternal(objectOutputStream);
            objectOutputStream.close();
        }
    }

    public static WindowConfig getInstance(){
        if(instance == null){
            try {
                instance = new WindowConfig();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(isFullScreen());
        out.writeObject(getWidth());
        out.writeObject(getHeight());
        out.writeObject(getSamples());
        out.writeObject(getGamma());
        out.writeObject(getContrast());
        out.writeObject(getzNear());
        out.writeObject(getzFar());
        out.writeObject(getLanguage());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setFullScreen((boolean)in.readObject());
        setWidth((int)in.readObject());
        setHeight((int)in.readObject());
        setSamples((int)in.readObject());
        setGamma((float)in.readObject());
        setContrast((float)in.readObject());
        setzNear((float)in.readObject());
        setzFar((float)in.readObject());
        setLanguage((int)in.readObject());
    }
}
