package com.mahmoud_ahmed;

import java.util.List;

import com.mahmoud_ahmed.simulation.SchedulingEngine;
import com.mahmoud_ahmed.model.Process;

public class Main {
    public static void main(String[] args) {
        SchedulingEngine schedulingEngine = new SchedulingEngine();
        schedulingEngine.runRoundRobin(
                List.of(
                        new Process(1, 0, 7, 0),
                        new Process(2, 2, 4, 0),
                        new Process(3, 4, 1, 0),
                        new Process(4, 5, 4, 0)),
                4);
    }
}