package com.mahmoud_ahmed.utils;

import java.util.*;
import java.util.stream.Collectors;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.ProcessMetrics;

public class SchedulingUtil {

    private SchedulingUtil() {

    }

    public static List<Process> sortedProcessesByArrivalTime(Collection<Process> processes) {
        List<Process> sortedProcesses = new LinkedList<>(processes);
        Collections.sort(sortedProcesses);
        return sortedProcesses;
    }

    public static double calculateAverageWaitingTime(List<ProcessMetrics> processMetrics) {
        return processMetrics.stream()
                .mapToInt(ProcessMetrics::getWaitingTime)
                .sum()
                / (double) processMetrics.size();
    }

    public static double calculateAverageTurnaroundTime(List<ProcessMetrics> processMetrics) {
        return processMetrics.stream()
                .mapToInt(ProcessMetrics::getTurnaroundTime)
                .sum()
                / (double) processMetrics.size();
    }

    public static List<ProcessMetrics> assembleExecutionSegments(List<ExecutionSegment> executionSegments) {
        Map<Long, List<ExecutionSegment>> processSegments = groupByProcess(executionSegments);
        return processSegments.values().stream()
                .map(SchedulingUtil::assembleProcessSegments)
                .collect(Collectors.toList());
    }

    public static Map<Long, List<ExecutionSegment>> groupByProcess(List<ExecutionSegment> executionSegments) {
        Map<Long, List<ExecutionSegment>> processSegments = new HashMap<>();

        for (ExecutionSegment segment : executionSegments) {
            long processId = segment.process().getProcessId();
            processSegments.computeIfAbsent(processId, k -> new LinkedList<>()).add(segment);
        }

        return processSegments;
    }

    public static ProcessMetrics assembleProcessSegments(List<ExecutionSegment> segments) {
        Process process = segments.getFirst().process();
        int startExecutionTime = segments.getFirst().startTime();
        int completionTime = segments.getLast().endTime();
        return new ProcessMetrics(process, startExecutionTime, completionTime);
    }
}
