package com.github.rok;

import com.github.rok.os.CPU;
import com.github.rok.os.Memory;
import com.github.rok.os.Process;
import org.knowm.xchart.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class MainPanel {

	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;

	private Memory memory;
	private CPU cpu;

	private CategoryChart memoryChart;
	private PieChart cpuChart;

	private XChartPanel<CategoryChart> memoryChartPanel;
	private XChartPanel<PieChart> cpuChartPanel;

	public MainPanel() {

		//Cria os modulos do sistema
		this.memory = new Memory();
		this.cpu = new CPU(this);

		memoryChart = new CategoryChartBuilder().width(WINDOW_WIDTH / 2).height(WINDOW_HEIGHT)
				                       .title("Memória").build();
		memoryChart.getStyler().setLegendVisible(true);

		memoryChart.getStyler().setYAxisMax(10.0);
		memoryChart.getStyler().setAvailableSpaceFill(.50);
		memoryChart.getStyler().setShowStackSum(true);
		memoryChart.getStyler().setStacked(true);

		List<Integer> processIds = memory.processList.stream()
				                          .map(Process::getId)
				                          .collect(Collectors.toList());
		memoryChart.addSeries("Em espera", processIds,
				memory.processList.stream()
						.map(Process::getWaitingTime)
						.collect(Collectors.toList())
		);
		memoryChart.addSeries("Computado", processIds, memory.processList.stream()
				                                               .map(Process::getProcessTime)
				                                               .collect(Collectors.toList()));

		// Gráfico de linha em tempo real
		cpuChart = new PieChartBuilder().title("CPU").build();
		cpuChart.addSeries("Em espera", 0);
		cpuChart.addSeries("Computado", 0);

		// Criação do JFrame
		JFrame frame = new JFrame("Escalonamento De Processos");
		frame.setLayout(new java.awt.GridLayout(1, 3));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Definir a cor de fundo base do JFrame
		frame.getContentPane().setBackground(Color.decode("#191919"));

		// Painel para posicionar os elementos
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Adiciona os gráficos ao JFrame pt 1
		memoryChartPanel = new XChartPanel<>(memoryChart);
		cpuChartPanel = new XChartPanel<>(cpuChart);



		// Adiciona o botão de atualização
		JButton updateButton = new JButton("Add Random");
		updateButton.setPreferredSize(new Dimension(120, 20));
		updateButton.addActionListener(e -> addRandomProcessToMemory());

		JButton removeFirstBtn = new JButton("Remove First");
		removeFirstBtn.setPreferredSize(new Dimension(120, 40));
		removeFirstBtn.addActionListener(e -> {
			getMemory().removeProcess(getMemory().getFirstProcess());
			updateMemoryChart();
		});
		JButton removeLastBtn = new JButton("Remove Last");
		removeLastBtn.setPreferredSize(new Dimension(120, 40));
		removeLastBtn.addActionListener(e -> {
			getMemory().removeProcess(getMemory().getLastProcess());
			updateMemoryChart();
		});
		JButton addToCPU = new JButton("To CPU");
		addToCPU.setPreferredSize(new Dimension(120, 40));
		addToCPU.addActionListener(e -> {
			cpu.setRunningProcess(memory.getFirstProcess(), 2);
			updateMemoryChart();
		});




		panel.add(Box.createHorizontalGlue());
		panel.add(updateButton);
		panel.add(removeFirstBtn);
		panel.add(removeLastBtn);
		panel.add(addToCPU);
		panel.add(Box.createHorizontalGlue());

		// Adiciona os gráficos ao JFrame pt 2
		frame.add(memoryChartPanel);
		frame.add(panel);
		frame.add(cpuChartPanel);

		frame.pack();
		frame.setVisible(true);
	}

	public void addRandomProcessToMemory() {
		memory.addRandomProcessToMemory();
		updateMemoryChart();
	}

	// Toda atualização na lista de processos, esse método deve ser chamado para atualizar o gráfico
	public void updateMemoryChart() {
		List<Integer> processIds = memory.processList.stream()
				                           .map(Process::getId)
				                           .collect(Collectors.toList());
		memoryChart.updateCategorySeries("Em espera", processIds,
				memory.processList.stream()
						.map(Process::getWaitingTime)
						.collect(Collectors.toList()), null);
		memoryChart.updateCategorySeries("Computado", processIds,
				memory.processList.stream()
						.map(Process::getProcessTime)
						.collect(Collectors.toList()), null);

		memoryChartPanel.revalidate();
		memoryChartPanel.repaint();
	}

	public void clearCPUChart() {
		cpuChart.updatePieSeries("Em espera", 0);
		cpuChart.updatePieSeries("Computado", 0);
		cpuChart.setTitle("CPU");
	}

	private Process lastProcess;
	public void updateCPUChart() {
		if (cpu.getRunningProcess() != null) {
			lastProcess = cpu.getRunningProcess();
			cpuChart.setTitle("CPU - Processo " + cpu.getRunningProcess().getId());
			cpuChart.updatePieSeries("Em espera", cpu.getRunningProcess().getWaitingTime());
			cpuChart.updatePieSeries("Computado", cpu.getRunningProcess().getProcessTime());
		} else {
			// TODO: TESTANDO OUTROS METODOS APAGAR DEPOIS
			Process next = memory.getProcess(lastProcess.getId()+1) == null ? memory.getFirstProcess() : memory.getProcess(lastProcess.getId()+1);
			cpu.setRunningProcess(next, 2);
			updateMemoryChart();
		}

		cpuChartPanel.revalidate();
		cpuChartPanel.repaint();
	}

	public Memory getMemory() {
		return memory;
	}
}
