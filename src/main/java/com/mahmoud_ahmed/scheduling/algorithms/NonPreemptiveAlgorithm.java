package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.Comparator;
import java.util.List;

import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.SchedulingState;

public abstract class NonPreemptiveAlgorithm implements SchedulingAlgorithm {
    private final Comparator<Process> comparator;

    public NonPreemptiveAlgorithm(Comparator<Process> comparator) {
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

    public void scheduleActiveProcess(SchedulingState state) {
        int startTime = state.getCurrentTime();
        state.advanceClock(state.getActiveProcess().getBurstTime());
        state.recordExecutionSegment(new ExecutionSegment(state.getActiveProcess(), startTime, state.getCurrentTime()));
        state.setActiveProcess(null);
    }
}