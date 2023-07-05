package com.github.rok.panel;

import com.github.rok.Main;
import com.github.rok.MainPanel;
import com.github.rok.utils.Utils;

import javax.swing.*;
import java.awt.*;

/*
 * @author Rok, Pedro Lucas N M Machado created on 04/07/2023
 */
public class PropertiesControlFrame {

	private final Frame frame;
	private final MainPanel main;

	public PropertiesControlFrame(JPanel panel, Frame frame, MainPanel main, int gridy) {
		this.frame = frame;
		this.main = main;
		Utils.addSeparator(panel, (gridy += 1), main.getWindowWidth()); // ALGORITMO & TEMPO DE SIMULAÇÃO
		frame.addComponentToList("algorithm_title", Utils.addSubTitle(panel, (gridy += 1), "Algoritmo & Tempo de Simulação"));
		JPanel selectPanel = new JPanel();
		addAlgoritymTime(selectPanel);
		GridBagConstraints gbcViewList = new GridBagConstraints();
		gbcViewList.gridy = (gridy += 1);
		panel.add(selectPanel, gbcViewList);

		Utils.addSeparator(panel, (gridy += 1), main.getWindowWidth()); // TAMANHO E VELOCIDADE DO PROCESSO
		frame.addComponentToList("cpu_title", Utils.addSubTitle(panel, (gridy += 1), "Propriedades da CPU"));
		JPanel cpuSelectPanel = new JPanel();
		addCPUProperties(cpuSelectPanel);
		GridBagConstraints gbcCPU = new GridBagConstraints();
		gbcCPU.gridy = (gridy += 1);
		panel.add(cpuSelectPanel, gbcCPU);

		Utils.addSeparator(panel, (gridy += 1), main.getWindowWidth()); // TAMANHO E VELOCIDADE DO PROCESSO
		frame.addComponentToList("process_title", Utils.addSubTitle(panel, (gridy += 1), "Tamanho & Velocidade do Processo"));
		JPanel processSelectPanel = new JPanel();
		addProcessProperties(processSelectPanel);
		GridBagConstraints gbcProcess = new GridBagConstraints();
		gbcProcess.gridy = (gridy += 1);
		panel.add(processSelectPanel, gbcProcess);

		Utils.addSeparator(panel, (gridy += 1), main.getWindowWidth()); // TAMANHO E VELOCIDADE DO PROCESSO
		frame.addComponentToList("algorithym_properties", Utils.addSubTitle(panel, (gridy += 1), "Propriedades de Aloritmos"));
		JPanel algorithymSelectPanel = new JPanel();
		addAlgorithmProperties(algorithymSelectPanel);
		GridBagConstraints gbcAlgorithym = new GridBagConstraints();
		gbcAlgorithym.gridy = (gridy += 1);
		panel.add(algorithymSelectPanel, gbcAlgorithym);
	}

	private void addAlgoritymTime(JPanel panel) {
		JComboBox<String> algorithm = new JComboBox<>(Main.getAlgorithmsNameList().toArray(new String[0]));
		frame.addComponentToList("algorithm", algorithm);
		JLabel algorithmLabel = new JLabel("Algoritmo: ");
		frame.addComponentToList("algorithm_label", algorithmLabel);
		panel.add(algorithmLabel);
		panel.add(algorithm);

		JSpinner simulationMin = Utils.createSpinner(0, 0, 59, 1);

		JSpinner simulationSec = Utils.createSpinner(30, 0, 59, 1, e -> {
			JSpinner spinnerSec = (JSpinner) e.getComponent();
			if (!spinnerSec.isEnabled()) return;
			if (e.getWheelRotation() < 0) {
				if ((int) spinnerSec.getValue() == 59) {
					spinnerSec.setValue(0);
					simulationMin.setValue((int) simulationMin.getValue() + 1);
					return;
				}
				spinnerSec.setValue((int) spinnerSec.getValue() + 1);
				return;
			}
			if ((int) spinnerSec.getValue() == 0) {
				if ((int) simulationMin.getValue() == 0) return;
				spinnerSec.setValue(59);
				simulationMin.setValue((int) simulationMin.getValue() - 1);
				return;
			}
			spinnerSec.setValue((int) spinnerSec.getValue() - 1);

		});
		frame.addComponentToList("simulation_min", simulationMin);
		frame.addComponentToList("simulation_sec", simulationSec);

		panel.add(simulationMin);
		JLabel simulationLabelMin = new JLabel("Min.");
		frame.addComponentToList("simulation_label_min", simulationLabelMin);
		panel.add(simulationLabelMin);
		panel.add(simulationSec);
		JLabel simulationLabelSec = new JLabel("Sec.");
		frame.addComponentToList("simulation_label_sec", simulationLabelSec);
		panel.add(simulationLabelSec);
	}

