package com.mahmoud_ahmed.scheduling.algorithms;

import com.mahmoud_ahmed.model.ExecutionSegment;
import com.mahmoud_ahmed.model.Process;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PreemptivePriorityTest {
    private PreemptivePriority preemptivePriority;

    @BeforeEach
    void setUp() {
        preemptivePriority = new PreemptivePriority();
    }

    @ParameterizedTest(name = "{index} - Test with {0}")
    @MethodSource("provideTestCases")
    void whenSchedulingProcesses_shouldFollowFCFSRules(String testCase, List<Process> input,
            List<ExecutionSegment> expected) {
        List<ExecutionSegment> result = preemptivePriority.schedule(input);
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
                        "Preemption by higher priority process",
                        Arrays.asList(
                                new Process(1, 0, 5, 2),
                                new Process(2, 2, 3, 1)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 5, 2), 0, 2),
                                new ExecutionSegment(new Process(2, 2, 3, 1), 2, 5),
                                new ExecutionSegment(new Process(1, 0, 5, 2), 5, 8))),
                Arguments.of(
                        "Multiple preemptions with varying priorities",
                        Arrays.asList(
                                new Process(1, 0, 6, 3),
                                new Process(2, 2, 4, 2),
                                new Process(3, 4, 2, 1)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 6, 3), 0, 2),
                                new ExecutionSegment(new Process(2, 2, 4, 2), 2, 4),
                                new ExecutionSegment(new Process(3, 4, 2, 1), 4, 6),
                                new ExecutionSegment(new Process(2, 2, 4, 2), 6, 8),
                                new ExecutionSegment(new Process(1, 0, 6, 3), 8, 12))),
                Arguments.of(
                        "No preemption when lower priority arrives",
                        Arrays.asList(
                                new Process(1, 0, 4, 1),
                                new Process(2, 2, 3, 2)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 4, 1), 0, 4),
                                new ExecutionSegment(new Process(2, 2, 3, 2), 4, 7))),
                Arguments.of(
                        "Equal priority with preemption timing",
                        Arrays.asList(
                                new Process(1, 0, 4, 2),
                                new Process(2, 1, 2, 2),
                                new Process(3, 2, 3, 2)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 4, 2), 0, 4),
                                new ExecutionSegment(new Process(2, 1, 2, 2), 4, 6),
                                new ExecutionSegment(new Process(3, 2, 3, 2), 6, 9))),
                Arguments.of(
                        "Complex scenario with immediate preemption",
                        Arrays.asList(
                                new Process(1, 0, 5, 3),
                                new Process(2, 0, 4, 2),
                                new Process(3, 0, 3, 1)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(3, 0, 3, 1), 0, 3),
                                new ExecutionSegment(new Process(2, 0, 4, 2), 3, 7),
                                new ExecutionSegment(new Process(1, 0, 5, 3), 7, 12))),
                Arguments.of(
                        "Preemption with late arrival of highest priority",
                        Arrays.asList(
                                new Process(1, 0, 8, 3),
                                new Process(2, 4, 2, 1)),
                        Arrays.asList(
                                new ExecutionSegment(new Process(1, 0, 8, 3), 0, 4),
                                new ExecutionSegment(new Process(2, 4, 2, 1), 4, 6),
                                new ExecutionSegment(new Process(1, 0, 8, 3), 6, 10))));
    }

    @Test
    void whenProcessListIsEmpty_shouldReturnEmptyResult() {
        assertTrue(preemptivePriority.schedule(Collections.emptyList()).isEmpty());
    }

    @Test
    void whenSchedulingLargeNumberOfProcesses_shouldCompleteInReasonableTime() {
        List<com.mahmoud_ahmed.model.Process> processes = IntStream.range(1, 1000)
                .mapToObj(i -> new Process(i, i, 30, 0))
                .collect(Collectors.toList());

        long startTime = System.currentTimeMillis();
        preemptivePriority.schedule(processes);
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
