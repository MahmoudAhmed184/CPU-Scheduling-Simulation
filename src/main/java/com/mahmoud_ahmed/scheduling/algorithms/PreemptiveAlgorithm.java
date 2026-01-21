package com.mahmoud_ahmed.scheduling.algorithms;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.scheduling.state.SchedulingState;
import com.mahmoud_ahmed.scheduling.state.ExecutionSegmentBuilder;

import java.util.*;

public abstract class PreemptiveAlgorithm implements SchedulingAlgorithm {
    private final Comparator<Process> comparator;

    public PreemptiveAlgorithm(Comparator<Process> comparator) {
        this.comparator = comparator;
    }

    @Override
    public List<ExecutionSegment> schedule(List<Process> processes) {
        SchedulingState state = new SchedulingState(processes, comparator);
        ExecutionSegmentBuilder builder = new ExecutionSegmentBuilder();
        while (state.hasUnfinishedProcesses()) {
            state.moveArrivedProcessesToReadyQueue();
            if (state.isCpuInIdleState()) {
                state.handleIdleState();
                continue;
            }
            handlePreemption(state, builder);
            selectProcessToRun(state, builder);
            scheduleActiveProcessFor(state, 1);
            handleFinishedProcess(state, builder);
        }
        return state.getExecutionHistory();
    }

    private static void selectProcessToRun(SchedulingState state, ExecutionSegmentBuilder builder) {
        if (!state.hasActiveProcess() && state.hasReadyProcesses()) {
            state.pollReadyProcessToSchedule();
            builder.withProcess(state.getActiveProcess())
                    .withStartTime(state.getCurrentTime());
        }
    }

    private static void handleFinishedProcess(SchedulingState state, ExecutionSegmentBuilder builder) {
        if (state.hasActiveProcessFinished()) {
            state.recordExecutionSegment(builder.withEndTime(state.getCurrentTime()).build());
            state.setActiveProcess(null);
        }
    }

    private void handlePreemption(SchedulingState state, ExecutionSegmentBuilder builder) {
        if (shouldPreempt(state.getActiveProcess(), state.peekNextReadyProcess())) {
            state.recordExecutionSegment(builder.withEndTime(state.getCurrentTime()).build());
            preemptActiveProcess(state);
        }
    }

    public void preemptActiveProcess(SchedulingState state) {
        state.addProcessToReadyQueue(state.getActiveProcess());
        state.setActiveProcess(null);
    }

    public void scheduleActiveProcessFor(SchedulingState state, int time) {
        state.advanceClock(time);
        Process activeProcess = state.getActiveProcess();
        activeProcess.setRemainingTime(activeProcess.getRemainingTime() - time);
    }

    abstract boolean shouldPreempt(Process activeProcess, Process arrivedProcess);
}
