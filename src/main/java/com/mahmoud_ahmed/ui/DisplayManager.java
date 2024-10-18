package com.mahmoud_ahmed.ui;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.ScheduledProcess;
import com.mahmoud_ahmed.utils.TableGenerator;
import com.mahmoud_ahmed.utils.SchedulingUtil;

public class DisplayManager {
    private final TableGenerator tableGenerator;
    private static final String[] TABLE_HEADERS = { "Process Number", "Start Execution Time", "Completion Time",
            "Turn Around Time", "Waiting Time" };
    private static final String[] SEGMENT_TABLE_HEADERS = { "Process Number", "Start Time", "Stop Time" };

    public DisplayManager() {
        this.tableGenerator = new TableGenerator();
    }

    public void displayNonPreemptiveAlgorithmResults(List<ScheduledProcess> processes) {
        List<List<String>> rows = processes.stream().map(ScheduledProcess::toList).collect(Collectors.toList());
        System.out.println(tableGenerator.generateTable(Arrays.asList(TABLE_HEADERS), rows));
    }

    public void displayPreemptiveAlgorithmResults(List<ExecutionSegment> executionSegments) {
        List<List<String>> segmentRows = executionSegments.stream()
                .map(ExecutionSegment::toList)
                .collect(Collectors.toList());

        System.out.println("Execution Segments Table:");
        System.out.println(tableGenerator.generateTable(Arrays.asList(SEGMENT_TABLE_HEADERS), segmentRows));

        System.out.println("Aggregated Process Statistics:");
        List<ScheduledProcess> scheduledProcesses = SchedulingUtil.assembleExecutionSegments(executionSegments);
        displayNonPreemptiveAlgorithmResults(scheduledProcesses);
    }
}
