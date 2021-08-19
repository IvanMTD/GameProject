package ru.phoenix.engine.core.buffer.util;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time {
    private static float lastTime = 0;

    private static int day = 0;
    private static int hour = 0;
    private static int minute = 0;
    private static float second = 0;
    private static float nonStopSecond = 0;

    public static void update(){
        float currentTime = (float)glfwGetTime();
        float deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        second += deltaTime;
        nonStopSecond += deltaTime;

        if(second >= 60){
            minute++;
            second = 0;
        }

        if(minute >= 60){
            hour++;
            minute = 0;
        }

        if(hour >= 24){
            day++;
            hour = 0;
        }
    }

    public static void setDay(int day) {
        Time.day = day;
    }

    public static void setHour(int hour) {
        Time.hour = hour;
    }

    public static void setMinute(int minut) {
        Time.minute = minut;
    }

    public static void setSecond(float second) {
        Time.second = second;
    }

    public static int getDay() {
        return day;
    }

    public static int getHour() {
        return hour;
    }

    public static int getMinute() {
        return minute;
    }

    public static int getSecond() {
        return (int)second;
    }

    public static float getNonStopSecond() {
        return nonStopSecond;
    }

    public static String getCurrentTime(){
        String d = day > 9 ? Integer.toString(day) : "0" + day;
        String h = hour > 9 ? Integer.toString(hour) : "0" + hour;
        String m = minute > 9 ? Integer.toString(minute) : "0" + minute;
        String s = (int)second > 9 ? Integer.toString((int)second) : "0" + (int)second;
        return " day: " + d + " time: " + h + ":" + m + ":" + s;
    }
}
