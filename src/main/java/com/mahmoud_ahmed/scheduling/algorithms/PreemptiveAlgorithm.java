package com.mahmoud_ahmed.scheduling.algorithms;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.scheduling.context.SchedulingContext;
import com.mahmoud_ahmed.scheduling.state.SchedulingState;
import com.mahmoud_ahmed.scheduling.state.ExecutionSegmentBuilder;

import java.util.*;

public abstract class PreemptiveAlgorithm implements SchedulingAlgorithm {
    private final Comparator<SchedulingContext> comparator;

    public PreemptiveAlgorithm(Comparator<SchedulingContext> comparator) {
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
            executeForOneTimeUnit(state);
            handleFinishedProcess(state, builder);
        }
        return state.getExecutionHistory();
    }

    private void selectProcessToRun(SchedulingState state, ExecutionSegmentBuilder builder) {
        if (!state.hasActiveProcess() && state.hasReadyProcesses()) {
            state.pollReadyProcessToSchedule();
            builder.withProcess(state.getActiveContext().getProcess())
                    .withStartTime(state.getCurrentTime());
        }
    }

    private void handleFinishedProcess(SchedulingState state, ExecutionSegmentBuilder builder) {
        if (state.hasActiveProcessFinished()) {
            state.recordExecutionSegment(builder.withEndTime(state.getCurrentTime()).build());
            state.clearActiveContext();
        }
    }

    private void handlePreemption(SchedulingState state, ExecutionSegmentBuilder builder) {
        if (shouldPreempt(state.getActiveContext(), state.peekNextReadyContext())) {
            state.recordExecutionSegment(builder.withEndTime(state.getCurrentTime()).build());
            state.requeueActiveProcess();
        }
    }

    private void executeForOneTimeUnit(SchedulingState state) {
        state.advanceClock(1);
        state.getActiveContext().executeFor(1);
    }

    protected abstract boolean shouldPreempt(SchedulingContext active, SchedulingContext next);
}
