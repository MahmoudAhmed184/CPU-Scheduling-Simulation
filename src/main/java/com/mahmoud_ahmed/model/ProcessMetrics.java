package com.mahmoud_ahmed.model;

public class ProcessMetrics {
    private final Process process;
    private final int startExecutionTime;
    private final int completionTime;
    private final int turnaroundTime;
    private final int waitingTime;

    public ProcessMetrics(Process process, int startExecutionTime, int completionTime) {
        this.process = process;
        this.startExecutionTime = startExecutionTime;
        this.completionTime = completionTime;
        this.turnaroundTime = completionTime - process.arrivalTime();
        this.waitingTime = turnaroundTime - process.burstTime();
    }

    public Process getProcess() {
        return this.process;
    }

    public int getStartExecutionTime() {
        return startExecutionTime;
    }

    public int getCompletionTime() {
        return this.completionTime;
    }

    public int getTurnaroundTime() {
        return this.turnaroundTime;
    }

    public int getWaitingTime() {
        return this.waitingTime;
    }
}
