package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

/**
 * Algoritmo de Escalonamento Garantido (Round Robin)
 */
public class AlgorithmRoundRobin implements AlgorithmInterface {
    private final IMainController controller;

    private int lastProcessPos = 0;

    public AlgorithmRoundRobin(IMainController controller) {
        this.controller = controller;
    }

    public void execute() {
        Process nextProcessByInitial = controller.getIMemory().getNextProcessByInitial(lastProcessPos);
        if(nextProcessByInitial == null)return;
        lastProcessPos = controller.getIMemory().getListPosById(nextProcessByInitial.getId());
        controller.addProcessToCPU(nextProcessByInitial, controller.getTimeOnCpu());
    }


}


