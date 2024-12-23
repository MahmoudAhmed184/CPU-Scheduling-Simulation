package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.SchedulingClock;
import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.utils.SchedulingUtil;

public class PreemptivePriority implements SchedulingAlgorithm {
    private final Queue<Process> readyQueue;

    public PreemptivePriority() {
        readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority)
            .thenComparing(Comparator.naturalOrder()));
    }

    @Override
    public List<ExecutionSegment> schedule(List<Process> processes) {
        List<Process> sortedProcesses = SchedulingUtil.sortProcessesByArrivalTime(processes);
        List<ExecutionSegment> executionSegments = new LinkedList<>();

        Process runningProcess = null;
        SchedulingClock clock = new SchedulingClock();
        int startExecutionTime = 0;

        while (!sortedProcesses.isEmpty() || !readyQueue.isEmpty() || runningProcess != null) {
            SchedulingUtil.addArrivedProcessesToReadyQueue(sortedProcesses, readyQueue, clock);

            if (SchedulingUtil.isCpuInIdleState(sortedProcesses, readyQueue, runningProcess)) {
                SchedulingUtil.handleIdleState(clock, sortedProcesses.getFirst());
                continue;
            }

            if (!readyQueue.isEmpty() && shouldPreempt(runningProcess, readyQueue.peek())) {
                executionSegments.add(new ExecutionSegment(runningProcess, startExecutionTime, clock.getCurrentTime()));
                readyQueue.add(runningProcess);
                runningProcess = null;
            }

            if (runningProcess == null && !readyQueue.isEmpty()) {
                runningProcess = readyQueue.poll();
                startExecutionTime = clock.getCurrentTime();
            }
            clock.increment();
            runningProcess.setRemainingTime(runningProcess.getRemainingTime() - 1);
            if (runningProcess.getRemainingTime() == 0) {
                executionSegments.add(new ExecutionSegment(runningProcess, startExecutionTime, clock.getCurrentTime()));
                runningProcess.resetRemainingTime();
                runningProcess = null;
            }
        }
        return executionSegments;
    }

    private static boolean shouldPreempt(Process activeProcess, Process arrivedProcess) {
        return activeProcess != null && activeProcess.getPriority() > arrivedProcess.getPriority();
    }
}