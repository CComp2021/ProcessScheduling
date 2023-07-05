package com.github.rok;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;

/*
 * @Author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Main {

	private static MainPanel mainPanel;

	public static void main(String[] args) {
		// TODO: Reorganize this code
		try {
			UIManager.setLookAndFeel(new FlatMacDarkLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
		mainPanel = new MainPanel();
	}

	public static MainPanel getPanel() {
		return mainPanel;
	}
}
