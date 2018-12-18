package com.example.douglas.vaichoverhoje;

public class Weather {
    private int currentTemp, minMorningTemp, maxMorningTemp,
    minAfternoonTemp, maxAfternoonTemp, minNightTemp,
    maxNightTemp, minTemp, maxTemp;

    private String date;

    public Weather(int currentTemp, int minMorningTemp, int maxMorningTemp,
                   int minAfternoonTemp, int maxAfternoonTemp, int minNightTemp,
                   int maxNightTemp, int minTemp, int maxTemp, String date) {
        this.currentTemp = currentTemp;
        this.minMorningTemp = minMorningTemp;
        this.maxMorningTemp = maxMorningTemp;
        this.minAfternoonTemp = minAfternoonTemp;
        this.maxAfternoonTemp = maxAfternoonTemp;
        this.minNightTemp = minNightTemp;
        this.maxNightTemp = maxNightTemp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.date = date;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public int getMinMorningTemp() {
        return minMorningTemp;
    }

    public void setMinMorningTemp(int minMorningTemp) {
        this.minMorningTemp = minMorningTemp;
    }

    public int getMaxMorningTemp() {
        return maxMorningTemp;
    }

    public void setMaxMorningTemp(int maxMorningTemp) {
        this.maxMorningTemp = maxMorningTemp;
    }

    public int getMinAfternoonTemp() {
        return minAfternoonTemp;
    }

    public void setMinAfternoonTemp(int minAfternoonTemp) {
        this.minAfternoonTemp = minAfternoonTemp;
    }

    public int getMaxAfternoonTemp() {
        return maxAfternoonTemp;
    }

    public void setMaxAfternoonTemp(int maxAfternoonTemp) {
        this.maxAfternoonTemp = maxAfternoonTemp;
    }

    public int getMinNightTemp() {
        return minNightTemp;
    }

    public void setMinNightTemp(int minNightTemp) {
        this.minNightTemp = minNightTemp;
    }

    public int getMaxNightTemp() {
        return maxNightTemp;
    }

    public void setMaxNightTemp(int maxNightTemp) {
        this.maxNightTemp = maxNightTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
