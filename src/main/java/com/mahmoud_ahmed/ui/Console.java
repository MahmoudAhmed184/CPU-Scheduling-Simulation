package com.mahmoud_ahmed.ui;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.ProcessMetrics;
import com.mahmoud_ahmed.model.Result;
import com.mahmoud_ahmed.utils.TableGenerator;

public class Console {
    private final TableGenerator tableGenerator;
    private static final String[] TABLE_HEADERS = { "Process Number", "Start Execution Time", "Completion Time",
            "Turn Around Time", "Waiting Time" };
    private static final String[] SEGMENT_TABLE_HEADERS = { "Process Number", "Start Time", "Stop Time" };

    public Console() {
        this.tableGenerator = new TableGenerator();
    }

    public void display(Result result) {
        List<List<String>> segmentRows = result.getExecutionSegments().stream()
                .map(ExecutionSegment::toList)
                .collect(Collectors.toList());

        System.out.println("Execution Segments Table:");
        System.out.println(tableGenerator.generateTable(Arrays.asList(SEGMENT_TABLE_HEADERS), segmentRows));
        
        System.out.println("Aggregated Process Statistics:");
        List<List<String>> rows = result.getMetrics().stream().map(ProcessMetrics::toList).collect(Collectors.toList());
        System.out.println(tableGenerator.generateTable(Arrays.asList(TABLE_HEADERS), rows));
    }
}
