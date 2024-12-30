package com.mahmoud_ahmed.scheduling.algorithms;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.SchedulingClock;
import com.mahmoud_ahmed.utils.SchedulingUtil;

import java.util.*;

public abstract class PreemptiveAlgorithm implements SchedulingAlgorithm {
    private final Queue<Process> readyQueue;

    public PreemptiveAlgorithm(Comparator<Process> comparator) {
        this.readyQueue = comparator == null ? new LinkedList<>() : new PriorityQueue<>(comparator);
    }

    @Override
    public List<ExecutionSegment> schedule(List<Process> processes) {
        List<Process> sortedProcesses = SchedulingUtil.sortedProcessesByArrivalTime(processes);
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

    abstract boolean shouldPreempt(Process activeProcess, Process arrivedProcess);
}
