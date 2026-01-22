package com.mahmoud_ahmed.scheduling.metrics;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.ProcessMetrics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetricsCalculator {

    private MetricsCalculator() {
    }

    public static List<ProcessMetrics> calculateMetrics(List<ExecutionSegment> segments) {
        return groupSegmentsByProcess(segments).values().stream()
                .map(MetricsCalculator::calculateProcessMetrics)
                .collect(Collectors.toList());
    }

    private static Map<Integer, List<ExecutionSegment>> groupSegmentsByProcess(List<ExecutionSegment> segments) {
        return segments.stream()
                .collect(Collectors.groupingBy(segment -> segment.process().processNumber()));
    }

    private static ProcessMetrics calculateProcessMetrics(List<ExecutionSegment> segments) {
        return new ProcessMetrics(
                segments.getFirst().process(),
                segments.getFirst().startTime(),
                segments.getLast().endTime());
    }

    public static double calculateAverageWaitingTime(List<ProcessMetrics> metrics) {
        return metrics.stream()
                .mapToInt(ProcessMetrics::getWaitingTime)
                .average()
                .orElse(0.0);
    }

    public static double calculateAverageTurnaroundTime(List<ProcessMetrics> metrics) {
        return metrics.stream()
                .mapToInt(ProcessMetrics::getTurnaroundTime)
                .average()
                .orElse(0.0);
    }
}
