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

public abstract class NonPreemptiveAlgorithm implements SchedulingAlgorithm {
    private final Queue<Process> readyQueue;

    public NonPreemptiveAlgorithm(Comparator<Process> comparator) {
        readyQueue = new PriorityQueue<>(comparator);
    }

    @Override
    public List<ExecutionSegment> schedule(List<Process> processes) {
        List<Process> sortedProcesses = SchedulingUtil.sortedProcessesByArrivalTime(processes);

        List<ExecutionSegment> executionSegments = new LinkedList<>();
        SchedulingClock clock = new SchedulingClock();

        while (!sortedProcesses.isEmpty() || !readyQueue.isEmpty()) {
            SchedulingUtil.addArrivedProcessesToReadyQueue(sortedProcesses, readyQueue, clock);

            if (SchedulingUtil.isCpuInIdleState(sortedProcesses, readyQueue, null)) {
                SchedulingUtil.handleIdleState(clock, sortedProcesses.getFirst());
                continue;
            }

            executionSegments.add(scheduleProcess(readyQueue.poll(), clock));
        }

        return executionSegments;
    }

    private static ExecutionSegment scheduleProcess(Process process, SchedulingClock clock) {
        int startExecutionTime = clock.getCurrentTime();
        int completionTime = startExecutionTime + process.getBurstTime();
        clock.advance(process.getBurstTime());
        return new ExecutionSegment(process, startExecutionTime, completionTime);
    }
}