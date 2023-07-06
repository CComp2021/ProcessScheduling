package com.github.rok.panel;

import com.github.rok.MainPanel;
import com.github.rok.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Frame {
	private JFrame frame;

	private final HashMap<String, JComponent> components = new HashMap<>();

	private JProgressBar centerBar;
	private ButtonsControlFrame buttonsControlFrame;

	public Frame(MainPanel main, ChartsFrame chartsFrame) {
		// Colocando o look and feel

		frame = new JFrame("Escalonamento De Processos");
		frame.setMinimumSize(new Dimension(1372, main.getWindowHeight()));
		frame.setResizable(true);

		frame.setLayout(new java.awt.GridLayout(1, 3));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Painel para posicionar os elementos
		JPanel panel = new JPanel(new GridBagLayout());

		JLabel label = new JLabel("CONTROLES");
		label.setFont(new Font("sans-serif", Font.PLAIN, 30));
		JPanel titlePanel = new JPanel();
		titlePanel.add(label);
		titlePanel.setBackground(Color.decode("#393939"));
		titlePanel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		titlePanel.setPreferredSize(new Dimension(main.getWindowWidth() / 2, 60));

		int gridy = 0;
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = gridy;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(titlePanel, gbc);

		// CENTRAL BAR
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setSize(main.getWindowWidth(), main.getWindowHeight());
		centerBar = Utils.createProgressBar("Aperte o botão para começar a simulação.", main.getWindowWidth() * 2);
		centerPanel.add(centerBar);
		addComponentToList("center_bar", centerBar);
		GridBagConstraints gbcBar = new GridBagConstraints();
		gbcBar.gridy = (gridy += 1);
		gbcBar.fill = GridBagConstraints.HORIZONTAL;
		panel.add(centerPanel, gbcBar);

		JPanel jPanel = Utils.addSeparator(panel, gridy + 1, main.getWindowWidth());
		jPanel.setBackground(Color.lightGray);

		buttonsControlFrame = new ButtonsControlFrame(panel, this, main, 2);// Last Gridy enter as variable
		new PropertiesControlFrame(panel, this, main, 3);

		Utils.addSeparator(panel, 16, main.getWindowWidth());
		// Preenche o painel com os elementos
		gbc = new GridBagConstraints();
		gbc.gridy = 17;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(new JPanel(), gbc);

		panel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		frame.add(chartsFrame.getMemoryPanel());
		frame.add(panel);
		frame.add(chartsFrame.getCpuPanel());

		frame.pack();
		frame.setVisible(true);
	}

	protected JComponent addComponentToList(String key, JComponent component) {
		components.put(key, component);
		return component;
	}

	public int getComponentInt(String key) {
		return (int) ((JSpinner) components.get(key)).getValue();
	}

	public double getComponentDouble(String key) {
		return (double) ((JSpinner) components.get(key)).getValue();
	}
	public JComponent getComponent(String key) {
		return components.get(key);
	}

	public void disableElements() {
		components.forEach((key, component) -> {
			component.setEnabled(false);
		});
	}

	public void updateCenterBar(int value, String text) {
		centerBar.setValue(value);
		centerBar.setString(text);
	}

	public void removeControlButtons() {
		buttonsControlFrame.flipSimulation(this, true);
	}

	public void enableElements() {
		components.forEach((key, component) -> component.setEnabled(true));
	}

	public void updateTime(long time) {
		buttonsControlFrame.updateTime(time);
	}


}
