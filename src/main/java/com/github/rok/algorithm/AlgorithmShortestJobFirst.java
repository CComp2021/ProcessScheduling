package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

/*
 * @author Fantiko, Kaio Stefan Campos Nunes created on 07/07/2023
 */
public class AlgorithmShortestJobFirst implements AlgorithmInterface{

    private final IMainController controller;

    public AlgorithmShortestJobFirst(IMainController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        Process lowestSizeProcess = controller.getIMemory().getLowestTimeProcess();
        if (lowestSizeProcess == null) {
            return;
        }
        controller.addProcessToCPU(lowestSizeProcess, 1);
    }
}