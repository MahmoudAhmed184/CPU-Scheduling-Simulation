package com.mahmoud_ahmed.utils;

import java.util.*;
import java.util.stream.Collectors;

import com.mahmoud_ahmed.model.Process;

public class SchedulingUtil {

    private SchedulingUtil() {

    }

    public static List<Process> sortedProcessesByArrivalTime(Collection<Process> processes) {
        return processes.stream()
            .map(Process::new)
            .sorted()
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
