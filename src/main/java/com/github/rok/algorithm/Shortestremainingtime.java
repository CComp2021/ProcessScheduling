package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

/*
 * @author Fantiko, Kaio Stefan Campos Nunes created on 07/07/2023
 */
public class Shortestremainingtime implements AlgorithmInterface{
    private final IMainController controller;

    public Shortestremainingtime(IMainController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        Process ShortestRemainingTime = controller.getIMemory().getLowestTimeProcess();
        if (ShortestRemainingTime == null) return;
        controller.addProcessToCPU(ShortestRemainingTime, ShortestRemainingTime.getWaitingTime()/2);
    }
}
