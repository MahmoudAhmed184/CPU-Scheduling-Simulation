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

public class NonPreemptivePriorityTest {
    private NonPreemptivePriority nonPreemptivePriority;

    @BeforeEach
    void setUp() {
        nonPreemptivePriority = new NonPreemptivePriority();
    }

    @ParameterizedTest(name = "{index} - Test with {0}")
    @MethodSource("provideTestCases")
    void whenSchedulingProcesses_shouldFollowFCFSRules(String testCase, List<Process> input, List<ExecutionSegment> expected) {
        List<ExecutionSegment> result = nonPreemptivePriority.schedule(input);
        assertEquals(expected.size(), result.size(), "Number of execution segments should match");
        for (int i = 0; i < expected.size(); i++) {
            assertExecutionSegment(
                result.get(i),
                expected.get(i).process().getProcessNumber(),
                expected.get(i).startTime(),
                expected.get(i).endTime()
            );
        }
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of(
                "Same arrival time, different priorities",
                Arrays.asList(
                    new Process(1, 0, 4, 2),
                    new Process(2, 0, 3, 5),
                    new Process(3, 0, 2, 3)
                ),
                Arrays.asList(
                    new ExecutionSegment(new Process(1, 0, 4, 2), 0, 4),
                    new ExecutionSegment(new Process(3, 0, 2, 3), 4, 6),
                    new ExecutionSegment(new Process(2, 0, 3, 5), 6, 9)
                )
            ),
            Arguments.of(
                "Later arrival of higher priority process",
                Arrays.asList(
                    new Process(1, 0, 6, 2),
                    new Process(2, 2, 3, 5)
                ),
                Arrays.asList(
                    new ExecutionSegment(new Process(1, 0, 6, 2), 0, 6),
                    new ExecutionSegment(new Process(2, 2, 3, 5), 6, 9)
                )
            ),
            Arguments.of(
                "Equal priorities, should follow FCFS",
                Arrays.asList(
                    new Process(1, 1, 3, 3),
                    new Process(2, 0, 4, 3),
                    new Process(3, 2, 2, 3)
                ),
                Arrays.asList(
                    new ExecutionSegment(new Process(2, 0, 4, 3), 0, 4),
                    new ExecutionSegment(new Process(1, 1, 3, 3), 4, 7),
                    new ExecutionSegment(new Process(3, 2, 2, 3), 7, 9)
                )
            ),
            Arguments.of(
                "Gaps between arrivals with priorities",
                Arrays.asList(
                    new Process(1, 0, 4, 2),
                    new Process(2, 5, 2, 5),
                    new Process(3, 5, 3, 3)
                ),
                Arrays.asList(
                    new ExecutionSegment(new Process(1, 0, 4, 2), 0, 4),
                    new ExecutionSegment(new Process(3, 5, 3, 3), 5, 8),
                    new ExecutionSegment(new Process(2, 5, 2, 5), 8, 10)
                )
            ),
            Arguments.of(
                "Process with zero burst time and priorities",
                Arrays.asList(
                    new Process(1, 0, 0, 1),
                    new Process(2, 0, 3, 3),
                    new Process(3, 0, 2, 5)
                ),
                Arrays.asList(
                    new ExecutionSegment(new Process(1, 0, 0, 1), 0, 0),
                    new ExecutionSegment(new Process(2, 0, 3, 3), 0, 3),
                    new ExecutionSegment(new Process(3, 0, 2, 5), 3, 5)
                )
            ),
            Arguments.of(
                "Complex scenario with multiple priorities and arrivals",
                Arrays.asList(
                    new Process(1, 0, 4, 2),
                    new Process(2, 1, 3, 5),
                    new Process(3, 2, 2, 4),
                    new Process(4, 3, 1, 3)
                ),
                Arrays.asList(
                    new ExecutionSegment(new Process(1, 0, 4, 2), 0, 4),
                    new ExecutionSegment(new Process(4, 3, 1, 3), 4, 5),
                    new ExecutionSegment(new Process(3, 2, 2, 4), 5, 7),
                    new ExecutionSegment(new Process(2, 1, 3, 5), 7, 10)
                )
            ),
            Arguments.of(
                "Multiple processes with same priority arriving together",
                Arrays.asList(
                    new Process(1, 0, 2, 4),
                    new Process(2, 0, 3, 4),
                    new Process(3, 0, 1, 4)
                ),
                Arrays.asList(
                    new ExecutionSegment(new Process(1, 0, 2, 4), 0, 2),
                    new ExecutionSegment(new Process(2, 0, 3, 4), 2, 5),
                    new ExecutionSegment(new Process(3, 0, 1, 4), 5, 6)
                )
            ),
            Arguments.of(
                "Processes with negative priority numbers",
                Arrays.asList(
                    new Process(1, 0, 3, -1),
                    new Process(2, 0, 2, -3),
                    new Process(3, 0, 4, -2)
                ),
                Arrays.asList(
                    new ExecutionSegment(new Process(2, 0, 2, -3), 0, 2),
                    new ExecutionSegment(new Process(3, 0, 4, -2), 2, 6),
                    new ExecutionSegment(new Process(1, 0, 3, -1), 6, 9)
                )
            )
        );
    }

    @Test
    void whenProcessListIsEmpty_shouldReturnEmptyResult() {
        assertTrue(nonPreemptivePriority.schedule(Collections.emptyList()).isEmpty());
    }

    @Test
    void whenSchedulingLargeNumberOfProcesses_shouldCompleteInReasonableTime() {
        List<Process> processes = IntStream.range(1, 1000)
            .mapToObj(i -> new Process(i, i, 30, 0))
            .collect(Collectors.toList());

        long startTime = System.currentTimeMillis();
        nonPreemptivePriority.schedule(processes);
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
