package com.mahmoud_ahmed.model;

import java.util.List;

public record Result(List<ExecutionSegment> executionSegments, List<ProcessMetrics> metrics,
                     double algorithmAverageWaitingTime, double algorithmAverageTurnaroundTime) {
}
