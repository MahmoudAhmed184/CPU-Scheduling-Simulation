package com.mahmoud_ahmed.scheduling.algorithms;

import com.mahmoud_ahmed.model.ExecutionSegment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.mahmoud_ahmed.model.Process;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FirstComeFirstServeTest {
    private FirstComeFirstServe fcfs;

    @BeforeEach
    void setUp() {
        fcfs = new FirstComeFirstServe();
    }

    @ParameterizedTest(name = "{index} - Test with {0}")
    @MethodSource("provideTestCases")
    void whenSchedulingProcesses_shouldFollowFCFSRules(String testCase, List<Process> input,
            List<ExecutionSegment> expected) {
        List<ExecutionSegment> result = fcfs.schedule(input);
        assertEquals(expected.size(), result.size(), "Number of execution segments should match");
        for (int i = 0; i < expected.size(); i++) {
            assertExecutionSegment(
                    result.get(i),
                    expected.get(i).process().processNumber(),
                    expected.get(i).startTime(),
                    expected.get(i).endTime());
        }
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(
                        "Same arrival time processes",
                        Arrays.asList(
                                new Process(1, 0, 5, 0),
                                new Process(2, 0, 3, 0),
                                new Process(3, 0, 2, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 5, 0), 0, 5),
                                new ExecutionSegment(new Process(2, 0, 3, 0), 5, 8),
                                new ExecutionSegment(new Process(3, 0, 2, 0), 8, 10))),
                Arguments.of(
                        "Different arrival times processes",
                        Arrays.asList(
                                new Process(1, 2, 4, 0),
                                new Process(2, 0, 3, 0),
                                new Process(3, 3, 4, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(2, 0, 3, 0), 0, 3),
                                new ExecutionSegment(new Process(1, 2, 4, 0), 3, 7),
                                new ExecutionSegment(new Process(3, 3, 4, 0), 7, 11))),
                Arguments.of(
                        "Gaps between process arrivals",
                        Arrays.asList(
                                new Process(1, 5, 3, 0),
                                new Process(2, 9, 2, 0),
                                new Process(3, 13, 4, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 5, 3, 0), 5, 8),
                                new ExecutionSegment(new Process(2, 9, 2, 0), 9, 11),
                                new ExecutionSegment(new Process(3, 13, 4, 0), 13, 17))),
                Arguments.of(
                        "Process with zero burst time",
                        Arrays.asList(
                                new Process(1, 0, 0, 0),
                                new Process(2, 0, 3, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 0, 0), 0, 0),
                                new ExecutionSegment(new Process(2, 0, 3, 0), 0, 3))),
                Arguments.of(
                        "Multiple processes with varied timings",
                        Arrays.asList(
                                new Process(1, 2, 2, 0),
                                new Process(2, 0, 1, 0),
                                new Process(3, 2, 3, 0),
                                new Process(4, 3, 5, 0),
                                new Process(5, 4, 4, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(2, 0, 1, 0), 0, 1),
                                new ExecutionSegment(new Process(1, 2, 2, 0), 2, 4),
                                new ExecutionSegment(new Process(3, 2, 3, 0), 4, 7),
                                new ExecutionSegment(new Process(4, 3, 5, 0), 7, 12),
                                new ExecutionSegment(new Process(5, 4, 4, 0), 12, 16))));
    }

    @Test
    void whenProcessListIsEmpty_shouldReturnEmptyResult() {
        assertTrue(fcfs.schedule(Collections.emptyList()).isEmpty());
    }

    @Test
    void whenSchedulingLargeNumberOfProcesses_shouldCompleteInReasonableTime() {
        List<Process> processes = IntStream.range(1, 1000)
                .mapToObj(i -> new Process(i, i, 30, 0))
                .collect(Collectors.toList());

        long startTime = System.currentTimeMillis();
        fcfs.schedule(processes);
        long endTime = System.currentTimeMillis();

        assertTrue((endTime - startTime) < 1000, "Scheduling took too long!");
    }

    private void assertExecutionSegment(ExecutionSegment segment,
            int expectedProcessNumber,
            int expectedStartTime,
            int expectedEndTime) {
        assertEquals(expectedProcessNumber, segment.process().processNumber());
        assertEquals(expectedStartTime, segment.startTime());
        assertEquals(expectedEndTime, segment.endTime());
    }
}
