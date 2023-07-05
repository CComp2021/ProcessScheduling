package com.github.rok.utils;

import com.formdev.flatlaf.ui.FlatSpinnerUI;
import org.knowm.xchart.internal.chartpart.Chart;

import javax.swing.*;
import javax.swing.plaf.SpinnerUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.text.DecimalFormat;
import java.util.function.Consumer;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Utils {

	public static double[][] getSineData(double phase) {
		double[] xData = new double[100];
		double[] yData = new double[100];
		for (int i = 0; i < xData.length; i++) {
			double radians = phase + (2 * Math.PI / xData.length * i);
			xData[i] = radians;
			yData[i] = Math.sin(radians);
		}
		return new double[][] { xData, yData };
	}

	public static void addDefaultStyle(Chart chart) {
		chart.getStyler().setChartBackgroundColor(Color.decode("#1e1e1e"));
		chart.getStyler().setPlotBackgroundColor(Color.decode("#191919"));
		chart.getStyler().setLegendBackgroundColor(Color.decode("#393939"));
		chart.getStyler().setChartTitleFont(new Font("sans-serif", Font.PLAIN, 30));
		chart.getStyler().setChartTitleBoxVisible(true);
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setChartTitleBoxBackgroundColor(Color.decode("#393939"));
		chart.getStyler().setLegendFont(new Font("sans-serif", Font.PLAIN, 12));
		chart.getStyler().setChartFontColor(Color.decode("#ffffff"));
		chart.getStyler().setSeriesColors(new Color[]{Color.decode("#0a84ff"), Color.decode("#ffcc00"), Color.decode("#ff9f0a")});
	}

	public static double generateRandomNumber(double min, double max) {
		double randomNumber = min + (max - min) * Math.random();
		return randomNumber;
	}

	public static JLabel addSubTitle(JPanel panel, int y, String title) {
		JLabel subtitle = new JLabel(title);
		subtitle.setFont(new Font("sans-serif", Font.PLAIN, 16));
		JPanel viewListPanel = new JPanel();
		viewListPanel.add(subtitle);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = y;
		panel.add(viewListPanel, gbc);
		return subtitle;
	}

	public static JProgressBar createProgressBar(String text, int width) {
		JProgressBar bar = new JProgressBar(0, 100);
		bar.setStringPainted(true);
		bar.setString(text);
		bar.setSize(width, 10);
		return bar;
	}

	public static JSpinner createSpinner(int initial, int min, int max, int step) {
		JSpinner spin = createSpinner(initial, min, max, step, e -> {
			JSpinner spinner = (JSpinner) e.getComponent();
			if (!spinner.isEnabled()) return;
			if (e.getWheelRotation() < 0) {
				if ((int) spinner.getValue() == max) {
					spinner.setValue(min);
					return;
				}
				spinner.setValue((int) spinner.getValue() + 1);
			} else {
				if ((int) spinner.getValue() == min) {
					spinner.setValue(max);
					return;
				}
				spinner.setValue((int) spinner.getValue() - 1);
			}
		});
		return spin;
	}

	public static JSpinner createSpinner(int initial, int min, int max, int step, Consumer<MouseWheelEvent> action) {
		SpinnerNumberModel model = new SpinnerNumberModel(initial, min, max, step);
		JSpinner spinner = new JSpinner(model);
		spinner.setToolTipText("Use o Scroll");
		spinner.setPreferredSize(new Dimension(50, 30));
		spinner.addMouseWheelListener(action::accept);
		spinner.setEditor(new JSpinner.DefaultEditor(spinner));
		return spinner;
	}

	public static JSpinner createCustomSpinner(double initial, double min, double max, double step) {
		// Cria um NumberFormatter para formatar o texto do JSpinner
		DecimalFormat decimalFormat = new DecimalFormat("#0.0");
		NumberFormatter formatter = new NumberFormatter(decimalFormat);
		formatter.setValueClass(Double.class);
		formatter.setMinimum(0.0);
		formatter.setMaximum(100.0);
		formatter.setAllowsInvalid(false);

		// Cria um SpinnerNumberModel com o NumberFormatter personalizado
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(initial, min, max, step);
		JSpinner spinner = new JSpinner(spinnerModel);
		spinner.setPreferredSize(new Dimension(60, 30));
		spinner.setToolTipText("Use o Scroll");
		spinner.setEditor(new JSpinner.DefaultEditor(spinner));
		spinner.addMouseWheelListener(e->{
			if (!spinner.isEnabled()) return;
			double val = (double) spinner.getValue();
			if (e.getWheelRotation() < 0) {
				if (val >= max) {
					spinner.setValue(min);
					return;
				}
				spinner.setValue(val + 0.1);
			} else {
				if ((int) val <= min) {
					spinner.setValue(max);
					return;
				}
				spinner.setValue(val - 0.1);
			}
		});

		return spinner;
	}

	public static double getPercentageToValue(double initialVal, double variableVal) {
		return Math.max(0, Math.min(100, (initialVal- variableVal) * (100.0 / initialVal)));
	}

	public static JPanel addSeparator(JPanel panel, int y, int width) {
		JPanel jSeparator = new JPanel();
		jSeparator.setBackground(Color.decode("#393939"));
		jSeparator.setPreferredSize(new Dimension(width/2, 1));
		GridBagConstraints gb = new GridBagConstraints();
		gb.gridy = y;
		panel.add(jSeparator, gb);
		return jSeparator;
	}
}
