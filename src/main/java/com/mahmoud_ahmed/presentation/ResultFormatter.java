package com.mahmoud_ahmed.presentation;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.ProcessMetrics;

import java.util.List;

public final class ResultFormatter {

    private ResultFormatter() {
    }

    public static List<String> formatSegment(ExecutionSegment segment) {
        return List.of(
                Integer.toString(segment.process().processNumber()),
                Integer.toString(segment.startTime()),
                Integer.toString(segment.endTime()));
    }

    public static List<String> formatMetrics(ProcessMetrics metrics) {
        return List.of(
                Integer.toString(metrics.getProcess().processNumber()),
                Integer.toString(metrics.getStartExecutionTime()),
                Integer.toString(metrics.getCompletionTime()),
                Integer.toString(metrics.getTurnaroundTime()),
                Integer.toString(metrics.getWaitingTime()));
    }
}
