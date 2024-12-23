package com.mahmoud_ahmed.scheduling;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.SchedulingClock;

public class SchedulerHelper {
    private SchedulerHelper() {

    }

    public static List<Process> sortProcessesByArrivalTime(Collection<Process> processes) {
        List<Process> sortedProcesses = new LinkedList<>(processes);
        Collections.sort(sortedProcesses);
        return sortedProcesses;
    }

    public static ExecutionSegment scheduleProcess(Process process, SchedulingClock clock) {
        int startExecutionTime = clock.getCurrentTime();
        int completionTime = startExecutionTime + process.getBurstTime();
        clock.advance(process.getBurstTime());
        return new ExecutionSegment(process, startExecutionTime, completionTime);
    }

    public static boolean isCpuInIdleState(List<Process> sortedProcesses, Queue<Process> readyQueue,
                                    Process runningProcess) {
        return !sortedProcesses.isEmpty() && readyQueue.isEmpty() && runningProcess == null;
    }

    public static void handleIdleState(SchedulingClock clock, Process firstArrivedProcess) {
        clock.setTime(firstArrivedProcess.getArrivalTime());
    }

    public static void handleTimeQuantumExpiration(Process process, Queue<Process> readyQueue, List<Process> processes,
                                            SchedulingClock clock, int timeQuantum) {
        clock.advance(timeQuantum);
        process.setRemainingTime(process.getRemainingTime() - timeQuantum);
        addArrivedProcessesToReadyQueue(processes, readyQueue, clock);
        readyQueue.add(process);
    }

    public static boolean shouldPreempt(Process activeProcess, Process arrivedProcess) {
        return activeProcess != null && activeProcess.getPriority() > arrivedProcess.getPriority();
    }

    public static void addArrivedProcessesToReadyQueue(List<Process> sortedProcesses, Queue<Process> readyQueue,
                                                SchedulingClock clock) {
        while (!sortedProcesses.isEmpty() && clock.isBeforeOrAt(sortedProcesses.getFirst().getArrivalTime())) {
            readyQueue.add(sortedProcesses.removeFirst());
        }
    }
}
