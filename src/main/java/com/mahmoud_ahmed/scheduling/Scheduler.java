package com.mahmoud_ahmed.scheduling;

import java.util.Collection;
import java.util.List;


import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ScheduledProcess;

public class Scheduler {

    private Scheduler() {

    }

    public static List<ScheduledProcess> scheduleFirstComeFirstServe(Collection<Process> processes) {
        return null;
    }

    public static List<ScheduledProcess> scheduleShortestJobFirst(Collection<Process> processes) {
        return null;
    }

    public static List<ScheduledProcess> scheduleNonPreemptivePriority(Collection<Process> processes) {
        return null;
    }

    public static List<ExecutionSegment> scheduleRoundRobin(Collection<Process> processes, int timeQuantum) {
        return null;
    }

    public static List<ExecutionSegment> schedulePreemptivePriority(Collection<Process> processes) {
        return null;
    }

}
