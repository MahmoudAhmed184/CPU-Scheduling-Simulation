package com.mahmoud_ahmed.model;

import java.util.Comparator;

/**
 * Represents a process in the CPU scheduling simulation.
 * Each process has attributes for scheduling decisions and execution tracking.
 */
public final class Process implements Comparable<Process> {
    private final int processNumber;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;
    private int remainingTime;

    /**
     * Creates a new process with the specified attributes.
     *
     * @param processNumber unique identifier for the process
     * @param arrivalTime   time when the process arrives in the ready queue
     * @param burstTime     total CPU time required by the process
     * @param priority      priority level (lower values indicate higher priority)
     */
    public Process(int processNumber, int arrivalTime, int burstTime, int priority) {
        this.processNumber = processNumber;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    /**
     * Copy constructor for creating a copy of an existing process.
     *
     * @param other the process to copy
     */
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