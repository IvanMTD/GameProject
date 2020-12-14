package ru.phoenix.engine.core.configuration;

import java.io.*;

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
    private String langueage;

    public WindowConfig() throws IOException, ClassNotFoundException {
        File direct = new File("./data/save/config");
        File file = new File(direct,"WindowConfig.ser");
        if(file.exists()){
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream("./data/save/config/WindowConfig.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            readExternal(objectInputStream);
            objectInputStream.close();
        }else{
            setDefault();
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream("./data/save/config/WindowConfig.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            writeExternal(objectOutputStream);
            objectOutputStream.close();
        }
    }

    public void setDefault(){
        setFullScreen(false);
        setWidth(1280);
        setHeight(720);
        setSamples(4);
        setGamma(2.2f);
        setContrast(0.0f);
        setzNear(0.01f);
        setzFar(300.0f);
        setLangueage("ENGLISH");
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

    public String getLangueage() {
        return langueage;
    }

    public void setLangueage(String langueage) {
        this.langueage = langueage;
    }

    public static WindowConfig getInstance(){
        if(instance == null){
            try {
                instance = new WindowConfig();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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
        out.writeObject(getLangueage());
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
        setLangueage((String)in.readObject());
    }
}
