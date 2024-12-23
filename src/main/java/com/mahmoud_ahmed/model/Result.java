package com.mahmoud_ahmed.model;

import java.util.List;

import com.mahmoud_ahmed.utils.SchedulingUtil;

public class Result {
    private final List<ExecutionSegment> executionSegments;
    private final List<ProcessMetrics> metrics;
    private final double algorithmAverageWaitingTime;
    private final double algorithmAverageTurnaroundTime;

    public Result(List<ExecutionSegment> executionSegments) {
        this.executionSegments = executionSegments;
        this.metrics = SchedulingUtil.assembleExecutionSegments(executionSegments);
        this.algorithmAverageWaitingTime = SchedulingUtil.calculateAverageWaitingTime(metrics);
        this.algorithmAverageTurnaroundTime = SchedulingUtil.calculateAverageTurnaroundTime(metrics);
    }

    public List<ExecutionSegment> getExecutionSegments() {
        return executionSegments;
    }

    public List<ProcessMetrics> getMetrics() {
        return metrics;
    }

    public double getAlgorithmAverageWaitingTime() {
        return algorithmAverageWaitingTime;
    }

    public double getAlgorithmAverageTurnaroundTime() {
        return algorithmAverageTurnaroundTime;
    }
}
