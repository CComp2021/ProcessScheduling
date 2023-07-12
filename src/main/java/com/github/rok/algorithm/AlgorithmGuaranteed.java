package com.github.rok.algorithm;

import com.github.rok.os.Process;
import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.interfaces.ICPU;
import com.github.rok.os.interfaces.IMemory;

public class AlgorithmGuaranteed implements AlgorithmInterface {

    private final IMainController mainController;
    private Process currentProcess = null;

    public AlgorithmGuaranteed(IMainController mainController) {
        this.mainController = mainController;
    }

    Process nextProcess;
    public void execute() {
        IMemory memory = mainController.getIMemory();
        ICPU cpu = mainController.getICPU();

        // Verifica se a CPU está livre
        if (!cpu.isRunning()) {
            // Obtém o processo com a maior prioridade na memória
            if(currentProcess == null) {
                currentProcess = memory.getHighestPriorityProcess();
                if(currentProcess == null)return;
                mainController.addProcessToCPU(currentProcess, mainController.getTimeOnCpu());
            }else{
                nextProcess = mainController.getIMemory().getHighestPriorityProcessBelow(currentProcess);
                if (nextProcess == null)return;
                currentProcess = nextProcess;
                mainController.addProcessToCPU(currentProcess, currentProcess.getTimeOnCPU());

            }

            if(currentProcess == mainController.getIMemory().getLowestPriorityProcess()){
                currentProcess=null;
            }

        }
    }

    @Override
    public void tickNewProcess(Process newProcess) {

    }
}
