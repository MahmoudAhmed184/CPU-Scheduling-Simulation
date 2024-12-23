package com.mahmoud_ahmed.utils;

import java.util.*;
import java.util.stream.Collectors;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.ProcessMetrics;
import com.mahmoud_ahmed.model.SchedulingClock;

public class SchedulingUtil {

    private SchedulingUtil() {

    }
    public static List<Process> sortProcessesByArrivalTime(Collection<Process> processes) {
        List<Process> sortedProcesses = new LinkedList<>(processes);
        Collections.sort(sortedProcesses);
        return sortedProcesses;
    }

    public static boolean isCpuInIdleState(List<Process> processes, Queue<Process> readyQueue, Process activeProcess) {
        return !processes.isEmpty() && readyQueue.isEmpty() && activeProcess == null;
    }

    public static void handleIdleState(SchedulingClock clock, Process firstArrivedProcess) {
        clock.setTime(firstArrivedProcess.getArrivalTime());
    }

    public static void addArrivedProcessesToReadyQueue(List<Process> sortedProcesses, Queue<Process> readyQueue,
                                                       SchedulingClock clock) {
        while (!sortedProcesses.isEmpty() && clock.isBeforeOrAt(sortedProcesses.getFirst().getArrivalTime())) {
            readyQueue.add(sortedProcesses.removeFirst());
        }
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
            long processId = segment.getProcess().getProcessId();
            processSegments.computeIfAbsent(processId, k -> new LinkedList<>()).add(segment);
        }

        return processSegments;
    }

    public static ProcessMetrics assembleProcessSegments(List<ExecutionSegment> segments) {
        Process process = segments.getFirst().getProcess();
        int startExecutionTime = segments.getFirst().getStartTime();
        int completionTime = segments.getLast().getEndTime();
        return new ProcessMetrics(process, startExecutionTime, completionTime);
    }
}
