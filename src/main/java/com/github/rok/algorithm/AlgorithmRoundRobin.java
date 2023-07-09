package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

/**
 * Algoritmo de Escalonamento Garantido (Round Robin)
 */
public class AlgorithmRoundRobin implements AlgorithmInterface {
    private final IMainController controller;

    private static final double TIME_QUANTUM = 4.0; // Quantum de tempo (em segundos)

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
        } else {
            // Verifica se o tempo do processo atual esgotou
            if (remainingTime <= 0) {
                // Envia o processo atual de volta para a memória
                controller.addProcessToCPU(currentProcess, currentProcess.getProcessTime());
                currentProcess = null;
                remainingTime = 0;
            } else {
                // Verifica se há algum processo com prioridade maior
                Process highestPriorityProcess = controller.getIMemory().getHighestIdProcess();
                if (highestPriorityProcess != null && highestPriorityProcess.getId() > currentProcess.getId()) {
                    // Envia o processo atual de volta para a memória
                    controller.addProcessToCPU(currentProcess, remainingTime);
                    currentProcess = highestPriorityProcess;
                    remainingTime = TIME_QUANTUM;
                    controller.addProcessToCPU(currentProcess, TIME_QUANTUM);
                } else {
                    // Continua executando o processo atual
                    remainingTime -= 0.01; // Reduz o tempo restante do processo (simulação)
                }
            }
        }

    }
}
