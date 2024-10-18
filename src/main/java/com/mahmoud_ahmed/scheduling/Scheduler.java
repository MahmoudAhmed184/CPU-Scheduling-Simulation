package com.mahmoud_ahmed.scheduling;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ScheduledProcess;

public class Scheduler {

    private Scheduler() {

    }

    public static List<ScheduledProcess> scheduleFirstComeFirstServe(Collection<Process> processes) {
        return scheduleNonPreemptiveAlgorithm(processes, Comparator.naturalOrder());
    }

    public static List<ScheduledProcess> scheduleShortestJobFirst(Collection<Process> processes) {
        return scheduleNonPreemptiveAlgorithm(processes,
                Comparator.comparingInt(Process::getBurstTime).thenComparing(Comparator.naturalOrder()));
    }

    public static List<ScheduledProcess> scheduleNonPreemptivePriority(Collection<Process> processes) {
        return scheduleNonPreemptiveAlgorithm(processes,
                Comparator.comparingInt(Process::getPriority).thenComparing(Comparator.naturalOrder()));
    }

    private static List<ScheduledProcess> scheduleNonPreemptiveAlgorithm(Collection<Process> processes, Comparator<Process> comparator) {
        List<Process> sortedProcesses = sortProcessesByArrivalTime(processes);
        var readyQueue = new PriorityQueue<>(comparator);

        List<ScheduledProcess> scheduledProcesses = new LinkedList<>();
        Process runningProcess = null;

        int currentTime = 0;

        while (!sortedProcesses.isEmpty() || !readyQueue.isEmpty()) {
            addArrivedProcessesToReadyQueue(sortedProcesses, readyQueue, currentTime);

            if (isCpuInIdleState(sortedProcesses, readyQueue, runningProcess)) {
                currentTime = handleIdleState(sortedProcesses.getFirst());
                continue;
            }

            runningProcess = readyQueue.poll();
            scheduledProcesses.add(scheduleProcess(runningProcess, currentTime));
            currentTime += runningProcess.getBurstTime();
            runningProcess = null;
        }

        return scheduledProcesses;
    }

    private static List<Process> sortProcessesByArrivalTime(Collection<Process> processes) {
        List<Process> sortedProcesses = new LinkedList<>(processes);
        Collections.sort(sortedProcesses);
        return sortedProcesses;
    }

    private static void addArrivedProcessesToReadyQueue(List<Process> sortedProcesses, Queue<Process> readyQueue,
            int currentTime) {
        while (!sortedProcesses.isEmpty() && sortedProcesses.getFirst().getArrivalTime() <= currentTime) {
            readyQueue.add(sortedProcesses.removeFirst());
        }
    }

    private static boolean isCpuInIdleState(List<Process> sortedProcesses, Queue<Process> readyQueue,
            Process runningProcess) {
        return !sortedProcesses.isEmpty() && readyQueue.isEmpty() && runningProcess == null;
    }

    private static int handleIdleState(Process firstArrivedProcess) {
        return firstArrivedProcess.getArrivalTime();
    }

    private static ScheduledProcess scheduleProcess(Process process, int startExecutionTime) {
        int completionTime = startExecutionTime + process.getBurstTime();
        int turnaroundTime = completionTime - process.getArrivalTime();
        int waitingTime = turnaroundTime - process.getBurstTime();
        return new ScheduledProcess(process, startExecutionTime, completionTime, turnaroundTime, waitingTime);
    }

    public static List<ExecutionSegment> scheduleRoundRobin(Collection<Process> processes, int timeQuantum) {
        return null;
    }

    public static List<ExecutionSegment> schedulePreemptivePriority(Collection<Process> processes) {
        return null;
    }

}
