package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

/**
 * Algoritmo de Escalonamento Garantido (Round Robin)
 */
public class AlgorithmRoundRobin implements AlgorithmInterface {
    private final IMainController controller;

    public AlgorithmRoundRobin(IMainController controller) {
        this.controller = controller;
    }

    Process nextProcess;
    public void execute() {
        nextProcess = controller.getIMemory().getNextProcessOnList();
        if (nextProcess == null) return;
        controller.addProcessToCPU(nextProcess, controller.getTimeOnCpu());
    }

    @Override
    public void tickNewProcess(Process newProcess) {

    }
}


