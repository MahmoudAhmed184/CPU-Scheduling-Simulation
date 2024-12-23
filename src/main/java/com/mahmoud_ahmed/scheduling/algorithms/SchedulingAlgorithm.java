package com.mahmoud_ahmed.scheduling.algorithms;

import java.util.List;

import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.model.ExecutionSegment;


public interface SchedulingAlgorithm {
    List<ExecutionSegment> schedule(List<Process> processes);
}