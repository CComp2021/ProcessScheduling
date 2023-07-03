package com.github.rok;

import org.knowm.xchart.internal.chartpart.Chart;

import java.awt.*;

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
}
