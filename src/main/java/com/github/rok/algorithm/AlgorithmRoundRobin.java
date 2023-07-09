package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

/**
 * Algoritmo de Escalonamento Garantido (Round Robin)
 */
public class AlgorithmRoundRobin implements AlgorithmInterface {
    private final IMainController controller;

    private static final double TIME_QUANTUM = 1.0; // Quantum de tempo (em segundos)

    private Process currentProcess; // Processo atualmente em execução
    private double remainingTime; // Tempo restante para o processo atual

    public AlgorithmRoundRobin(IMainController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        if (currentProcess == null) {
            // Verifica se há algum processo pronto para ser executado
            Process nextProcess = controller.getIMemory().getLowestIdProcess();
            if (nextProcess != null) {
                currentProcess = nextProcess;
                remainingTime = TIME_QUANTUM;
                controller.addProcessToCPU(currentProcess, TIME_QUANTUM);
            }
        }else if(currentProcess == controller.getLastProcess()){
            controller.addProcessToCPU(currentProcess,remainingTime);
            currentProcess = controller.getIMemory().getLowestIdProcess();
        } else {
            Process nextProcess = controller.getIMemory().getProcess(currentProcess.getId()+1);
            controller.addProcessToCPU(currentProcess,remainingTime);
            currentProcess = nextProcess;
            remainingTime = TIME_QUANTUM;
            controller.addProcessToCPU(currentProcess,TIME_QUANTUM);
        }

    }
}
