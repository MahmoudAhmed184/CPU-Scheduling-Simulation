package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.SchedulingClock;
import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.utils.SchedulingUtil;

public class RoundRobin implements SchedulingAlgorithm {
    private final Queue<Process> readyQueue;
    private final int timeQuantum;

    public RoundRobin(int timeQuantum){
        this.readyQueue = new LinkedList<>();
        this.timeQuantum = timeQuantum;
    }

    @Override
    public List<ExecutionSegment> schedule(List<Process> processes) {
        List<Process> sortedProcesses = SchedulingUtil.sortedProcessesByArrivalTime(processes);

        List<ExecutionSegment> executionSegments = new LinkedList<>();

        Process runningProcess = null;
        SchedulingClock clock = new SchedulingClock();

        while (!sortedProcesses.isEmpty() || !readyQueue.isEmpty()) {
            SchedulingUtil.addArrivedProcessesToReadyQueue(sortedProcesses, readyQueue, clock);

            if (SchedulingUtil.isCpuInIdleState(sortedProcesses, readyQueue, runningProcess)) {
                SchedulingUtil.handleIdleState(clock, sortedProcesses.getFirst());
                continue;
            }

            runningProcess = readyQueue.poll();
            int startExecutionTime = clock.getCurrentTime();

            if (runningProcess.getRemainingTime() > timeQuantum) {
                handleTimeQuantumExpiration(runningProcess, sortedProcesses, clock);
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

    private void handleTimeQuantumExpiration(Process process, List<Process> processes, SchedulingClock clock) {
        clock.advance(timeQuantum);
        process.setRemainingTime(process.getRemainingTime() - timeQuantum);
        SchedulingUtil.addArrivedProcessesToReadyQueue(processes, readyQueue, clock);
        readyQueue.add(process);
    }
}