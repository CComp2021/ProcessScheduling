package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

/*
 * Author: Rok, Pedro Lucas N M Machado
 * Created on: 05/07/2023
 */
public class AlgorithmPriority implements AlgorithmInterface {
    private final IMainController controller;

    public AlgorithmPriority(IMainController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        Process highestPriorityProcess = getHighestPriorityProcess();
        if (highestPriorityProcess == null) return;
        controller.addProcessToCPU(highestPriorityProcess, controller.getTimeOnCpu());
    }

    private Process getHighestPriorityProcess() {
        Process highestPriorityProcess = null;
        for (int priority = controller.getPriorMax(); priority >= controller.getPriorMin(); priority--) {
            Process process = findProcessByPriority(priority);
            if (process != null) {
                if (highestPriorityProcess == null || process.getPriority() > highestPriorityProcess.getPriority()) {
                    highestPriorityProcess = process;
                }
            }
        }
        return highestPriorityProcess;
    }

    private Process findProcessByPriority(int priority) {
        for (int i = 0; i < 10; i++) {
            Process process = controller.getIMemory().getProcessOnListPos(i);
            if (process != null && process.getPriority() == priority) {
                return process;
            }
        }
        return null;
    }
}
