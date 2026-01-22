package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.Comparator;

import com.mahmoud_ahmed.scheduling.context.SchedulingContext;

public class NonPreemptivePriority extends NonPreemptiveAlgorithm {
    public NonPreemptivePriority() {
        super(Comparator.comparingInt(SchedulingContext::getPriority)
                .thenComparingInt(SchedulingContext::getArrivalTime)
                .thenComparingInt(SchedulingContext::getProcessNumber));
    }
}