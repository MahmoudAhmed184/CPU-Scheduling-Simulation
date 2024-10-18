package com.mahmoud_ahmed.model;

import java.util.List;

public class ExecutionSegment {
    private final Process process;
    private final int startTime;
    private final int endTime;

    public ExecutionSegment(Process process, int startTime, int endTime) {
        this.process = process;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Process getProcess() {
        return this.process;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public int getEndTime() {
        return this.endTime;
    }

    public List<String> toList() {
        return List.of(
                Integer.toString(process.getProcessNumber()),
                Integer.toString(startTime),
                Integer.toString(endTime));
    }
}
