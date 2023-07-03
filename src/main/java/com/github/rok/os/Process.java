package com.github.rok.os;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Process {
	private int id;
	private double processTime; // Tempo de espera (varia de acordo com o algoritmo)
	private double waitingTime; // Tempo total que o processo leva

	public Process(int id, double waitingTime) {
		this.id = id;
		this.processTime = 0;
		this.waitingTime = waitingTime;
	}

	public int getId() {
		return id;
	}

	public double getProcessTime() {
		return processTime;
	}

	public void addProcessTime(double processTime) {
		this.processTime += processTime;
		this.waitingTime -= processTime;
	}

	public double getWaitingTime() {
		return waitingTime;
	}
}
