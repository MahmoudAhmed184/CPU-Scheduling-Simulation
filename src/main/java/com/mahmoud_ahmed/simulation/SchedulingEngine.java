package com.mahmoud_ahmed.simulation;

import java.util.Collection;
import java.util.List;
import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ScheduledProcess;
import com.mahmoud_ahmed.scheduling.Scheduler;
import com.mahmoud_ahmed.ui.DisplayManager;

public class SchedulingEngine {
    private DisplayManager displayManager;

    public SchedulingEngine() {
        this.displayManager = new DisplayManager();
    }

    public void runFirstComeFirstServe(Collection<Process> processes) {
        List<ScheduledProcess> result = Scheduler.scheduleFirstComeFirstServe(processes);
        displayManager.displayNonPreemptiveAlgorithmResults(result);
    }

    public void runShortestJobFirst(Collection<Process> processes) {
        List<ScheduledProcess> result = Scheduler.scheduleShortestJobFirst(processes);
        displayManager.displayNonPreemptiveAlgorithmResults(result);
    }

    public void runNonPreemptivePriority(Collection<Process> processes) {
        List<ScheduledProcess> result = Scheduler.scheduleNonPreemptivePriority(processes);
        displayManager.displayNonPreemptiveAlgorithmResults(result);
    }

    public void runRoundRobin(Collection<Process> processes, int timeQuantum) {
        List<ExecutionSegment> result = Scheduler.scheduleRoundRobin(processes, timeQuantum);
        displayManager.displayPreemptiveAlgorithmResults(result);
    }

    public void runPreemptivePriority(Collection<Process> processes) {
        List<ExecutionSegment> result = Scheduler.schedulePreemptivePriority(processes);
        displayManager.displayPreemptiveAlgorithmResults(result);
    }

}
