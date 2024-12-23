package com.mahmoud_ahmed.scheduling;

import java.util.List;

import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.Result;
import com.mahmoud_ahmed.scheduling.algorithms.SchedulingAlgorithm;

public class Scheduler {

    public Scheduler() {

    }

    public Result schedule(SchedulingAlgorithm algorithm, List<Process> processes) {
        return new Result(algorithm.schedule(processes));
    }
}
