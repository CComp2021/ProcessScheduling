package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgorithmLottery implements AlgorithmInterface {
    private final IMainController controller;

    public AlgorithmLottery(IMainController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        List<Process> allProcesses = controller.getIMemory().getAllProcesses();

        if (allProcesses.isEmpty()) {
            return;
        }

        int totalTickets = calculateTotalTickets(allProcesses);
        int winningTicket = generateWinningTicket(totalTickets);

        int accumulatedTickets = 0;
        Process selectedProcess = null;

        for (Process process : allProcesses) {
            accumulatedTickets += process.getPriority();

            if (accumulatedTickets >= winningTicket) {
                selectedProcess = process;
                break;
            }
        }

        if (selectedProcess != null) {
            controller.addProcessToCPU(selectedProcess, selectedProcess.getProcessTime());
        }
    }

    private int calculateTotalTickets(List<Process> processes) {
        int totalTickets = 0;

        for (Process process : processes) {
            totalTickets += process.getPriority();
        }

        return totalTickets;
    }

    private int generateWinningTicket(int totalTickets) {
        Random random = new Random();
        return random.nextInt(totalTickets) + 1;
    }
}
