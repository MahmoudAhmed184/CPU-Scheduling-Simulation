package com.mahmoud_ahmed.scheduling.state;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.scheduling.context.SchedulingContext;

import java.util.Comparator;
import java.util.List;

public class SchedulingState {
    private final ProcessQueueManager queueManager;
    private final SchedulingClock clock;
    private final ExecutionRecorder recorder;

    public SchedulingState(List<Process> processes, Comparator<SchedulingContext> comparator) {
        this.queueManager = new ProcessQueueManager(processes, comparator);
        this.clock = new SchedulingClock();
        this.recorder = new ExecutionRecorder();
    }

    public void moveArrivedProcessesToReadyQueue() {
        queueManager.moveArrivedToReady(clock.getCurrentTime());
    }

    public boolean hasUnfinishedProcesses() {
        return queueManager.hasUnfinished();
    }

    public boolean hasActiveProcess() {
        return queueManager.hasActive();
    }

    public boolean hasReadyProcesses() {
        return queueManager.hasReady();
    }

    public boolean isCpuInIdleState() {
        return queueManager.hasWaiting() && !queueManager.hasReady() && !queueManager.hasActive();
    }

    public void handleIdleState() {
        clock.setTime(queueManager.getNextArrivalTime());
    }

    public void pollReadyProcessToSchedule() {
        queueManager.activateNextReady();
    }

    public int getCurrentTime() {
        return clock.getCurrentTime();
    }

    public SchedulingContext getActiveContext() {
        return queueManager.getActiveContext();
    }

    public SchedulingContext peekNextReadyContext() {
        return queueManager.peekNextReady();
    }

    public boolean hasActiveProcessFinished() {
        return queueManager.getActiveContext().hasFinished();
    }

    public void recordExecutionSegment(ExecutionSegment segment) {
        recorder.record(segment);
    }

    public List<ExecutionSegment> getExecutionHistory() {
        return recorder.getHistory();
    }

    public void requeueActiveProcess() {
        queueManager.requeueActive();
    }

    public void clearActiveContext() {
        queueManager.clearActive();
    }

    public void advanceClock(int time) {
        clock.advance(time);
    }
}