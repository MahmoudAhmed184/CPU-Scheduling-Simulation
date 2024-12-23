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

public abstract class NonPreemptiveAlgorithm implements SchedulingAlgorithm {
    private final Queue<Process> readyQueue;

    public NonPreemptiveAlgorithm(Comparator<Process> comparator) {
        readyQueue = new PriorityQueue<>(comparator);
    }

    @Override
    public List<ExecutionSegment> schedule(List<Process> processes) {
        List<Process> sortedProcesses = SchedulerHelper.sortProcessesByArrivalTime(processes);

        List<ExecutionSegment> executionSegments = new LinkedList<>();
        SchedulingClock clock = new SchedulingClock();

        while (!sortedProcesses.isEmpty() || !readyQueue.isEmpty()) {
            SchedulerHelper.addArrivedProcessesToReadyQueue(sortedProcesses, readyQueue, clock);

            if (SchedulerHelper.isCpuInIdleState(sortedProcesses, readyQueue, null)) {
                SchedulerHelper.handleIdleState(clock, sortedProcesses.getFirst());
                continue;
            }

            executionSegments.add(SchedulerHelper.scheduleProcess(readyQueue.poll(), clock));
        }

        return executionSegments;
    }
}