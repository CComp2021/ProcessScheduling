package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

/*
 * @author Fantiko, Kaio Stefan Campos Nunes created on 07/07/2023
 */
public class AlgorithmShortestremainingtime implements AlgorithmInterface{
    private final IMainController controller;

    public AlgorithmShortestremainingtime(IMainController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        Process shortestRemainingTime = controller.getIMemory().getLowestTimeProcess();
        if (shortestRemainingTime == null) return;
        controller.addProcessToCPU(shortestRemainingTime, shortestRemainingTime.getWaitingTime());
    }
}
