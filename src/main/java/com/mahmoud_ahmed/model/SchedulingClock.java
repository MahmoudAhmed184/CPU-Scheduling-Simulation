package com.mahmoud_ahmed.model;

public class SchedulingClock {
    private int currentTime;

    public SchedulingClock() {
        this.currentTime = 0;
    }

    public int getCurrentTime() {
        return this.currentTime;
    }

    public void increment() {
        this.currentTime++;
    }

    public void advance(int timeIncrement) {
        this.currentTime += timeIncrement;
    }

    public void reset() {
        this.currentTime = 0;
    }

    public void setTime(int newTime) {
        this.currentTime = newTime;
    }

    public boolean isBeforeOrAt(int time) {
        return time <= this.currentTime;
    }

    @Override
    public String toString() {
        return String.valueOf(currentTime);
    }
}
