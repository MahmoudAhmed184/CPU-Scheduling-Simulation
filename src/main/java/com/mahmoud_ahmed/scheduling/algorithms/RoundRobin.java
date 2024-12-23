package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.SchedulingClock;
import com.mahmoud_ahmed.scheduling.SchedulerHelper;
import com.mahmoud_ahmed.model.ExecutionSegment;

public class RoundRobin implements SchedulingAlgorithm {
    private final int timeQuantum;

    public RoundRobin(int timeQuantum){
        this.timeQuantum = timeQuantum;
    }

    @Override
    public List<ExecutionSegment> schedule(List<Process> processes) {
        List<Process> sortedProcesses = SchedulerHelper.sortProcessesByArrivalTime(processes);

        Queue<Process> readyQueue = new LinkedList<>();

        List<ExecutionSegment> executionSegments = new LinkedList<>();

        Process runningProcess = null;
        SchedulingClock clock = new SchedulingClock();

        while (!sortedProcesses.isEmpty() || !readyQueue.isEmpty()) {
            SchedulerHelper.addArrivedProcessesToReadyQueue(sortedProcesses, readyQueue, clock);

            if (SchedulerHelper.isCpuInIdleState(sortedProcesses, readyQueue, runningProcess)) {
                SchedulerHelper.handleIdleState(clock, sortedProcesses.getFirst());
                continue;
            }

            runningProcess = readyQueue.poll();
            int startExecutionTime = clock.getCurrentTime();

            if (runningProcess.getRemainingTime() > timeQuantum) {
                SchedulerHelper.handleTimeQuantumExpiration(runningProcess, readyQueue, sortedProcesses, clock,
                        timeQuantum);
                executionSegments.add(new ExecutionSegment(runningProcess, startExecutionTime, clock.getCurrentTime()));
            } else {
                clock.advance(runningProcess.getRemainingTime());
                executionSegments.add(new ExecutionSegment(runningProcess, startExecutionTime, clock.getCurrentTime()));
                runningProcess.resetRemainingTime();
            }
            runningProcess = null;
        }

        return executionSegments;
    }
}