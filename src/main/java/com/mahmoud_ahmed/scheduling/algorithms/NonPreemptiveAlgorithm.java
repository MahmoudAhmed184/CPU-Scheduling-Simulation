package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.Comparator;
import java.util.List;

import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.scheduling.context.SchedulingContext;
import com.mahmoud_ahmed.scheduling.state.SchedulingState;

public abstract class NonPreemptiveAlgorithm implements SchedulingAlgorithm {
    private final Comparator<SchedulingContext> comparator;

    public NonPreemptiveAlgorithm(Comparator<SchedulingContext> comparator) {
        this.comparator = comparator;
    }

    @Override
    public List<ExecutionSegment> schedule(List<Process> processes) {
        SchedulingState state = new SchedulingState(processes, comparator);

        while (state.hasUnfinishedProcesses()) {
            state.moveArrivedProcessesToReadyQueue();
            if (state.isCpuInIdleState()) {
                state.handleIdleState();
                continue;
            }
            state.pollReadyProcessToSchedule();
            scheduleActiveProcess(state);
        }

        return state.getExecutionHistory();
    }

    private void scheduleActiveProcess(SchedulingState state) {
        SchedulingContext context = state.getActiveContext();
        int startTime = state.getCurrentTime();
        state.advanceClock(context.getBurstTime());
        state.recordExecutionSegment(new ExecutionSegment(context.getProcess(), startTime, state.getCurrentTime()));
        state.clearActiveContext();
    }
}