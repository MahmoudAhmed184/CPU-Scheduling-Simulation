package com.mahmoud_ahmed.utils;

import java.util.*;
import com.mahmoud_ahmed.model.Process;

public class SchedulingUtil {

    private SchedulingUtil() {

    }

    public static List<Process> sortedProcessesByArrivalTime(Collection<Process> processes) {
        List<Process> sortedProcesses = new LinkedList<>(processes);
        Collections.sort(sortedProcesses);
        return sortedProcesses;
    }
}
