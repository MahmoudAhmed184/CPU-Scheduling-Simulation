package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.LinkedList;
import com.mahmoud_ahmed.model.Process;

public class RoundRobin extends PreemptiveAlgorithm {
    private final int timeQuantum;

    public RoundRobin(int timeQuantum) {
        super(new LinkedList<>());
        this.timeQuantum = timeQuantum;
    }

    @Override
    boolean shouldPreempt(Process activeProcess, Process arrivedProcess) {
        return activeProcess != null &&
               ((activeProcess.getBurstTime() - activeProcess.getRemainingTime()) % timeQuantum == 0);
    }
}