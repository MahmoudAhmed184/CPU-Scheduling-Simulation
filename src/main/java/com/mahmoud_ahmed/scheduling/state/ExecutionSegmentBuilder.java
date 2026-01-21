package com.mahmoud_ahmed.scheduling.state;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;

public class ExecutionSegmentBuilder {
    private Process process;
    private int startTime;
    private int endTime;

    public ExecutionSegmentBuilder() {
    }

    public ExecutionSegmentBuilder withProcess(Process process) {
        this.process = process;
        return this;
    }

    public ExecutionSegmentBuilder withStartTime(int startTime) {
        this.startTime = startTime;
        return this;
    }

    public ExecutionSegmentBuilder withEndTime(int endTime) {
        this.endTime = endTime;
        return this;
    }

    public ExecutionSegment build() {
        return new ExecutionSegment(process, startTime, endTime);
    }
}
