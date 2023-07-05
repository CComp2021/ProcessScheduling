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
	public Process(int id, double waitingTime, double processTime) {
		this.id = id;
		this.processTime = processTime;
		this.waitingTime = waitingTime;
	}

	public int getId() {
		return id;
	}

	public double getProcessTime() {
		return processTime;
	}

	protected void addProcessTime(double processTime) {
		this.processTime += processTime;
		this.waitingTime -= processTime;
	}

	public double getWaitingTime() {
		return waitingTime;
	}

	// Apenas para não ocorrer bug de desincronização na visualização, mas idealmente é como se fosse o mesmo processo que está na memória
	@Override
	protected Process clone(){
		return new Process(id, waitingTime, processTime);
	}

	protected void consume(Process process){
		this.id = process.id;
		this.processTime = process.processTime;
		this.waitingTime = process.waitingTime;
	}
}
