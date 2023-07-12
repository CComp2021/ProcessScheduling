package com.github.rok.panel;

import com.github.rok.MainPanel;
import com.github.rok.os.Memory;
import com.github.rok.os.Process;
import com.github.rok.utils.Utils;
import org.knowm.xchart.*;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * @author Rok, Pedro Lucas N M Machado created on 04/07/2023
 */
public class ChartsFrame {

	private final MainPanel main;

	private final JPanel memoryPanel;
	private final JPanel cpuPanel;

	private final CategoryChart memoryChart;
	private final PieChart cpuChart;

	private final XChartPanel<CategoryChart> memoryChartPanel;
	private final XChartPanel<PieChart> cpuChartPanel;

	private final JProgressBar memoryBar;
	private final JProgressBar cpuBar;

	public ChartsFrame(MainPanel main, Memory memory) {
		this.main = main;
		memoryChart = new CategoryChartBuilder().width(main.getWindowWidth() / 2).height(main.getWindowHeight()).title("MEMORY").build();

		// Configurações do gráfico de memória
		Utils.addDefaultStyle(memoryChart);
		memoryChart.getStyler().setAxisTickLabelsColor(Color.decode("#ffffff"));
		memoryChart.getStyler().setPlotGridLinesColor(Color.decode("#393939"));
		memoryChart.getStyler().setYAxisMax(12.0);
		memoryChart.getStyler().setAvailableSpaceFill(.50);
		memoryChart.getStyler().setShowStackSum(true);
		memoryChart.getStyler().setAxisTicksLineVisible(false);
		memoryChart.getStyler().setAxisTicksMarksVisible(false);
		memoryChart.getStyler().setYAxisLabelAlignment(AxesChartStyler.TextAlignment.Right);
		memoryChart.getStyler().setAxisTickPadding(0);
		memoryChart.getStyler().setStacked(true);
		memoryChart.getStyler().setSeriesColors(new Color[]{Color.decode("#ff5555"), Color.decode("#0a84ff"), Color.decode("#ffcc00"),Color.decode("#888888"), Color.decode("#6d6d6d"), Color.decode("#c5c5c5")});
		memoryChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);

		List<Integer> processIds = memory.processList.stream()
				                           .map(Process::getId)
				                           .collect(Collectors.toList());
		memoryChart.addSeries("Prioridade", processIds,
				memory.processList.stream()
						.map(process -> (double) process.getPriority()/10)
						.collect(Collectors.toList()));
		memoryChart.addSeries("Em espera", processIds,
				memory.processList.stream()
						.map(Process::getWaitingTime)
						.collect(Collectors.toList())
		);
		memoryChart.addSeries("Computado", processIds,
				memory.processList.stream()
						.map(Process::getProcessedTime)
						.collect(Collectors.toList()));
		memoryChart.addSeries("gpriority", processIds, memory.processList.stream()
				.map(process -> {return 0;})
				.collect(Collectors.toList()));
		memoryChart.addSeries("gespera", processIds, memory.processList.stream()
				.map(process -> {return 0;})
				.collect(Collectors.toList()));
		memoryChart.addSeries("gcomputado", processIds, memory.processList.stream()
				.map(process -> {return 0;})
				.collect(Collectors.toList()));
		memoryChart.getSeriesMap().get("gespera").setShowInLegend(false);
		memoryChart.getSeriesMap().get("gcomputado").setShowInLegend(false);
		memoryChart.getSeriesMap().get("gpriority").setShowInLegend(false);


		cpuChart = new PieChartBuilder().width(main.getWindowWidth() / 2).height(main.getWindowHeight()).title("CPU").build();

		// Configurações do gráfico CPU
		Utils.addDefaultStyle(cpuChart);
		cpuChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
		cpuChart.getStyler().setSeriesColors(new Color[]{Color.decode("#0a84ff"), Color.decode("#ff9f0a"), Color.decode("#ffcc00")});
		cpuChart.getStyler().setLabelsFont(new Font("sans-serif", Font.BOLD, 15));

		cpuChart.addSeries("Em espera", 0);
		cpuChart.addSeries("Processando...", 0);
		cpuChart.addSeries("Computado", 0);

		// Cria o JFrame
		memoryChartPanel = new XChartPanel<>(memoryChart);
		cpuChartPanel = new XChartPanel<>(cpuChart);

		memoryPanel = new JPanel();
		memoryPanel.setSize(main.getWindowWidth(), main.getWindowHeight());
		memoryPanel.setLayout(new BoxLayout(memoryPanel, BoxLayout.Y_AXIS));
		memoryBar = Utils.createProgressBar("Próximo Processo...", main.getWindowWidth());
		memoryPanel.add(memoryBar);
		memoryPanel.add(memoryChartPanel);

