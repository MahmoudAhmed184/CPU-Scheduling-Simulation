package com.mahmoud_ahmed.scheduling.state;

import com.mahmoud_ahmed.model.ExecutionSegment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExecutionRecorder {
    private final List<ExecutionSegment> timeline;

    public ExecutionRecorder() {
        this.timeline = new ArrayList<>();
    }

    public void record(ExecutionSegment segment) {
        timeline.add(segment);
    }

    public List<ExecutionSegment> getHistory() {
        return Collections.unmodifiableList(timeline);
    }
}