	private void addCPUProperties(JPanel panel) {
		JSpinner cpuSpeed = Utils.createCustomSpinner(2, 0.1, 99.9, 0.1);
		frame.addComponentToList("cpu_speed", cpuSpeed);
		JSpinner scalingDelay = Utils.createCustomSpinner(0.2, 0.0, 99.9, 0.1);
		frame.addComponentToList("scaling_delay", scalingDelay);

		panel.add(frame.addComponentToList("cpu_speed_label", new JLabel("Velocidade: ")));
		panel.add(cpuSpeed);
		panel.add(frame.addComponentToList("cpu_speed_sec", new JLabel("sec.")));
		panel.add(new JLabel("     "));
		JComponent scalingDelayLabel = frame.addComponentToList("scaling_delay_label", new JLabel("Tempo de Escalonamento: "));
		scalingDelayLabel.setToolTipText("Tempo de espera total para o escalonamento de um processo (ida e volta)");
		panel.add(scalingDelayLabel);
		panel.add(scalingDelay);
		panel.add(frame.addComponentToList("scaling_speed_sec", new JLabel("sec.")));
	}

	private void addProcessProperties(JPanel panel) {
		JSpinner processDelay = Utils.createSpinner(3, 1, 99, 1);
		frame.addComponentToList("process_delay", processDelay);
		JSpinner processMin = Utils.createSpinner(1, 1, 98, 1);
		frame.addComponentToList("process_min", processMin);
		JSpinner processMax = Utils.createSpinner(10, 1, 99, 1);
		frame.addComponentToList("process_max", processMax);
		processDelay.addChangeListener(e -> {
			JSpinner spinner = (JSpinner) e.getSource();
			main.getMemory().setGenerationSpeed((int) spinner.getValue() * 100);
		});
		JSpinnerMinMax(processMin, processMax);


		panel.add(frame.addComponentToList("process_delay_label", new JLabel("Delay:")));
		panel.add(processDelay);
		panel.add(new JLabel("     "));
		panel.add(frame.addComponentToList("process_max_label", new JLabel("Max.")));
		panel.add(processMax);
		panel.add(processMin);
		panel.add(frame.addComponentToList("process_min_label", new JLabel("Min.")));
	}

	public void addAlgorithmProperties(JPanel panel) {
		JSpinner cpuRunningTime = Utils.createSpinner(5, 1, 99, 1);
		frame.addComponentToList("cpu_running_time", cpuRunningTime);
		JSpinner processMin = Utils.createSpinner(1, 1, 98, 1);
		frame.addComponentToList("prior_min", processMin);
		JSpinner processMax = Utils.createSpinner(10, 1, 99, 1);
		frame.addComponentToList("prior_max", processMax);
		JSpinnerMinMax(processMin, processMax);

		panel.add(frame.addComponentToList("cpu_running_time_label", new JLabel("Tempo na CPU:")));
		panel.add(cpuRunningTime);
		panel.add(new JLabel("        "));
		panel.add(frame.addComponentToList("prior_max_label", new JLabel("Prioridade:   Max.")));
		panel.add(processMax);
		panel.add(processMin);
		panel.add(frame.addComponentToList("prior_min_label", new JLabel("Min.")));
	}

	private void JSpinnerMinMax(JSpinner processMin, JSpinner processMax) {
		processMin.addChangeListener(e -> {
			JSpinner spinner = (JSpinner) e.getSource();
			if ((int) spinner.getValue() > (int) processMax.getValue())
				processMax.setValue(spinner.getValue());
		});
		processMax.addChangeListener(e -> {
			JSpinner spinner = (JSpinner) e.getSource();
			if ((int) spinner.getValue() < (int) processMin.getValue())
				processMin.setValue(spinner.getValue());
		});
	}
}
