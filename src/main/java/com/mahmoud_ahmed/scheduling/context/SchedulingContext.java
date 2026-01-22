package com.mahmoud_ahmed.scheduling.context;

import com.mahmoud_ahmed.model.Process;

import java.util.Comparator;

public class SchedulingContext implements Comparable<SchedulingContext> {
    private final Process process;
    private int remainingTime;
    private int elapsedTime;

    public SchedulingContext(Process process) {
        this.process = process;
        this.remainingTime = process.burstTime();
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
        return process.processNumber();
    }

    public int getArrivalTime() {
        return process.arrivalTime();
    }

    public int getBurstTime() {
        return process.burstTime();
    }

    public int getPriority() {
        return process.priority();
    }

    @Override
    public int compareTo(SchedulingContext that) {
        return Comparator.comparingInt(SchedulingContext::getArrivalTime)
                .thenComparingInt(SchedulingContext::getProcessNumber)
                .compare(this, that);
    }
}
