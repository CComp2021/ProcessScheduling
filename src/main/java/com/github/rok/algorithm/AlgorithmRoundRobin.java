package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

/**
 * Algoritmo de Escalonamento Garantido (Round Robin)
 */
public class AlgorithmRoundRobin implements AlgorithmInterface {
    private final IMainController controller;

    private int lastProcessPos = -1;

    public AlgorithmRoundRobin(IMainController controller) {
        this.controller = controller;
    }

    int ticko = 0;

    public void execute() {
        Process nextProcessByInitial = controller.getIMemory().getNextProcessOnList();
        if (nextProcessByInitial == null) return;
        if (controller.addProcessToCPU(nextProcessByInitial, controller.getTimeOnCpu())) {
            lastProcessPos = controller.getIMemory().getListPosById(nextProcessByInitial.getId());
        }
    }


}


