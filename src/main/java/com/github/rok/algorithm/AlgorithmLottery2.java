package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import java.util.Random;

import com.github.rok.os.Process;

public class AlgorithmLottery2 implements AlgorithmInterface{

    private final IMainController controller;

    public AlgorithmLottery2( IMainController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        Random random = new Random();
        int numeroAleatorio = random.nextInt(controller.getIMemory().getMemorySize());

        Process nextProcessByInitial = controller.getIMemory().getProcessOnListPos(numeroAleatorio);
        if (nextProcessByInitial == null) return;
        controller.addProcessToCPU(nextProcessByInitial, controller.getTimeOnCpu());
    }

    @Override
    public void tickNewProcess(Process newProcess) {

    }
}
