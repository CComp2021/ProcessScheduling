package com.github.rok.algorithm;

import com.github.rok.os.Process;
import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.interfaces.ICPU;
import com.github.rok.os.interfaces.IMemory;

public class AlgorithmGuaranteed implements AlgorithmInterface {

    private final IMainController mainController;

    public AlgorithmGuaranteed(IMainController mainController) {
        this.mainController = mainController;
    }

    public void execute() {
        IMemory memory = mainController.getIMemory();
        ICPU cpu = mainController.getICPU();

        // Verifica se a CPU está livre
        if (!cpu.isRunning()) {
            // Obtém o processo com a maior prioridade na memória
            Process nextProcess = memory.getHighestPriorityProcess();

            // Verifica se há algum processo na memória
            if (nextProcess != null) {
                // Define o tempo de processamento do próximo processo na CPU
                double timeProcessing = mainController.getTimeOnCpu();

                // Adiciona o processo à CPU para execução
                mainController.addProcessToCPU(nextProcess, timeProcessing);
            }
        }
    }

    @Override
    public void tickNewProcess(Process newProcess) {

    }
}
