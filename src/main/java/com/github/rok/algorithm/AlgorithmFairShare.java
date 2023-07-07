package com.github.rok.algorithm;

import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.interfaces.IMainController;
import com.github.rok.os.Process;

import java.util.List;

public class AlgorithmFairShare implements AlgorithmInterface {
    private final IMainController controller;
    private int timeQuantum; // Tempo de quantum para cada processo

    public AlgorithmFairShare(IMainController controller, int timeQuantum) {
        this.controller = controller;
        this.timeQuantum = timeQuantum;
    }

    @Override
    public void execute() {
        List<Process> eligibleProcesses = controller.getIMemory().getEligibleProcesses();
        if (eligibleProcesses.isEmpty()) return;

        for (Process process : eligibleProcesses) {
            int processTime = (int) Math.min(process.getProcessTime(), timeQuantum);
            controller.addProcessToCPU(process, processTime);
            process.reduceProcessTime(processTime);
        }
    }
}
