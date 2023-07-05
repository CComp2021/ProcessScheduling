package com.github.rok.panel;

import com.github.rok.MainPanel;

import javax.swing.*;
import java.awt.*;

/*
 * @author Rok, Pedro Lucas N M Machado created on 04/07/2023
 */
public class ButtonsControlFrame {

	public ButtonsControlFrame(JPanel panel, Frame mainFrame, MainPanel main, int gridy) {
		// Adiciona o botão de atualização
		JButton updateButton = new JButton("PAUSE");
		updateButton.setPreferredSize(new Dimension(140, 50));
		updateButton.setVisible(false);
		updateButton.setBackground(Color.decode("#996939"));
		updateButton.addActionListener(e -> {
			pauseButton(main, updateButton, updateButton.isSelected());

		});
		JButton startSim = new JButton("START SIMULATION");
		startSim.setBackground(Color.decode("#399939"));
		startSim.setPreferredSize(new Dimension(140, 50));

		JButton plus = new JButton("+");
		plus.setFont(new Font("Arial", Font.PLAIN, 20));
		plus.setPreferredSize(new Dimension(50, 50));
		plus.setToolTipText("Aumenta a velocidade de geração de processos");
		plus.setVisible(false);
		plus.addActionListener(e -> addValueToDelay(-1, main, mainFrame));

		JButton minus = new JButton("-");
		minus.setFont(new Font("Arial", Font.PLAIN, 20));
		minus.setPreferredSize(new Dimension(50, 50));
		minus.setToolTipText("Diminui a velocidade de geração de processos");
		minus.setVisible(false);
		minus.addActionListener(e -> addValueToDelay(1, main, mainFrame));

		startSim.addActionListener(e ->

		{
			if (!main.isRunning()) {
				startSim.setText("STOP");
				startSim.setBackground(Color.decode("#993939"));
				setRunning(true, mainFrame, main, updateButton, plus, minus);
				return;
			}
			setRunning(false, mainFrame, main, updateButton, plus, minus);
			startSim.setBackground(Color.decode("#399939"));
			startSim.setText("START SIMULATION");
		});

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(plus);
		buttonsPanel.add(updateButton);
		buttonsPanel.add(startSim);
		buttonsPanel.add(minus);
		updateButton.setSelected(false);

		GridBagConstraints gbcButtons = new GridBagConstraints();
		gbcButtons.gridy = (gridy+=1);
		gbcButtons.insets = new Insets(10, 10, 10, 10);
		panel.add(buttonsPanel, gbcButtons);
	}

	private void addValueToDelay(int val, MainPanel main, Frame mainFrame) {
		JSpinner processDelay = ((JSpinner) mainFrame.getComponent("process_delay"));
		double generationSpeed = main.getMemory().getGenerationSpeed();
		if ((generationSpeed / 100 > 1 && val == -1) || (generationSpeed / 100 < 99 && val == 1)) {
			processDelay.setValue((int) processDelay.getValue() + val);
			main.getMemory().setGenerationSpeed((int) processDelay.getValue() * 100);
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

	public void setRunning(boolean running, Frame frame, MainPanel main, JButton pause, JButton plus, JButton minus) {
		pause.setVisible(running);
		plus.setVisible(running);
		minus.setVisible(running);

		pauseButton(main, pause, running);
		if (running) {
			main.updateProperties();
			frame.disableElements();
		} else {
			frame.enableElements();
		}
		main.setRunning(running);
	}
}
