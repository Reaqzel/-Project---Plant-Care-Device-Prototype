package com.project;

public class Settings {
    private boolean temp_b;
    private boolean hum_b;
    private boolean light_b;
    private int temp_interval;
    private int hum_interval;
    private int light_interval;
    private int irrigation_interval;

    // Constructor with no arguments
    public Settings() {}

    // Getters
    public boolean isTempB() {
        return temp_b;
    }

    public boolean isHumB() {
        return hum_b;
    }

    public boolean isLightB() {
        return light_b;
    }

    public int getTempInterval() {
        return temp_interval;
    }

    public int getHumInterval() {
        return hum_interval;
    }

    public int getLightInterval() {
        return light_interval;
    }
    public int getirrigationinterval(){
        return irrigation_interval;
    }

    // Setters
    public void setTempB(boolean temp_b) {
        this.temp_b = temp_b;
    }

    public void setHumB(boolean hum_b) {
        this.hum_b = hum_b;
    }

    public void setLightB(boolean light_b) {
        this.light_b = light_b;
    }

    public void setTempInterval(int temp_interval) {
        this.temp_interval = temp_interval;
    }

    public void setHumInterval(int hum_interval) {
        this.hum_interval = hum_interval;
    }

    public void setLightInterval(int light_interval) {
        this.light_interval = light_interval;
    }
    public void setirrigationinterval(int irrigation_interval){
        this.irrigation_interval = irrigation_interval;
    }

    @Override
    public String toString() {
        return "Settings has this {" +
                "temp_b=" + temp_b +
                ", hum_b=" + hum_b +
                ", light_b=" + light_b +
                ", temp_interval=" + temp_interval +
                ", hum_interval=" + hum_interval +
                ", light_interval=" + light_interval +
                ", irrigation_interval=" + irrigation_interval +
                '}';
    }

}
