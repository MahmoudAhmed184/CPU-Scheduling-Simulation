package com.mahmoud_ahmed.presentation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.mahmoud_ahmed.model.Result;

public class ConsolePresenter {
    private final TableGenerator tableGenerator;
    private static final String[] TABLE_HEADERS = { "Process Number", "Start Execution Time", "Completion Time",
            "Turn Around Time", "Waiting Time" };
    private static final String[] SEGMENT_TABLE_HEADERS = { "Process Number", "Start Time", "Stop Time" };

    public ConsolePresenter() {
        this.tableGenerator = new TableGenerator();
    }

    public void display(Result result) {
        List<List<String>> segmentRows = result.executionSegments().stream()
                .map(ResultFormatter::formatSegment)
                .collect(Collectors.toList());

        System.out.println("Execution Segments Table:");
        System.out.println(tableGenerator.generateTable(Arrays.asList(SEGMENT_TABLE_HEADERS), segmentRows));

        System.out.println("Aggregated Process Statistics:");
        List<List<String>> rows = result.metrics().stream()
                .map(ResultFormatter::formatMetrics)
                .collect(Collectors.toList());
        System.out.println(tableGenerator.generateTable(Arrays.asList(TABLE_HEADERS), rows));

        System.out.println("Algorithm Average Waiting Time: " + result.algorithmAverageWaitingTime());
        System.out.println("Algorithm Average Turnaround Time: " + result.algorithmAverageTurnaroundTime());
    }
}
