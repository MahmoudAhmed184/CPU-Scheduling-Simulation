package com.mahmoud_ahmed.scheduling.state;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;

import java.util.*;
import java.util.stream.Collectors;

public class SchedulingState {
    private final Queue<Process> readyQueue;
    private final List<Process> processes;
    private final SchedulingClock clock;
    private final List<ExecutionSegment> timeline;
    private Process activeProcess;

    public SchedulingState(List<Process> processes, Comparator<Process> comparator) {
        this.readyQueue = comparator == null ? new LinkedList<>() : new PriorityQueue<>(comparator);
        this.processes = sortProcessesByArrivalTime(processes);
        this.clock = new SchedulingClock();
        this.timeline = new ArrayList<>();
        this.activeProcess = null;
    }

    private static List<Process> sortProcessesByArrivalTime(List<Process> processes) {
        return processes.stream()
                .map(Process::new)
                .sorted()
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Process getActiveProcess() {
        return activeProcess;
    }

    public void setActiveProcess(Process process) {
        this.activeProcess = process;
    }

    public void moveArrivedProcessesToReadyQueue() {
        while (hasWaitingProcesses() && hasProcessReached(processes.getFirst())) {
            readyQueue.add(processes.removeFirst());
        }
    }

    public boolean hasUnfinishedProcesses() {
        return hasWaitingProcesses() ||
                hasReadyProcesses() ||
                hasActiveProcess();
    }

    public boolean hasActiveProcess() {
        return activeProcess != null;
    }

    public boolean hasReadyProcesses() {
        return !readyQueue.isEmpty();
    }

    private boolean hasWaitingProcesses() {
        return !processes.isEmpty();
    }

    private boolean hasProcessReached(Process process) {
        return clock.isBeforeOrAt(process.getArrivalTime());
    }

    public boolean isCpuInIdleState() {
        return hasWaitingProcesses() &&
                !hasReadyProcesses() &&
                !hasActiveProcess();
    }

    public void handleIdleState() {
        advanceClockToNextProcessArrivalTime();
    }

    private void advanceClockToNextProcessArrivalTime() {
        clock.setTime(processes.getFirst().getArrivalTime());
    }

    public void pollReadyProcessToSchedule() {
        activeProcess = readyQueue.poll();
    }

    public int getCurrentTime() {
        return clock.getCurrentTime();
    }

    public Process peekNextReadyProcess() {
        return readyQueue.peek();
    }

    public boolean hasActiveProcessFinished() {
        return activeProcess.getRemainingTime() == 0;
    }

    public void recordExecutionSegment(ExecutionSegment executionSegment) {
        timeline.add(executionSegment);
    }

    public List<ExecutionSegment> getExecutionHistory() {
        return timeline;
    }

    public void addProcessToReadyQueue(Process process) {
        readyQueue.add(process);
    }

    public void advanceClock(int time) {
        clock.advance(time);
    }
}