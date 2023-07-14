package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

public class AlgorithmPriority implements AlgorithmInterface {

    private final IMainController controller;

    private  Process currentProcess = null;
    Process nextProcess;

    public AlgorithmPriority (IMainController controller){
        this.controller = controller;
    }

    @Override
    public void execute() {
        if(currentProcess != null){
            if(currentProcess.getPriority() != 1d)
                currentProcess.setPriority(currentProcess.getPriority());
        }

        nextProcess = controller.getIMemory().getHighestPriorityProcess();

        if(nextProcess == null)return;
        currentProcess = nextProcess;
        controller.addProcessToCPU(currentProcess, currentProcess.getWaitingTime());
    }

    @Override
    public void tickNewProcess(Process newProcess) {

    }
}
