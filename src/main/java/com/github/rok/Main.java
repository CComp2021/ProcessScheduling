package com.github.rok;

import com.github.rok.panel.MainPanel;

/*
 * @Author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Main {

	private static MainPanel mainPanel;

	public static void main(String[] args) throws Exception {
		mainPanel = new MainPanel();
	}

	public static MainPanel getPanel() {
		return mainPanel;
	}
}
