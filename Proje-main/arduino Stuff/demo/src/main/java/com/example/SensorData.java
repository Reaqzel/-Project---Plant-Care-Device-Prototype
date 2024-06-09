package com.example;

public class SensorData {
    private double lightValue = 0;
    private double humidityValue = 0;
    private double temperatureValue = 0;

    public synchronized void setValues(double light, double humidity, double temperature) {
        this.lightValue = light;
        this.humidityValue = humidity;
        this.temperatureValue = temperature;
    }

    public synchronized double[] getValues() {
        return new double[] { lightValue, humidityValue, temperatureValue };
    }

    public synchronized double getTempValue(){
        return temperatureValue;
    }
    public synchronized double getLightValue(){
        return lightValue;
    }
    public synchronized double getHumpValue(){
        return humidityValue;
    }
}

