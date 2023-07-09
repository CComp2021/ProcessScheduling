package com.github.rok.os;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Process {
	private int id;
	private double processTime; // Tempo de espera (varia de acordo com o algoritmo)
	private double waitingTime; // Tempo total que o processo leva
	private double timeScaling;

	// Informações para os relatórios
	private final long arrivalTime;
	private long timeOnCPU;

	private int priority; // Nova propriedade para a prioridade do processo

	public Process(int id, double waitingTime, long arrivalTime) {
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.processTime = 0;
		this.waitingTime = waitingTime;
		this.timeScaling = 0;

	}

	public Process(int id, double waitingTime, double processTime, long arrivalTime, double timeScaling, long timeOnCPU) {
		this.id = id;
		this.processTime = processTime;
		this.waitingTime = waitingTime;
		this.arrivalTime = arrivalTime;
		this.timeScaling = timeScaling;
		this.timeOnCPU = timeOnCPU;
	}

	public Process(int id, double waitingTime, long arrivalTime, int priority) {
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.processTime = 0;
		this.waitingTime = waitingTime;
		this.timeScaling = 0;

		this.priority = priority;
	}

	public int getId() {
		return id;
	}

	public double getProcessedTime() {
		return processTime;
	}

	protected void addProcessTime(double processTime) {
		this.processTime += processTime;
		this.waitingTime -= processTime;
	}

	protected void addTimeOnCPU(long timeOnCPU) {
		this.timeOnCPU += timeOnCPU;
	}

	protected void addScalingTime(double timeScaling) {
		this.timeScaling += timeScaling;
	}

	public double getWaitingTime() {
		return waitingTime;
	}

	public double getTimeScaling() {
		return timeScaling;
	}

	public long getArrivalTime() {
		return arrivalTime;
	}

	public long getTimeOnCPU() {
		return timeOnCPU;
	}

	// Apenas para não ocorrer bug de desincronização na visualização, mas idealmente é como se fosse o mesmo processo que está na memória
	@Override
	protected Process clone() {
		return new Process(id, waitingTime, processTime, arrivalTime, timeScaling, timeOnCPU);
	}

	protected void consume(Process process) {
		this.id = process.id;
		this.timeScaling = process.timeScaling;
		this.processTime = process.processTime;
		this.waitingTime = process.waitingTime;
		this.timeOnCPU = process.timeOnCPU;
	}

	public int getPriority() {
		return priority;
	}
}
