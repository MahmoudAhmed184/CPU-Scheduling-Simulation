package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.Comparator;

import com.mahmoud_ahmed.scheduling.context.SchedulingContext;

public class ShortestJobFirst extends NonPreemptiveAlgorithm {
    public ShortestJobFirst() {
        super(Comparator.comparingInt(SchedulingContext::getBurstTime)
                .thenComparingInt(SchedulingContext::getArrivalTime)
                .thenComparingInt(SchedulingContext::getProcessNumber));
    }
}