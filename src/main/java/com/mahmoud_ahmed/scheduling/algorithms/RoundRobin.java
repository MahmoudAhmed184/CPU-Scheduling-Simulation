package com.mahmoud_ahmed.scheduling.algorithms;

import com.mahmoud_ahmed.scheduling.context.SchedulingContext;

public class RoundRobin extends PreemptiveAlgorithm {
    private final int timeQuantum;

    public RoundRobin(int timeQuantum) {
        super(null);
        this.timeQuantum = timeQuantum;
    }

    @Override
    protected boolean shouldPreempt(SchedulingContext active, SchedulingContext next) {
        return active != null &&
                (active.getElapsedTime() % timeQuantum == 0);
    }
}