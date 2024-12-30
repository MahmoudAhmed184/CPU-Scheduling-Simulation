package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.Comparator;
import com.mahmoud_ahmed.model.Process;

public class PreemptivePriority extends PreemptiveAlgorithm {

    public PreemptivePriority() {
        super(Comparator.comparingInt(Process::getPriority)
            .thenComparing(Comparator.naturalOrder()));
    }

    @Override
    boolean shouldPreempt(Process activeProcess, Process arrivedProcess) {
        return activeProcess != null && activeProcess.getPriority() > arrivedProcess.getPriority();
    }
}