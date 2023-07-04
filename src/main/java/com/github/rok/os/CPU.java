package com.github.rok.os;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class CPU {

	private Process runningProcess;
	private Process originalProcess;
	private double alreadyProcessed;
	private long processSpeed; // processo divido por 10 ( default 2 = 0.2)
	private double timeProcessing;
	private Consumer<Process> consumer;

	private boolean paused = true;

	public CPU(Consumer<Process> consumer) {
		this.processSpeed = 2;
		new Thread(this::process).start();
		this.consumer = consumer;
	}

	private void process() {
		while (true) {
			try {
				Thread.sleep(10); // processo acelerado
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (paused) continue;

			if (runningProcess == null) continue;
			runningProcess.addProcessTime((double) processSpeed /100);
			timeProcessing -= (double) processSpeed/100;

			if (timeProcessing <= 0 || runningProcess.getWaitingTime() <= 0) {
				endProcess();
			}
			consumer.accept(null);
		}
	}

	private void endProcess() {
		originalProcess.consume(runningProcess);
		consumer.accept(originalProcess);
		setRunningProcess(null);
	}

	public void setRunningProcess(@Nullable Process runningProcess) {
		double processTime = runningProcess == null ? 0 : runningProcess.getProcessTime();
		setRunningProcess(runningProcess, processTime);
	}

	/**
	 * @param runningProcess Processo que a ser executado
	 * @param timeProcessing Tempo processando (em segundos)
	 */
	public void setRunningProcess(Process runningProcess, double timeProcessing) {
		if (runningProcess == null) {
			this.runningProcess = null;
			this.timeProcessing = 0;
			this.alreadyProcessed = 0;
			return;
		}
		originalProcess = runningProcess;
		this.runningProcess = runningProcess.clone();
		this.alreadyProcessed = runningProcess.getProcessTime();
		this.timeProcessing = timeProcessing;
	}

	@Nullable
	public Process getRunningProcess() {
		return runningProcess;
	}

	public double getTimeProcessing() {
		return timeProcessing;
	}

	public double getAlreadyProcessed() {
		return alreadyProcessed;
	}

	public boolean pause() {
		paused = !paused;
		return paused;
	}

	public boolean isRunning() {
		return runningProcess != null;
	}

	public void setProcessSpeed(long processSpeed) {
		this.processSpeed = processSpeed;
	}

	public long getProcessSpeed() {
		return processSpeed;
	}
}
