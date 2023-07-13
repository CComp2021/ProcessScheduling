package com.github.rok;

import com.github.rok.interfaces.*;
import com.github.rok.os.*;
import com.github.rok.os.Process;
import com.github.rok.os.interfaces.ICPU;
import com.github.rok.os.interfaces.IMemory;
import com.github.rok.panel.ChartsFrame;
import com.github.rok.panel.Frame;
import com.github.rok.utils.Utils;

import javax.swing.*;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class MainPanel implements IController, IMainController {

    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 500;

    // Properiedades de Algoritmos
    private double processOnCPU;
    private int priorMax;
    private int priorMin;

    // Counter
    private int minutes;
    private int seconds;

    private Memory memory;
    private CPU cpu;
    private Counter counter;
    private IReport report;

    private final Frame frame;
    private ChartsFrame chartsFrame;

    private AlgorithmInterface algorithm;

    private boolean running = false;
    private boolean paused = false;

    public MainPanel() {
        this.cpu = new CPU(this);
        this.memory = new Memory(this);
        this.counter = new Counter(this);
        chartsFrame = new ChartsFrame(this, memory);
        frame = new Frame(this, chartsFrame);
        report = new Report(frame.getReportPanel(), cpu, memory.getEnded(),this);
    }

    public void importAlgorithms() {
        JComboBox<String> algorithmBox = (JComboBox<String>) frame.getComponent("algorithm");
        for (String algorithm : Main.getAlgorithmsNameList()) {
            algorithmBox.addItem(algorithm);
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }


    @Override
    public boolean addProcessToCPU(Process process, double timeProcessing) {
        boolean b = cpu.addProcessToCPU(process, timeProcessing);
        updateMemoryChart();
        return b;
    }

    @Override
    public void removeControlButtons() {
        frame.removeControlButtons();
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
        memory.pause(!running);
        memory.clearMemory();
        report.setRunning(running);
        if (running) {
            paused = false;
            memory.addRandomProcessToMemory();
            counter.setCounter(minutes, seconds);
        }
        cpu.endProcess();
        cpu.pause(!running);
        updateCenterBar(0, "Feito por Pedro Lucas Nascimento, Caio Lapa, Kaio Stefan e Joao Victor Mascarenhas.");

        chartsFrame.updateCPUChartColor(true);
        updateMemoryBar(0);
        updateCPUBar(0);
        updateMemoryChart();
        updateCPUChart();
    }

    public void pause(boolean pause) {
        memory.pause(pause);
        cpu.pause(pause);
        counter.setPaused(pause);
        paused = pause;
    }

    // Toda atualização na lista de processos, esse método deve ser chamado para atualizar o gráfico
    public void updateProperties() {
        // Memory
        memory.setGenerationSpeed((int) (frame.getComponentDouble("process_delay") * 100));
        int processMax = frame.getComponentInt("process_max");
        int processMin = frame.getComponentInt("process_min");
        memory.setProcessSize(processMin, processMax);
        chartsFrame.getMemoryChart().getStyler().setYAxisMax((double) processMax+2);

        // CPU
        cpu.setScalingDelay(frame.getComponentDouble("scaling_delay"));
        cpu.setProcessSpeed(frame.getComponentDouble("cpu_speed"));
        algorithm = Main.getAlgorithm((String) ((JComboBox<?>) frame.getComponent("algorithm")).getSelectedItem());

        // Counter
        minutes = frame.getComponentInt("simulation_min");
        seconds = frame.getComponentInt("simulation_sec");

        // Properties
        processOnCPU = frame.getComponentDouble("cpu_running_time");
        priorMax = frame.getComponentInt("prior_max");
        priorMin = frame.getComponentInt("prior_min");
    }

    private void updateMemoryBar(int value) {
        chartsFrame.updateMemoryBar(value);
    }

    private void updateCPUBar(int value) {
        chartsFrame.updateCpuBar(value);
    }

    private void updateCenterBar(int value, String text) {
        frame.updateCenterBar(value, text);
    }

    private void updateMemoryChart() {
        chartsFrame.updateMemoryChart();
    }

    private Process lastProcess = null;

    public void updateCPUChart() {
        boolean processRunning = cpu.getRunningProcess() != null;
        chartsFrame.updateCPUChart(processRunning, counter.isRunning());
        if (processRunning) {
            lastProcess = cpu.getRunningProcess();
        } else {
            if (cpu.isScaling()) return;
            useCPUWithAlgorithm();
            updateMemoryChart();
        }
    }

    public void useCPUWithAlgorithm() {
        algorithm.execute();
    }

    public CPU getCpu() {
        return cpu;
    }

    public Memory getMemory() {
        return memory;
    }

    public int getWindowWidth() {
        return WINDOW_WIDTH;
    }

    public int getWindowHeight() {
        return WINDOW_HEIGHT;
    }

    public boolean isPaused() {
        return paused;
    }

    @Override
    public void updateTick() {
        if (cpu.getRunningProcess() == null) {
            updateCPUChart();
            return;
        }
        updateCPUBar((int) Utils.getPercentageToValue(cpu.getInitialTime(), cpu.getTimeProcessing()));
        updateCPUChart();
    }

    @Override
    public void scalingTick(boolean toMemory, double completePercentage, CPU.STATE isRunning) {
        if (isRunning == CPU.STATE.COMPUTING) {
            updateCenterBar(100, "Aguardando...");
        }
        if (isRunning == CPU.STATE.WAITING) {
            updateCenterBar(0, "Aguardando...");
        }
        if (cpu.isRunning()) {
            chartsFrame.updateCPUChartColor(true);
        }
        if (isRunning != CPU.STATE.SCALING) return;
        chartsFrame.updateCPUChartColor(false);
        if (toMemory) {
            updateCenterBar((int) (100 - completePercentage), "Escalonando para Memória...");
            updateCPUChart();
            return;
        }
        updateCenterBar(((int) completePercentage), "Escalonando para CPU...");
        updateCPUChart();
    }

    @Override
    public void memoryTick(double nextGen) {

        if (counter.isRunning()) {
            frame.updateTime(counter.getTime());
        }
        if (nextGen <= 0) {
            updateMemoryChart();
        }
        if (!cpu.isRunning() && memory.getFirstProcess() != null && !cpu.isScaling()) {
            useCPUWithAlgorithm();
        }

        updateMemoryBar(100 - (int) (nextGen / (memory.getGenerationSpeed() / 100)));
    }

    @Override
    public void sendToMemory(Process process) {
        if (process.getWaitingTime() <= 0)
            getMemory().removeProcess(process);
        updateMemoryChart();
        if (getMemory().isEmpty()) {
            chartsFrame.clearCPUChart();
            updateCPUBar(0);
        }
        updateCPUChart();
    }

    @Override
    public void memoryNewProcessTick(Process newProcess) {
        algorithm.tickNewProcess(newProcess);
    }

    @Override
    public IMemory getIMemory() {
        return memory;
    }

    @Override
    public ICPU getICPU() {
        return cpu;
    }

    @Override
    public double getTimeOnCpu() {
        return processOnCPU;
    }

    @Override
    public Process getLastProcess() {
        return lastProcess;
    }

    public int getPriorMax() {
        return priorMax;
    }

    public int getPriorMin() {
        return priorMin;
    }
}
