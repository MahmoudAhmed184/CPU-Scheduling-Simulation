package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.Comparator;

import com.mahmoud_ahmed.model.Process;

public class NonPreemptivePriority extends NonPreemptiveAlgorithm{
    public NonPreemptivePriority(){
        super(Comparator.comparingInt(Process::getPriority).thenComparing(Comparator.naturalOrder()));
    }
}