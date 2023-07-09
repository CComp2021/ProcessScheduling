package com.github.rok;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.github.rok.algorithm.AlgorithmFIFO;

import com.github.rok.algorithm.AlgorithmFairShare;
import com.github.rok.algorithm.AlgorithmShortestJobFirst;
import com.github.rok.algorithm.AlgorithmShortestremainingtime;

import com.github.rok.interfaces.AlgorithmInterface;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

/*
 * @Author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Main {

	private static MainPanel mainPanel;
	private static final HashMap<String, AlgorithmInterface> algorithms = new HashMap<>();

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatMacDarkLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}

		mainPanel = new MainPanel();
		importAlgorithms();
	}

	public static void importAlgorithms() {
		// Inserir algoritmos aqui

		algorithms.put("FIFO", new AlgorithmFIFO(mainPanel));
		algorithms.put("SJF", new AlgorithmShortestJobFirst(mainPanel));
		algorithms.put("SRT", new AlgorithmShortestremainingtime(mainPanel));
		algorithms.put("Fair-Share", new AlgorithmFairShare(mainPanel));
		algorithms.put("Lottery", new AlgorithmFairShare(mainPanel));
		mainPanel.importAlgorithms();
	}

	public static AlgorithmInterface getAlgorithm(String name) {
		return algorithms.get(name);
	}

	public static List<String> getAlgorithmsNameList() {
		return List.copyOf(algorithms.keySet());
	}

	public static MainPanel getPanel() {
		return mainPanel;
	}
}
