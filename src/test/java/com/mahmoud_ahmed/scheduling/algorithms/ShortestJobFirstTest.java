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

public class ShortestJobFirstTest {
    private ShortestJobFirst sjf;

    @BeforeEach
    void setUp() {
        sjf = new ShortestJobFirst();
    }

    @ParameterizedTest(name = "{index} - Test with {0}")
    @MethodSource("provideTestCases")
    void whenSchedulingProcesses_shouldFollowFCFSRules(String testCase, List<Process> input,
            List<ExecutionSegment> expected) {
        List<ExecutionSegment> result = sjf.schedule(input);

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
                        "Same arrival time, different burst times",
                        Arrays.asList(
                                new Process(1, 0, 6, 0),
                                new Process(2, 0, 2, 0),
                                new Process(3, 0, 4, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(2, 0, 2, 0), 0, 2),
                                new ExecutionSegment(new Process(3, 0, 4, 0), 2, 6),
                                new ExecutionSegment(new Process(1, 0, 6, 0), 6, 12))),
                Arguments.of(
                        "Later arrival of shorter process",
                        Arrays.asList(
                                new Process(1, 0, 8, 0),
                                new Process(2, 2, 2, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 8, 0), 0, 8),
                                new ExecutionSegment(new Process(2, 2, 2, 0), 8, 10))),
                Arguments.of(
                        "Multiple processes with varied arrival times",
                        Arrays.asList(
                                new Process(1, 0, 5, 0),
                                new Process(2, 1, 2, 0),
                                new Process(3, 2, 1, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 5, 0), 0, 5),
                                new ExecutionSegment(new Process(3, 2, 1, 0), 5, 6),
                                new ExecutionSegment(new Process(2, 1, 2, 0), 6, 8))),
                Arguments.of(
                        "Equal burst times, should follow arrival order",
                        Arrays.asList(
                                new Process(1, 1, 3, 0),
                                new Process(2, 0, 3, 0),
                                new Process(3, 2, 3, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(2, 0, 3, 0), 0, 3),
                                new ExecutionSegment(new Process(1, 1, 3, 0), 3, 6),
                                new ExecutionSegment(new Process(3, 2, 3, 0), 6, 9))),
                Arguments.of(
                        "Gaps between arrivals with varied burst times",
                        Arrays.asList(
                                new Process(1, 0, 4, 0),
                                new Process(2, 5, 1, 0),
                                new Process(3, 5, 2, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 4, 0), 0, 4),
                                new ExecutionSegment(new Process(2, 5, 1, 0), 5, 6),
                                new ExecutionSegment(new Process(3, 5, 2, 0), 6, 8))),
                Arguments.of(
                        "Process with zero burst time",
                        Arrays.asList(
                                new Process(1, 0, 0, 0),
                                new Process(2, 0, 3, 0),
                                new Process(3, 0, 1, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 0, 0), 0, 0),
                                new ExecutionSegment(new Process(3, 0, 1, 0), 0, 1),
                                new ExecutionSegment(new Process(2, 0, 3, 0), 1, 4))),
                Arguments.of(
                        "Complex scenario with multiple arrival patterns",
                        Arrays.asList(
                                new Process(1, 0, 6, 0),
                                new Process(2, 2, 2, 0),
                                new Process(3, 3, 1, 0),
                                new Process(4, 4, 4, 0)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 6, 0), 0, 6),
                                new ExecutionSegment(new Process(3, 3, 1, 0), 6, 7),
                                new ExecutionSegment(new Process(2, 2, 2, 0), 7, 9),
                                new ExecutionSegment(new Process(4, 4, 4, 0), 9, 13))));
    }

    @Test
    void whenProcessListIsEmpty_shouldReturnEmptyResult() {
        assertTrue(sjf.schedule(Collections.emptyList()).isEmpty());
    }

    @Test
    void whenSchedulingLargeNumberOfProcesses_shouldCompleteInReasonableTime() {
        List<Process> processes = IntStream.range(1, 1000)
                .mapToObj(i -> new Process(i, i, 30, 0))
                .collect(Collectors.toList());

        long startTime = System.currentTimeMillis();
        sjf.schedule(processes);
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
