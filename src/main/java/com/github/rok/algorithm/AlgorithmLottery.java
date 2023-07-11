//package com.github.rok.algorithm;
//
//import com.github.rok.interfaces.IMainController;
//import com.github.rok.interfaces.AlgorithmInterface;
//import com.github.rok.os.Process;
//import com.github.rok.utils.Utils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
///**
// * O algoritmo Lottery atribui bilhetes a cada processo com base em sua prioridade.
// * Um bilhete aleatório é sorteado, e o processo que possui esse bilhete é executado.
// * O processo com prioridade mais alta terá mais bilhetes, aumentando suas chances de ser selecionado.
// * Esse algoritmo garante que processos com prioridade mais alta sejam executados com mais frequência.
// */
//public class AlgorithmLottery implements AlgorithmInterface {
//    private final IMainController controller;
//    private final Random random;
//
//    public AlgorithmLottery(IMainController controller) {
//        this.controller = controller;
//        this.random = new Random();
//    }
//
//    @Override
//    public void execute() {
//        List<Process> processosElegiveis = obterProcessosElegiveis();
//        if (processosElegiveis.isEmpty()) return;
//
//        Process processoSelecionado = sortearVencedor(processosElegiveis);
//        controller.addProcessToCPU(processoSelecionado, controller.getTimeOnCpu());
//    }
//
//    private List<Process> obterProcessosElegiveis() {
//        List<Process> processosElegiveis = new ArrayList<>();
//        for (int i = 0; i < controller.getIMemory().getNextEmptySlot(); i++) {
//            Process processo = controller.getIMemory().getProcessOnListPos(i);
//            if (processo != null && processo.getWaitingTime() > 0) {
//                processosElegiveis.add(processo);
//            }
//        }
//        return processosElegiveis;
//    }
//
//    private Process sortearVencedor(List<Process> processos) {
//        int totalBilhetes = getTotalBilhetes(processos);
//        int bilheteVencedor = random.nextInt(totalBilhetes) + 1;
//
//        int bilhetesCumulativos = 0;
//        for (Process processo : processos) {
//            int bilhetes = calcularBilhetes(processo);
//            bilhetesCumulativos += bilhetes;
//            if (bilhetesCumulativos >= bilheteVencedor) {
//                return processo;
//            }
//        }
//
//        // Isso nunca deveria acontecer, a menos que não haja processos elegíveis
//        return null;
//    }
//
//    private int getTotalBilhetes(List<Process> processos) {
//        int totalBilhetes = 0;
//        for (Process processo : processos) {
//            totalBilhetes += calcularBilhetes(processo);
//        }
//        return totalBilhetes;
//    }
//
//    private int calcularBilhetes(Process processo) {
//        int faixaPrioridade = controller.getPriorMax() - controller.getPriorMin() + 1;
//        double porcentagemPrioridade = processo.getWaitingTime() / controller.getTimeOnCpu();
//        return (int) Math.round(porcentagemPrioridade * faixaPrioridade);
//    }
//}
