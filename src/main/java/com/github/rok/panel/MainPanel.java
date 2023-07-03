package com.github.rok.panel;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.github.rok.Utils;
import com.github.rok.os.CPU;
import com.github.rok.os.Memory;
import com.github.rok.os.Process;
import org.knowm.xchart.*;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
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

	private static final int WINDOW_WIDTH = 900;
	private static final int WINDOW_HEIGHT = 500;

	private Memory memory;
	private CPU cpu;

	private CategoryChart memoryChart;
	private PieChart cpuChart;

	private XChartPanel<CategoryChart> memoryChartPanel;
	private XChartPanel<PieChart> cpuChartPanel;

	public MainPanel() {

		// Colocando o look and feel
		try {
			UIManager.setLookAndFeel( new FlatMacDarkLaf() );
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}

		//Cria os modulos do sistema
		this.memory = new Memory();
		this.cpu = new CPU( process -> {
			if (process == null) {
				updateCPUChart();
				return;
			}
			if (process.getWaitingTime() <= 0)
				getMemory().removeProcess(process);

			if (getMemory().isEmpty())
				clearCPUChart();
		});

		memoryChart = new CategoryChartBuilder().width(WINDOW_WIDTH/2).height(WINDOW_HEIGHT).title("MEMÓRIA").build();

		// Configurações do gráfico de memória
		Utils.addDefaultStyle(memoryChart);
		memoryChart.getStyler().setAxisTickLabelsColor(Color.decode("#ffffff"));
		memoryChart.getStyler().setPlotGridLinesColor(Color.decode("#393939"));
		memoryChart.getStyler().setYAxisMax(10.0);
		memoryChart.getStyler().setAvailableSpaceFill(.50);
		memoryChart.getStyler().setShowStackSum(true);
		memoryChart.getStyler().setAxisTicksLineVisible(false);
		memoryChart.getStyler().setAxisTicksMarksVisible(false);
		memoryChart.getStyler().setYAxisLabelAlignment(AxesChartStyler.TextAlignment.Right);
		memoryChart.getStyler().setAxisTickPadding(0);
		memoryChart.getStyler().setStacked(true);
		memoryChart.getStyler().setSeriesColors(new Color[]{Color.decode("#0a84ff"), Color.decode("#ffcc00")});
		memoryChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);

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

		cpuChart = new PieChartBuilder().width(WINDOW_WIDTH/2).height(WINDOW_HEIGHT).title("CPU").build();

		// Configurações do gráfico CPU
		Utils.addDefaultStyle(cpuChart);
		cpuChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
		cpuChart.getStyler().setSeriesColors(new Color[]{Color.decode("#0a84ff"), Color.decode("#ff9f0a"),Color.decode("#ffcc00")});
		cpuChart.getStyler().setLabelsFont(new Font("sans-serif", Font.BOLD, 15));

		cpuChart.addSeries("Em espera", 0);
		cpuChart.addSeries("Processando...", 0);
		cpuChart.addSeries("Computado", 0);


		// Cria o JFrame
		memoryChartPanel = new XChartPanel<>(memoryChart);
		cpuChartPanel = new XChartPanel<>(cpuChart);
		JFrame frame = new Frame(this, memoryChartPanel, cpuChartPanel).get();


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
		cpuChart.updatePieSeries("Processando...", 0);
		cpuChart.updatePieSeries("Computado", 0);
		cpuChart.setTitle("CPU");
	}

	private Process lastProcess;
	public void updateCPUChart() {
		if (cpu.getRunningProcess() != null) {
			lastProcess = cpu.getRunningProcess();
			cpuChart.setTitle("CPU - Processo " + cpu.getRunningProcess().getId());
			cpuChart.updatePieSeries("Em espera", cpu.getRunningProcess().getWaitingTime());
			cpuChart.updatePieSeries("Processando...", cpu.getRunningProcess().getProcessTime() - cpu.getAlreadyProcessed());
			cpuChart.updatePieSeries("Computado", cpu.getAlreadyProcessed());
		} else {
			// TODO: TESTANDO OUTROS METODOS APAGAR DEPOIS

			int i = lastProcess.getId() + 1;
			while (memory.getProcess(i) == null && i < lastProcess.getId()+9) {
				i++;
			}
			Process next = memory.getProcess(i) == null ? memory.getFirstProcess() : memory.getProcess(i);

			cpu.setRunningProcess(next, 2);
			updateMemoryChart();
		}

		cpuChartPanel.revalidate();
		cpuChartPanel.repaint();
	}

	public CPU getCpu() {
		return cpu;
	}

	public Memory getMemory() {
		return memory;
	}
}