		cpuPanel = new JPanel();
		cpuPanel.setSize(main.getWindowWidth(), main.getWindowHeight());
		cpuPanel.setLayout(new BoxLayout(cpuPanel, BoxLayout.Y_AXIS));
		cpuBar = Utils.createProgressBar("Computando...", main.getWindowWidth());
		cpuPanel.add(cpuBar);
		cpuPanel.add(cpuChartPanel);
	}

	public void clearCPUChart() {
		cpuChart.updatePieSeries("Em espera", 0);
		cpuChart.updatePieSeries("Processando...", 0);
		cpuChart.updatePieSeries("Computado", 0);
		cpuChart.setTitle("CPU");
	}

	public void updateMemoryChart() {
		List<Integer> processIds = main.getMemory().processList.stream()
				                           .map(Process::getId)
				                           .collect(Collectors.toList());
		memoryChart.updateCategorySeries("Em espera", processIds,
				main.getMemory().processList.stream()
						.map(process -> {
							if (process.isGray()) return 0;
							return process.getWaitingTime();
						})
						.collect(Collectors.toList()), null);

		memoryChart.updateCategorySeries("Computado", processIds,
				main.getMemory().processList.stream()
						.map(process -> {
							if (process.isGray()) return 0;
							return process.getProcessedTime();
						})
						.collect(Collectors.toList()), null);
		memoryChart.updateCategorySeries("Prioridade", processIds,
				main.getMemory().processList.stream()
						.map(process -> {
							if (process.isGray()) return 0;
							return (double) process.getPriority()/10;
						})
						.collect(Collectors.toList()), null);
		memoryChart.updateCategorySeries("gpriority", processIds, main.getMemory().processList.stream()
				.map(process -> {
					if (!process.isGray()) return 0;
					return (double) process.getPriority()/10;
				})
				.collect(Collectors.toList()), null);
		memoryChart.updateCategorySeries("gespera", processIds, main.getMemory().processList.stream()
				.map(process -> {
					if (!process.isGray()) return 0;
					return process.getWaitingTime();
				})
				.collect(Collectors.toList()), null);
		memoryChart.updateCategorySeries("gcomputado", processIds, main.getMemory().processList.stream()
				.map(process -> {
					if (!process.isGray()) return 0;
					return process.getProcessTime();
				})
				.collect(Collectors.toList()), null);

		memoryChartPanel.revalidate();
		memoryChartPanel.repaint();
	}

	public void updateCPUChart(boolean hasRunningProcess, boolean isRunning) {
		if (!isRunning) {
			main.getCpu().setRunningProcess(null, 0);
		}
		if (hasRunningProcess && isRunning) {
			cpuChart.setTitle("CPU - Processo " + main.getCpu().getRunningProcess().getId());
			cpuChart.updatePieSeries("Em espera", main.getCpu().getRunningProcess().getWaitingTime());
			cpuChart.updatePieSeries("Processando...", main.getCpu().getRunningProcess().getProcessedTime() - main.getCpu().getAlreadyProcessed());
			cpuChart.updatePieSeries("Computado", main.getCpu().getAlreadyProcessed());
		}
		cpuChartPanel.revalidate();
		cpuChartPanel.repaint();
	}

	public void updateCPUChartColor(boolean isRunning) {
		for (Map.Entry<String, PieSeries> entry : cpuChart.getSeriesMap().entrySet()) {
			switch (entry.getKey()) {
				case "Em espera":
					entry.getValue().setFillColor(isRunning ? Color.decode("#0a84ff") : Color.decode("#6d6d6d"));
					break;
				case "Processando...":
					entry.getValue().setFillColor(isRunning ? Color.decode("#ff9f0a") : Color.decode("#ababab"));
					break;
				case "Computado":
					entry.getValue().setFillColor(isRunning ? Color.decode("#ffcc00") : Color.decode("#c5c5c5"));
					break;
			}

		}
		cpuChartPanel.revalidate();
		cpuChartPanel.repaint();
	}

	public void updateMemoryBar(int value) {
		memoryBar.setValue(value);
	}

	public void updateCpuBar(int value) {
		cpuBar.setValue(value);
	}

	public void updateCpuBar(int value, String text) {
		cpuBar.setValue(value);
		cpuBar.setString(text);
	}

	public XChartPanel<CategoryChart> getMemoryChartPanel() {
		return memoryChartPanel;
	}


	public CategoryChart getMemoryChart() {
		return memoryChart;
	}

	public JPanel getMemoryPanel() {
		return memoryPanel;
	}

	public JPanel getCpuPanel() {
		return cpuPanel;
	}

	public PieChart getCpuChart() {
		return cpuChart;
	}
}
