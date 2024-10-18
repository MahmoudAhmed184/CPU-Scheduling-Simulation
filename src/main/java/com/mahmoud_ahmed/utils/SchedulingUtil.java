package com.mahmoud_ahmed.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.ScheduledProcess;

public class SchedulingUtil {

    private SchedulingUtil() {

    }

    public static double calculateAverageWaitingTime(List<ScheduledProcess> scheduledProcesses) {
        return scheduledProcesses.stream()
                .mapToInt(ScheduledProcess::getWaitingTime)
                .sum()
                / (double) scheduledProcesses.size();
    }

    public static double calculateAverageTurnaroundTime(List<ScheduledProcess> scheduledProcesses) {
        return scheduledProcesses.stream()
                .mapToInt(ScheduledProcess::getTurnaroundTime)
                .sum()
                / (double) scheduledProcesses.size();
    }

    public static List<ScheduledProcess> assembleExecutionSegments(List<ExecutionSegment> executionSegments) {
        Map<Long, List<ExecutionSegment>> processSegments = groupByProcess(executionSegments);
        return processSegments.values().stream()
                .map(SchedulingUtil::assembleProcessSegments)
                .collect(Collectors.toList());
    }

    public static Map<Long, List<ExecutionSegment>> groupByProcess(List<ExecutionSegment> executionSegments) {
        Map<Long, List<ExecutionSegment>> processSegments = new HashMap<>();

        for (ExecutionSegment segment : executionSegments) {
            long processId = segment.getProcess().getProcessId();
            processSegments.computeIfAbsent(processId, k -> new LinkedList<>()).add(segment);
        }

        return processSegments;
    }

    public static ScheduledProcess assembleProcessSegments(List<ExecutionSegment> segments) {
        Process process = segments.getFirst().getProcess();
        int startExecutionTime = segments.getFirst().getStartTime();
        int completionTime = segments.getLast().getEndTime();
        int turnaroundTime = completionTime - process.getArrivalTime();
        int waitingTime = turnaroundTime - process.getBurstTime();

        return new ScheduledProcess(process, startExecutionTime, completionTime, turnaroundTime, waitingTime);
    }
}
