package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.Comparator;

import com.mahmoud_ahmed.scheduling.context.SchedulingContext;

public class PreemptivePriority extends PreemptiveAlgorithm {

    public PreemptivePriority() {
        super(Comparator.comparingInt(SchedulingContext::getPriority)
                .thenComparingInt(SchedulingContext::getArrivalTime)
                .thenComparingInt(SchedulingContext::getProcessNumber));
    }

    @Override
    protected boolean shouldPreempt(SchedulingContext active, SchedulingContext next) {
        return active != null &&
                next != null &&
                active.getPriority() > next.getPriority();
    }
}