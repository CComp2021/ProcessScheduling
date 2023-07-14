package com.github.rok.algorithm;

import com.github.rok.os.Process;
import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmGuaranteed implements AlgorithmInterface {

    private final IMainController controller;

    public AlgorithmGuaranteed(IMainController mainController) {
        this.controller = mainController;
    }

    Process lastProcess;
    int lastProcessIndex = 0;
    public void execute() {
        if (lastProcess == null) {
            lastProcess = controller.getIMemory().getHighestPriorityProcess();
            lastProcessIndex = (int) lastProcess.getPriority();
            controller.addProcessToCPU(lastProcess, controller.getTimeOnCpu());
            return;
        }
        lastProcess.setPriority(lastProcess.getPriority() + 0.001);

        Process nextProcess = controller.getIMemory().getHighestPriorityProcessBelow(lastProcess,lastProcessIndex);
        if (nextProcess == null) {
            nextProcess = controller.getIMemory().getHighestPriorityProcess();
        }
        if (nextProcess == null) return;
        lastProcess = nextProcess;
        lastProcessIndex = (int) lastProcess.getPriority();
        System.out.println(lastProcess.getPriority());
        controller.addProcessToCPU(nextProcess, controller.getTimeOnCpu());
    }

    @Override
    public void tickNewProcess(Process newProcess) {

    }
}
