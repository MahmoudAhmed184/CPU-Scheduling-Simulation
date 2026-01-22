package com.mahmoud_ahmed.scheduling.algorithms;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoundRobinTest {
    private RoundRobin roundRobin;

    @BeforeEach
    void setUp() {
        roundRobin = new RoundRobin(4);
    }

    @ParameterizedTest(name = "{index} - Test with {0}")
    @MethodSource("provideTestCases")
    void whenSchedulingProcesses_shouldFollowFCFSRules(String testCase, List<Process> input,
            List<ExecutionSegment> expected) {
        List<ExecutionSegment> result = roundRobin.schedule(input);

        assertEquals(expected.size(), result.size(), "Number of execution segments should match");
        for (int i = 0; i < expected.size(); i++) {
            assertExecutionSegment(
                    result.get(i),
                    expected.get(i).process().getProcessNumber(),
                    expected.get(i).startTime(),
                    expected.get(i).endTime());
        }
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(
                        "Single process within quantum",
                        List.of(new Process(1, 0, 3, 0)),
                        List.of(new ExecutionSegment(new Process(1, 0, 3, 0), 0, 3))),
                Arguments.of(
                        "Single process requiring multiple quanta",
                        List.of(new Process(1, 0, 6, 0)),
                        List.of(
                                new ExecutionSegment(new Process(1, 0, 6, 0), 0, 4),
                                new ExecutionSegment(new Process(1, 0, 6, 0), 4, 6))),
                Arguments.of(
                        "Multiple processes with no waiting",
                        List.of(
                                new Process(1, 0, 3, 0),
                                new Process(2, 0, 3, 0)),
                        List.of(
                                new ExecutionSegment(new Process(1, 0, 3, 0), 0, 3),
                                new ExecutionSegment(new Process(2, 0, 3, 0), 3, 6))),
                Arguments.of(
                        "Multiple processes requiring time slicing",
                        List.of(
                                new Process(1, 0, 6, 0),
                                new Process(2, 0, 6, 0)),
                        List.of(
                                new ExecutionSegment(new Process(1, 0, 6, 0), 0, 4),
                                new ExecutionSegment(new Process(2, 0, 6, 0), 4, 8),
                                new ExecutionSegment(new Process(1, 0, 6, 0), 8, 10),
                                new ExecutionSegment(new Process(2, 0, 6, 0), 10, 12))),
                Arguments.of(
                        "Processes with different arrival times",
                        List.of(
                                new Process(1, 0, 4, 0),
                                new Process(2, 2, 3, 0),
                                new Process(3, 4, 2, 0)),
                        List.of(
                                new ExecutionSegment(new Process(1, 0, 4, 0), 0, 4),
                                new ExecutionSegment(new Process(2, 2, 3, 0), 4, 7),
                                new ExecutionSegment(new Process(3, 4, 2, 0), 7, 9))));
    }

    @ParameterizedTest(name = "Quantum={0}, {1}")
    @MethodSource("provideTestCasesWithQuantum")
    void whenSchedulingProcessesWithVariousQuanta_shouldBehaveCorrectly(int quantum, String testCase,
            List<Process> input, List<ExecutionSegment> expected) {
        roundRobin = new RoundRobin(quantum);
        List<ExecutionSegment> result = roundRobin.schedule(input);

        assertEquals(expected.size(), result.size(), "Number of execution segments should match");
        for (int i = 0; i < expected.size(); i++) {
            assertExecutionSegment(
                    result.get(i),
                    expected.get(i).process().getProcessNumber(),
                    expected.get(i).startTime(),
                    expected.get(i).endTime());
        }
    }

    private static Stream<Arguments> provideTestCasesWithQuantum() {
        return Stream.of(
                Arguments.of(2, "Single process, burst < quantum",
                        List.of(new Process(1, 0, 1, 0)),
                        List.of(new ExecutionSegment(new Process(1, 0, 1, 0), 0, 1))),
                Arguments.of(2, "Single process, burst > quantum",
                        List.of(new Process(1, 0, 5, 0)),
                        List.of(
                                new ExecutionSegment(new Process(1, 0, 5, 0), 0, 2),
                                new ExecutionSegment(new Process(1, 0, 5, 0), 2, 4),
                                new ExecutionSegment(new Process(1, 0, 5, 0), 4, 5))),
                Arguments.of(4, "Multiple processes with same arrival",
                        List.of(new Process(1, 0, 6, 0), new Process(2, 0, 4, 0)),
                        List.of(
                                new ExecutionSegment(new Process(1, 0, 6, 0), 0, 4),
                                new ExecutionSegment(new Process(2, 0, 4, 0), 4, 8),
                                new ExecutionSegment(new Process(1, 0, 6, 0), 8, 10))),
                Arguments.of(6, "Process finishes within quantum",
                        List.of(new Process(1, 0, 5, 0)),
                        List.of(new ExecutionSegment(new Process(1, 0, 5, 0), 0, 5))),
                Arguments.of(6, "Processes arrive at different times",
                        List.of(new Process(1, 0, 8, 0), new Process(2, 2, 6, 0)),
                        List.of(
                                new ExecutionSegment(new Process(1, 0, 8, 0), 0, 6),
                                new ExecutionSegment(new Process(2, 2, 6, 0), 6, 12),
                                new ExecutionSegment(new Process(1, 0, 8, 0), 12, 14))));
    }

    @Test
    void whenProcessListIsEmpty_shouldReturnEmptyResult() {
        assertTrue(roundRobin.schedule(Collections.emptyList()).isEmpty());
    }

    @Test
    void whenSchedulingLargeNumberOfProcesses_shouldCompleteInReasonableTime() {
        List<Process> processes = IntStream.range(1, 1000)
                .mapToObj(i -> new Process(i, i, 30, 0))
                .collect(Collectors.toList());

        long startTime = System.currentTimeMillis();
        roundRobin.schedule(processes);
        long endTime = System.currentTimeMillis();

        assertTrue((endTime - startTime) < 1000, "Scheduling took too long!");
    }

    private void assertExecutionSegment(ExecutionSegment segment,
            int expectedProcessNumber,
            int expectedStartTime,
            int expectedEndTime) {
        assertEquals(expectedProcessNumber, segment.process().getProcessNumber());
        assertEquals(expectedStartTime, segment.startTime());
        assertEquals(expectedEndTime, segment.endTime());
    }

}
