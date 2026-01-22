package com.mahmoud_ahmed.scheduling.state;

import com.mahmoud_ahmed.model.Process;
import com.mahmoud_ahmed.scheduling.context.SchedulingContext;

import java.util.*;
import java.util.stream.Collectors;

public class ProcessQueueManager {
    private final Queue<SchedulingContext> readyQueue;
    private final List<SchedulingContext> waitingProcesses;
    private SchedulingContext activeContext;

    public ProcessQueueManager(List<Process> processes, Comparator<SchedulingContext> comparator) {
        this.readyQueue = comparator == null ? new LinkedList<>() : new PriorityQueue<>(comparator);
        this.waitingProcesses = wrapAndSortProcesses(processes);
        this.activeContext = null;
    }

    private static List<SchedulingContext> wrapAndSortProcesses(List<Process> processes) {
        return processes.stream()
                .sorted(Comparator.comparingInt(Process::getArrivalTime)
                        .thenComparingInt(Process::getProcessNumber))
                .map(SchedulingContext::new)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void moveArrivedToReady(int currentTime) {
        while (hasWaiting() && waitingProcesses.getFirst().getArrivalTime() <= currentTime) {
            readyQueue.add(waitingProcesses.removeFirst());
        }
    }

    public boolean hasUnfinished() {
        return hasWaiting() || hasReady() || hasActive();
    }

    public boolean hasActive() {
        return activeContext != null;
    }

    public boolean hasReady() {
        return !readyQueue.isEmpty();
    }

    public boolean hasWaiting() {
        return !waitingProcesses.isEmpty();
    }

    public int getNextArrivalTime() {
        return waitingProcesses.getFirst().getArrivalTime();
    }

    public void activateNextReady() {
        activeContext = readyQueue.poll();
    }

    public SchedulingContext getActiveContext() {
        return activeContext;
    }

    public SchedulingContext peekNextReady() {
        return readyQueue.peek();
    }

    public void clearActive() {
        activeContext = null;
    }

    public void requeueActive() {
        readyQueue.add(activeContext);
        activeContext = null;
    }
}
