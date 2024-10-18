package com.mahmoud_ahmed.scheduling;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ScheduledProcess;
import com.mahmoud_ahmed.model.SchedulingClock;

public class Scheduler {

    private Scheduler() {

    }

    public static List<ScheduledProcess> scheduleFirstComeFirstServe(Collection<Process> processes) {
        return scheduleNonPreemptiveAlgorithm(processes, Comparator.naturalOrder());
    }

    public static List<ScheduledProcess> scheduleShortestJobFirst(Collection<Process> processes) {
        return scheduleNonPreemptiveAlgorithm(processes,
                Comparator.comparingInt(Process::getBurstTime).thenComparing(Comparator.naturalOrder()));
    }

    public static List<ScheduledProcess> scheduleNonPreemptivePriority(Collection<Process> processes) {
        return scheduleNonPreemptiveAlgorithm(processes,
                Comparator.comparingInt(Process::getPriority).thenComparing(Comparator.naturalOrder()));
    }

    private static List<ScheduledProcess> scheduleNonPreemptiveAlgorithm(Collection<Process> processes, Comparator<Process> comparator) {
        List<Process> sortedProcesses = SchedulerHelper.sortProcessesByArrivalTime(processes);
        var readyQueue = new PriorityQueue<>(comparator);

        List<ScheduledProcess> scheduledProcesses = new LinkedList<>();
        SchedulingClock clock = new SchedulingClock();

        while (!sortedProcesses.isEmpty() || !readyQueue.isEmpty()) {
            SchedulerHelper.addArrivedProcessesToReadyQueue(sortedProcesses, readyQueue, clock);

            if (SchedulerHelper.isCpuInIdleState(sortedProcesses, readyQueue, null)) {
                SchedulerHelper.handleIdleState(clock, sortedProcesses.getFirst());
                continue;
            }

            scheduledProcesses.add(SchedulerHelper.scheduleProcess(readyQueue.poll(), clock));
        }

        return scheduledProcesses;
    }

    public static List<ExecutionSegment> scheduleRoundRobin(Collection<Process> processes, int timeQuantum) {
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
                SchedulerHelper.handleTimeQuantumExpiration(runningProcess, readyQueue, sortedProcesses, clock, timeQuantum);
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

    public static List<ExecutionSegment> schedulePreemptivePriority(Collection<Process> processes) {
        List<Process> sortedProcesses = SchedulerHelper.sortProcessesByArrivalTime(processes);

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority)
                .thenComparing(Comparator.naturalOrder()));

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
