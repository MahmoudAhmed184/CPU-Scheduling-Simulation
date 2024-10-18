package com.mahmoud_ahmed.scheduling;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ScheduledProcess;
import com.mahmoud_ahmed.model.SchedulingClock;

public class SchedulerHelper {
    private SchedulerHelper() {

    }

    static List<Process> sortProcessesByArrivalTime(Collection<Process> processes) {
        List<Process> sortedProcesses = new LinkedList<>(processes);
        Collections.sort(sortedProcesses);
        return sortedProcesses;
    }

    static ScheduledProcess scheduleProcess(Process process, SchedulingClock clock) {
        int startExecutionTime = clock.getCurrentTime();
        int completionTime = startExecutionTime + process.getBurstTime();
        int turnaroundTime = completionTime - process.getArrivalTime();
        int waitingTime = turnaroundTime - process.getBurstTime();
        clock.advance(process.getBurstTime());
        return new ScheduledProcess(process, startExecutionTime, completionTime, turnaroundTime, waitingTime);
    }

    static boolean isCpuInIdleState(List<Process> sortedProcesses, Queue<Process> readyQueue,
            Process runningProcess) {
        return !sortedProcesses.isEmpty() && readyQueue.isEmpty() && runningProcess == null;
    }

    static void handleIdleState(SchedulingClock clock, Process firstArrivedProcess) {
        clock.setTime(firstArrivedProcess.getArrivalTime());
    }

    static void handleTimeQuantumExpiration(Process process, Queue<Process> readyQueue, List<Process> processes,
            SchedulingClock clock, int timeQuantum) {
        clock.advance(timeQuantum);
        process.setRemainingTime(process.getRemainingTime() - timeQuantum);
        addArrivedProcessesToReadyQueue(processes, readyQueue, clock);
        readyQueue.add(process);
    }

    static boolean shouldPreempt(Process activeProcess, Process arrivedProcess) {
        return activeProcess != null && activeProcess.getPriority() > arrivedProcess.getPriority();
    }

    static void addArrivedProcessesToReadyQueue(List<Process> sortedProcesses, Queue<Process> readyQueue,
            SchedulingClock clock) {
        while (!sortedProcesses.isEmpty() && clock.isBeforeOrAt(sortedProcesses.getFirst().getArrivalTime())) {
            readyQueue.add(sortedProcesses.removeFirst());
        }
    }
}
