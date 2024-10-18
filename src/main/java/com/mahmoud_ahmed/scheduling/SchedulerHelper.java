package com.mahmoud_ahmed.scheduling;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ScheduledProcess;

public class SchedulerHelper {
    private SchedulerHelper() {

    }

    static List<Process> sortProcessesByArrivalTime(Collection<Process> processes) {
        List<Process> sortedProcesses = new LinkedList<>(processes);
        Collections.sort(sortedProcesses);
        return sortedProcesses;
    }

    static ScheduledProcess scheduleProcess(Process process, int startExecutionTime) {
        int completionTime = startExecutionTime + process.getBurstTime();
        int turnaroundTime = completionTime - process.getArrivalTime();
        int waitingTime = turnaroundTime - process.getBurstTime();
        return new ScheduledProcess(process, startExecutionTime, completionTime, turnaroundTime, waitingTime);
    }

    static boolean isCpuInIdleState(List<Process> sortedProcesses, Queue<Process> readyQueue,
            Process runningProcess) {
        return !sortedProcesses.isEmpty() && readyQueue.isEmpty() && runningProcess == null;
    }

    static int handleIdleState(Process firstArrivedProcess) {
        return firstArrivedProcess.getArrivalTime();
    }

    static void handleTimeQuantumExpiration(Process process, Queue<Process> readyQueue, List<Process> processes,
            int timeQuantum, int currentTime) {
        process.setRemainingTime(process.getRemainingTime() - timeQuantum);
        addArrivedProcessesToReadyQueue(processes, readyQueue, currentTime);
        readyQueue.add(process);
    }

    static boolean shouldPreempt(Process activeProcess, Process arrivedProcess) {
        return activeProcess != null && activeProcess.getPriority() > arrivedProcess.getPriority();
    }

    static void addArrivedProcessesToReadyQueue(List<Process> sortedProcesses, Queue<Process> readyQueue,
            int currentTime) {
        while (!sortedProcesses.isEmpty() && sortedProcesses.getFirst().getArrivalTime() <= currentTime) {
            readyQueue.add(sortedProcesses.removeFirst());
        }
    }
}
