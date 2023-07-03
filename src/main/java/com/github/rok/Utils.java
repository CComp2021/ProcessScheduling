package com.github.rok;

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

}
