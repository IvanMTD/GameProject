package ru.phoenix.engine.core.configuration;

import java.io.*;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class ActiveButtons implements Externalizable {

    private static final long serialVersionUID = 1L;

    private transient static ActiveButtons instance = null;

    // mouse buttons
    private int mouse_1;
    private int mouse_2;

    // keyboard buttons

    public ActiveButtons() throws IOException, ClassNotFoundException {
        File direct = new File("./data/save/config");
        File file = new File(direct,"ActiveButtons.ser");
        if(file.exists()){
            System.out.println("\"Active Buttons\" file exist");
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream("./data/save/config/ActiveButtons.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            readExternal(objectInputStream);
            objectInputStream.close();
        }else{
            System.out.println("\"Active Buttons\" file not exist");
            setDefault();
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream("./data/save/config/ActiveButtons.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            writeExternal(objectOutputStream);
            objectOutputStream.close();
        }
    }

    private void setDefault(){
        setMouse_1(GLFW_MOUSE_BUTTON_LEFT);
        setMouse_2(GLFW_MOUSE_BUTTON_RIGHT);
    }

    public int getMouse_1() {
        return mouse_1;
    }

    public void setMouse_1(int mouse_1) {
        this.mouse_1 = mouse_1;
    }

    public int getMouse_2() {
        return mouse_2;
    }

    public void setMouse_2(int mouse_2) {
        this.mouse_2 = mouse_2;
    }

    public void save() throws IOException {
        File direct = new File("./data/save/config");
        File file = new File(direct,"ActiveButtons.ser");
        if(file.exists()){
            file.delete();
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream("./data/save/config/ActiveButtons.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            writeExternal(objectOutputStream);
            objectOutputStream.close();
        }else{
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream("./data/save/config/ActiveButtons.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            writeExternal(objectOutputStream);
            objectOutputStream.close();
        }
    }

    public static ActiveButtons getInstance() {
        if(instance == null){
            try {
                instance = new ActiveButtons();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getMouse_1());
        out.writeObject(getMouse_2());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setMouse_1((int)in.readObject());
        setMouse_2((int)in.readObject());
    }
}
