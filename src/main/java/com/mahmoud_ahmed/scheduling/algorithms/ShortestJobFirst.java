package com.mahmoud_ahmed.scheduling.algorithms;
import java.util.Comparator;
import com.mahmoud_ahmed.model.Process;

public class ShortestJobFirst extends NonPreemptiveAlgorithm{
    public ShortestJobFirst() {
        super(Comparator.comparingInt(Process::getBurstTime).thenComparing(Comparator.naturalOrder()));
    }
}