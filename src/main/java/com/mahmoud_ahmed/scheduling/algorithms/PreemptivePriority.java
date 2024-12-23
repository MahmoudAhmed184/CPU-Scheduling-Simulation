package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.SchedulingClock;
import com.mahmoud_ahmed.scheduling.SchedulerHelper;
import com.mahmoud_ahmed.model.ExecutionSegment;

public class PreemptivePriority implements SchedulingAlgorithm {
    private final Queue<Process> readyQueue;

    public PreemptivePriority() {
        readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority)
                .thenComparing(Comparator.naturalOrder()));
    }

    @Override
    public List<ExecutionSegment> schedule(List<Process> processes) {
        List<Process> sortedProcesses = SchedulerHelper.sortProcessesByArrivalTime(processes);
        List<ExecutionSegment> executionSegments = new LinkedList<>();

        Process runningProcess = null;
        SchedulingClock clock = new SchedulingClock();
        int startExecutionTime = 0;

        while (!sortedProcesses.isEmpty() || !readyQueue.isEmpty() || runningProcess != null) {
            SchedulerHelper.addArrivedProcessesToReadyQueue(sortedProcesses, readyQueue, clock);

            if (SchedulerHelper.isCpuInIdleState(sortedProcesses, readyQueue, runningProcess)) {
                SchedulerHelper.handleIdleState(clock, sortedProcesses.getFirst());
                continue;
            }

            if (!readyQueue.isEmpty() && SchedulerHelper.shouldPreempt(runningProcess, readyQueue.peek())) {
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

}