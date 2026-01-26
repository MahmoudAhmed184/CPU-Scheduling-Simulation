# CPU Scheduling Simulation

## Overview

The CPU Scheduling Simulation is a Java-based engine designed to model and execute various CPU scheduling algorithms. It provides a framework for defining processes with specific arrival times, burst times, and priorities, and simulates their execution order based on selected scheduling strategies. The system generates detailed execution timelines and calculates performance metrics, including turnaround time and waiting time.

The project is implemented using Java 23 and adheres to Clean Architecture principles to ensure modularity and separation of concerns.

## Table of Contents

1.  [System Architecture](#system-architecture)
2.  [Supported Algorithms](#supported-algorithms)
3.  [Project Structure](#project-structure)
4.  [Prerequisites](#prerequisites)
5.  [Installation](#installation)
6.  [Usage](#usage)
7.  [Metrics](#metrics)
8.  [License](#license)

## System Architecture

The system follows a modular design separating the core simulation engine from the scheduling strategies and data presentation.

### Core Components

*   **SchedulingEngine**: The primary entry point (Facade) for the application. It accepts input data and configuration, initializes the appropriate components, and returns the simulation results.
*   **Simulator**: Manages the simulation lifecycle. It tracks the system clock and invokes the scheduling algorithm to determine process allocation.
*   **SchedulingContext**: Encapsulates the runtime state of a process, such as remaining burst time and accumulated wait time.
*   **SchedulingAlgorithm**: An interface defining the contract for process scheduling. Concrete implementations (e.g., RoundRobin, FCFS) define specific allocation logic.
*   **MetricsCalculator**: A utility component that derives statistical performance data from the raw execution timeline.

## Supported Algorithms

The application implements the following scheduling algorithms:

| Algorithm | Type | Description |
| :--- | :--- | :--- |
| **First-Come, First-Served (FCFS)** | Non-Preemptive | Processes are executed in order of arrival. |
| **Shortest Job First (SJF)** | Non-Preemptive | The process with the shortest burst time is executed next. Ties are broken by arrival time. |
| **Round Robin (RR)** | Preemptive | Each process is assigned a fixed time quantum. Unfinished processes are preempted and placed at the end of the ready queue. |
| **Priority Scheduling** | Non-Preemptive | The process with the highest priority (lowest numerical value) is executed next. |
| **Priority Scheduling** | Preemptive | If a new process arrives with a higher priority than the currently running process, the current process is preempted. |

## Project Structure

The codebase is organized into the following packages:

*   `com.mahmoud_ahmed.simulation`: Contains the `SchedulingEngine` class.
*   `com.mahmoud_ahmed.scheduling`: Contains the `Simulator` and the high-level `SchedulingAlgorithm` interface.
*   `com.mahmoud_ahmed.scheduling.algorithms`: Contains concrete implementations of the scheduling algorithms.
*   `com.mahmoud_ahmed.scheduling.context`: Contains the `SchedulingContext` class for tracking process state.
*   `com.mahmoud_ahmed.scheduling.metrics`: Contains the `MetricsCalculator` for statistical analysis.
*   `com.mahmoud_ahmed.model`: Contains immutable data classes: `Process`, `Result`, `ExecutionSegment`, and `ProcessMetrics`.
*   `com.mahmoud_ahmed.presentation`: Contains classes responsible for formatting and displaying output.

## Prerequisites

*   Java Development Kit (JDK) 23 or later.
*   Apache Maven 3.8 or later.

## Installation

To build the project from source:

1.  Clone the repository:
    ```bash
    git clone https://github.com/MahmoudAhmed184/CPU-Scheduling-Simulation.git
    ```
2.  Navigate to the project directory:
    ```bash
    cd CPU-Scheduling-Simulation
    ```
3.  Compile and package the application:
    ```bash
    mvn clean package
    ```

## Usage

### Command Line Execution

The application can be run directly via Maven using the `exec` plugin:

```bash
mvn exec:java -Dexec.mainClass="com.mahmoud_ahmed.Main"
```

### Library Integration

The simulation engine can be embedded into other Java applications. The following example demonstrates how to configure and run a Round Robin simulation:

```java
import com.mahmoud_ahmed.simulation.SchedulingEngine;
import com.mahmoud_ahmed.model.Process;
import java.util.List;

public class SimulationExample {
    public static void main(String[] args) {
        SchedulingEngine engine = new SchedulingEngine();

        // Process Definition: ID, Arrival Time, Burst Time, Priority
        List<Process> processList = List.of(
            new Process(1, 0, 10, 3),
            new Process(2, 2,  4, 1),
            new Process(3, 4,  6, 2)
        );

        // Execute Round Robin with a Time Quantum of 2
        engine.runRoundRobin(processList, 2);
    }
}
```

## Metrics

The simulation outputs the following performance metrics:

*   **Turnaround Time**: The total time elapsed from the arrival of the process to its completion.
    $$Turnaround Time = Completion Time - Arrival Time$$
*   **Waiting Time**: The total time a process spends in the ready queue waiting for the CPU.
    $$Waiting Time = Turnaround Time - Burst Time$$

## License

This project is licensed under the MIT License.
