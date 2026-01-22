package com.mahmoud_ahmed.scheduling.context;

import com.mahmoud_ahmed.model.Process;

public class SchedulingContext {
    private final Process process;
    private int remainingTime;
    private int elapsedTime;

    public SchedulingContext(Process process) {
        this.process = process;
        this.remainingTime = process.getBurstTime();
        this.elapsedTime = 0;
    }

    public Process getProcess() {
        return process;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void executeFor(int time) {
        this.remainingTime -= time;
        this.elapsedTime += time;
    }

    public boolean hasFinished() {
        return remainingTime == 0;
    }

    public int getProcessNumber() {
        return process.getProcessNumber();
    }

    public int getArrivalTime() {
        return process.getArrivalTime();
    }

    public int getBurstTime() {
        return process.getBurstTime();
    }

    public int getPriority() {
        return process.getPriority();
    }
}
