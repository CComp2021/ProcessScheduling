package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

/**
 * Algoritmo de Escalonamento Garantido (Round Robin)
 */
public class AlgorithmRoundRobin implements AlgorithmInterface {
    private final IMainController controller;

    private int pos = 0;

    public AlgorithmRoundRobin(IMainController controller) {
        this.controller = controller;
    }

    public void execute() {
        Process nextProcess = controller.getIMemory().getNextProcessOnList();
        if(nextProcess == null)return;
        controller.addProcessToCPU(nextProcess, controller.getTimeOnCpu());
        pos++;
    }
}

