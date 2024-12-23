package com.mahmoud_ahmed.model;

import java.util.List;

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
        this.turnaroundTime = completionTime - process.getArrivalTime();
        this.waitingTime = turnaroundTime - process.getBurstTime();
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

    public List<String> toList() {
        return List.of(
                Integer.toString(process.getProcessNumber()),
                Integer.toString(startExecutionTime),
                Integer.toString(completionTime),
                Integer.toString(turnaroundTime),
                Integer.toString(waitingTime));
    }
}
