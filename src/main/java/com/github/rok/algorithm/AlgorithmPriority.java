package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

import java.util.List;
import java.util.Collections;

public class AlgorithmPriority implements AlgorithmInterface {
    private final IMainController controller;

    public AlgorithmPriority(IMainController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        List<Process> processes = controller.getIMemory().getAllProcesses();

        if (processes.isEmpty()) {
            return;
        }

        // Ordenar os processos por prioridade (assumindo que a prioridade é um atributo dos processos)
        Collections.sort(processes, (p1, p2) -> p2.getPriority() - p1.getPriority());

        Process highestPriorityProcess = processes.get(0);
        controller.addProcessToCPU(highestPriorityProcess, highestPriorityProcess.getProcessTime());
    }
}
