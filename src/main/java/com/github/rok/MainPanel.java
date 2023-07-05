package com.github.rok;

import com.github.rok.algorithm.AlgorithmInterface;
import com.github.rok.os.*;
import com.github.rok.os.Process;
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

	private Memory memory;
	private CPU cpu;

	private final Frame frame;
	private ChartsFrame chartsFrame;

	private AlgorithmInterface algorithm;

	private boolean running = false;
	public MainPanel() {
		//Cria os modulos do sistema
		this.cpu = new CPU(this);
		this.memory = new Memory(this);
		chartsFrame = new ChartsFrame(this, memory);
		frame = new Frame(this, chartsFrame);
	}
	public void importAlgorithms() {
		JComboBox<String> algorithmBox = (JComboBox<String>) frame.getComponent("algorithm");
		for (String algorithm : Main.getAlgorithmsNameList()) {
			algorithmBox.addItem(algorithm);
		}
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public void addProcessToCPU(Process process, double timeProcessing) {
		cpu.addProcessToCPU(process, timeProcessing);
	}

	public void setRunning(boolean running) {
		this.running = running;
		memory.pause(!running);
		memory.clearMemory();
		if (running)
			memory.addRandomProcessToMemory();

		cpu.endProcess();
		cpu.pause(!running);
		updateCenterBar(0,"Feito por Pedro Lucas Nascimento, Caio Lapa, Kaio Stefan e Joao Victor Mascarenhas.");

		chartsFrame.updateCPUChartColor(true);
		updateMemoryBar(0);
		updateCPUBar(0);
		updateMemoryChart();
		updateCPUChart();
	}

	public void pause(boolean pause) {
		memory.pause(pause);
		cpu.pause(pause);
	}

	// Toda atualização na lista de processos, esse método deve ser chamado para atualizar o gráfico
	public void updateProperties() {
		// Memory
		memory.setGenerationSpeed(((int) ((JSpinner) frame.getComponent("process_delay")).getValue()) * 100);
		int processMax = (int) ((JSpinner) frame.getComponent("process_max")).getValue();
		int processMin = (int) ((JSpinner) frame.getComponent("process_min")).getValue();
		memory.setProcessSize(processMin, processMax);
		chartsFrame.getMemoryChart().getStyler().setYAxisMax((double) processMax);

		// CPU
		cpu.setScalingDelay((double) ((JSpinner) frame.getComponent("scaling_delay")).getValue());
		cpu.setProcessSpeed(((int) ((JSpinner) frame.getComponent("cpu_speed")).getValue()));
		algorithm = Main.getAlgorithm((String) ((JComboBox<?>) frame.getComponent("algorithm")).getSelectedItem());
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
		chartsFrame.updateCPUChart(processRunning);
		if (processRunning) {
			lastProcess = cpu.getRunningProcess();
		} else {
			// TODO: TESTANDO OUTROS METODOS APAGAR DEPOIS
			useCPUWithAlgorithm();
			updateMemoryChart();
		}
	}

	public void useCPUWithAlgorithm() {
		// TODO: ROBIN REDONDO EMULADO
		algorithm.execute();
		/*
		Process firstProcess = memory.getFirstProcess();
		if (firstProcess == null) return;
		int i = lastProcess == null ? firstProcess.getId() : lastProcess.getId() + 1;
		while (memory.getProcess(i) == null && i < lastProcess.getId() + 9) {
			i++;
		}
		Process next = memory.getProcess(i) == null ? firstProcess : memory.getProcess(i);
		addProcessToCPU(next, (int) ((JSpinner) frame.getComponent("cpu_running_time")).getValue());*/
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


	@Override
	public void updateTick() {
		if (cpu.getRunningProcess() != null ) {
			updateCPUBar((int) ((int) Utils.getPercentageToValue(cpu.getInitialTime(), cpu.getTimeProcessing())*cpu.getProcessSpeed()));
			updateCPUChart();
			return;
		}
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
			updateCenterBar((int) (100 -  completePercentage), "Escalonando para Memória...");
			updateCPUChart();
			return;
		}
		updateCenterBar((int) completePercentage, "Escalonando para CPU...");
		updateCPUChart();
	}
	@Override
	public void memoryTick(double nextGen) {
		if (nextGen <= 0) {
			updateMemoryChart();
		}
		if (!cpu.isRunning() && memory.getFirstProcess() != null) {
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
	public IMemory getIMemory() {
		return memory;
	}
	@Override
	public ICPU getICPU() {
		return cpu;
	}
}
