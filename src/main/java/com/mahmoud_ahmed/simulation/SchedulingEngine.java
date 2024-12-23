package com.mahmoud_ahmed.simulation;

import java.util.List;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.Result;
import com.mahmoud_ahmed.scheduling.Scheduler;
import com.mahmoud_ahmed.scheduling.algorithms.*;
import com.mahmoud_ahmed.ui.Console;

public class SchedulingEngine {
    private final Scheduler scheduler;
    private final Console console;

    public SchedulingEngine() {
        this.scheduler = new Scheduler();
        this.console = new Console();
    }

    public void runFirstComeFirstServe(List<Process> processes) {
        SchedulingAlgorithm algorithm = new FirstComeFirstServe();
        Result result = scheduler.schedule(algorithm, processes);
        console.display(result);
    }

    public void runShortestJobFirst(List<Process> processes) {
        SchedulingAlgorithm algorithm = new ShortestJobFirst();
        Result result = scheduler.schedule(algorithm, processes);
        console.display(result);
    }

    public void runNonPreemptivePriority(List<Process> processes) {
        SchedulingAlgorithm algorithm = new NonPreemptivePriority();
        Result result = scheduler.schedule(algorithm, processes);
        console.display(result);
    }

    public void runRoundRobin(List<Process> processes, int timeQuantum) {
        SchedulingAlgorithm algorithm = new RoundRobin(timeQuantum);
        Result result = scheduler.schedule(algorithm, processes);
        console.display(result);
    }

    public void runPreemptivePriority(List<Process> processes) {
        SchedulingAlgorithm algorithm = new PreemptivePriority();
        Result result = scheduler.schedule(algorithm, processes);
        console.display(result);
    }
}
