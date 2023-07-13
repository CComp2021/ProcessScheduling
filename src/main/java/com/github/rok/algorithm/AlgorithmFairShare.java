package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;
import com.github.rok.os.interfaces.IMemory;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * Algoritmo Fair Share para o escalonamento de processos.
 * O tempo de processamento é distribuído de forma justa entre os processos.
 */
public class AlgorithmFairShare implements AlgorithmInterface {
    private final IMainController controller;

    public AlgorithmFairShare(IMainController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {

        List<Process> processes = controller.getIMemory().getProcessList();

        // Verifica se há algum processo na memória
        if (processes.isEmpty()) {
            return;
        }

        // Calcula a fatia de tempo justa para cada processo
        double timeSlice = calculateTimeSlice(processes.size());
        // Adiciona cada processo à CPU com sua fatia de tempo
        Process nextProcessOnList = controller.getIMemory().getNextProcessOnList();
        if(nextProcessOnList == null) return;

        controller.addProcessToCPU(nextProcessOnList, timeSlice);

    }

    @Override
    public void tickNewProcess(Process newProcess) {

    }

    /**
     * Obtém todos os processos presentes na memória.
     *
     * @return Lista de processos presentes na memória.
     */
    private List<Process> getAllProcesses() {
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            Process process = controller.getIMemory().getProcessOnListPos(i);

            if (process != null) {
                processes.add(process);
            }
        }
        return processes;
    }

    /**
     * Calcula a fatia de tempo justa para cada processo, dividindo igualmente o tempo total disponível entre eles.
     *
     * @param numProcesses Número de processos presentes na memória.
     * @return A fatia de tempo justa para cada processo.
     */
    private double calculateTimeSlice(int numProcesses) {
        double totalProcessingTime = controller.getTimeOnCpu();
        return totalProcessingTime / numProcesses;
    }
}
