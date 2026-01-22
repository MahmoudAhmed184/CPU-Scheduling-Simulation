package com.mahmoud_ahmed.model;

import java.util.Comparator;

public final class Process implements Comparable<Process> {
    private final int processNumber;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;
    private int remainingTime;

    public Process(int processNumber, int arrivalTime, int burstTime, int priority) {
        this.processNumber = processNumber;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    public Process(Process other) {
        this.processNumber = other.processNumber;
        this.arrivalTime = other.arrivalTime;
        this.burstTime = other.burstTime;
        this.priority = other.priority;
        this.remainingTime = other.remainingTime;
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
                .thenComparingInt(Process::getProcessNumber)
                .compare(this, that);
    }
}