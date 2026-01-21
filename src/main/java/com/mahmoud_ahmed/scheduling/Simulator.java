package com.mahmoud_ahmed.scheduling;

import java.util.List;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.ProcessMetrics;
import com.mahmoud_ahmed.model.Result;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.scheduling.algorithms.SchedulingAlgorithm;
import com.mahmoud_ahmed.scheduling.metrics.MetricsCalculator;

public class Simulator {

    public Simulator() {

    }

    public Result simulate(SchedulingAlgorithm algorithm, List<Process> processes) {
        List<ExecutionSegment> timeline = algorithm.schedule(processes);
        List<ProcessMetrics> metrics = MetricsCalculator.calculateMetrics(timeline);
        double averageWaitingTime = MetricsCalculator.calculateAverageWaitingTime(metrics);
        double averageTurnaroundTime = MetricsCalculator.calculateAverageTurnaroundTime(metrics);
        return new Result(timeline, metrics, averageWaitingTime, averageTurnaroundTime);
    }
}
