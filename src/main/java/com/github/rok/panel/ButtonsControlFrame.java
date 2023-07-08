package com.github.rok.panel;

import com.github.rok.MainPanel;
import com.github.rok.utils.TimeUtils;

import javax.swing.*;
import java.awt.*;

/*
 * @author Rok, Pedro Lucas N M Machado created on 04/07/2023
 */
public class ButtonsControlFrame {

	private MainPanel main;
	private JButton pause;
	private JButton startSim;
	private JButton plus;
	private JButton minus;

	private JLabel timeLabel;

	public ButtonsControlFrame(JPanel panel, Frame mainFrame, MainPanel main, int gridy) {
		// Adiciona o botão de atualização
		this.main = main;
		pause = new JButton("PAUSE");
		pause.setPreferredSize(new Dimension(115, 50));
		pause.setVisible(false);
		pause.setBackground(Color.decode("#996939"));
		pause.addActionListener(e -> {
			pauseButton(main, pause, pause.isSelected());
		});
		startSim = new JButton("START SIMULATION");
		startSim.setBackground(Color.decode("#399939"));
		startSim.setPreferredSize(new Dimension(140, 50));

		JPanel timePanel = new JPanel();
		timeLabel = new JLabel("0");
		timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		timeLabel.setPreferredSize(new Dimension(50, 50));
		timeLabel.setToolTipText("Tempo de simulação");
		timeLabel.setVisible(false);
		timePanel.add(timeLabel);

		plus = new JButton("+");
		plus.setFont(new Font("Arial", Font.PLAIN, 20));
		plus.setPreferredSize(new Dimension(50, 50));
		plus.setToolTipText("Aumenta a velocidade de geração de processos");
		plus.setVisible(false);
		plus.addActionListener(e -> addValueToDelay(-1, main, mainFrame));

		minus = new JButton("-");
		minus.setFont(new Font("Arial", Font.PLAIN, 20));
		minus.setPreferredSize(new Dimension(50, 50));
		minus.setToolTipText("Diminui a velocidade de geração de processos");
		minus.setVisible(false);
		minus.addActionListener(e -> addValueToDelay(1, main, mainFrame));

		startSim.addActionListener(e -> {
			flipSimulation(mainFrame, main.isRunning());
		});

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(plus);
		buttonsPanel.add(pause);
		buttonsPanel.add(timeLabel);
		buttonsPanel.add(startSim);
		buttonsPanel.add(minus);
		pause.setSelected(false);

		GridBagConstraints gbcButtons = new GridBagConstraints();
		gbcButtons.gridy = (gridy += 1);
		gbcButtons.insets = new Insets(10, 10, 10, 10);
		panel.add(buttonsPanel, gbcButtons);
	}

	private void addValueToDelay(int val, MainPanel main, Frame mainFrame) {
		JSpinner processDelay = ((JSpinner) mainFrame.getComponent("process_delay"));
		double generationSpeed = main.getMemory().getGenerationSpeed();
		if ((generationSpeed / 100 > 1 && val == -1) || (generationSpeed / 100 < 99 && val == 1)) {
			processDelay.setValue((((double)processDelay.getValue()) + val));
			main.getMemory().setGenerationSpeed((int) (((double)processDelay.getValue()) * 100));
		}
	}

	public void pauseButton(MainPanel main, JButton updateButton, boolean pause) {
		if (pause) {
			main.pause(false);
			updateButton.setText("PAUSE");
			updateButton.setSelected(false);
			updateButton.setBackground(Color.decode("#996939"));
			return;
		}
		main.pause(true);
		updateButton.setText("RESUME");
		updateButton.setBackground(Color.decode("#396939"));
		updateButton.setSelected(true);
	}

	public void flipSimulation(Frame mainFrame, boolean running) {
		if (!running) {
			startSim.setText("STOP");
			startSim.setBackground(Color.decode("#993939"));
			startSim.setPreferredSize(new Dimension(115, 50));
			setRunning(true, mainFrame);
			return;
		}
		setRunning(false, mainFrame);
		startSim.setPreferredSize(new Dimension(140, 50));
		startSim.setBackground(Color.decode("#399939"));
		startSim.setText("START SIMULATION");
	}
	public void setRunning(boolean running, Frame frame) {
		pause.setVisible(running);
		plus.setVisible(running);
		minus.setVisible(running);
		timeLabel.setVisible(running);

		pauseButton(main, pause, running);
		if (running) {
			main.updateProperties();
			frame.disableElements();
		} else {
			frame.enableElements();
		}
		main.setRunning(running);
	}

	public void updateTime(Long time) {
		timeLabel.setText(TimeUtils.getTime(time));
	}
}
