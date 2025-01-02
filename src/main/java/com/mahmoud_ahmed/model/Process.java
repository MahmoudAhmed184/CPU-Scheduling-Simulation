package com.mahmoud_ahmed.model;

import java.util.Comparator;

public final class Process implements Comparable<Process> {
    private static long counter = 0;
    private final long processId;
    private final int processNumber;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;
    private int remainingTime;

    public Process(int processNumber, int arrivalTime, int burstTime, int priority) {
        this.processId = counter++;
        this.processNumber = processNumber;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    public Process(Process other) {
        this.processId = counter++;
        this.processNumber = other.processNumber;
        this.arrivalTime = other.arrivalTime;
        this.burstTime = other.burstTime;
        this.priority = other.priority;
        this.remainingTime = other.remainingTime;
    }

    public long getProcessId() {
        return this.processId;
    }

    public int getProcessNumber() {
        return this.processNumber;
    }

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    public int getBurstTime() {
        return this.burstTime;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    @Override
    public int compareTo(Process that) {
        return Comparator.comparingInt(Process::getArrivalTime)
            .thenComparingLong(Process::getProcessId)
            .compare(this, that);
    }
}