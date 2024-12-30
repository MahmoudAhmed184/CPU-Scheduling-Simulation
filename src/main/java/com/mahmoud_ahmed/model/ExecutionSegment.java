package com.mahmoud_ahmed.model;

import java.util.List;

public record ExecutionSegment(Process process, int startTime, int endTime) {

    public List<String> toList() {
        return List.of(
            Integer.toString(process.getProcessNumber()),
            Integer.toString(startTime),
            Integer.toString(endTime));
    }
}
