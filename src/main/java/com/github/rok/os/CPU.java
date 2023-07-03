package com.github.rok.os;

import com.github.rok.MainPanel;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class CPU {

	private Process runningProcess;
	private long processSpeed; // processo divido por 10 ( default 2 = 0.2)
	private double timeProcessing;
	private Consumer<Process> consumer;

	private boolean paused = false;

	public CPU( Consumer<Process> consumer) {
		this.processSpeed = 2;
		new Thread(this::process).start();
		this.consumer = consumer;
	}

	private void process() {
		while (true) {
			try {
				Thread.sleep(processSpeed * 100); // processo acelerado
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (paused) continue;

			if (runningProcess == null) continue;
			runningProcess.addProcessTime((double) processSpeed /10);
			timeProcessing -= (double) processSpeed/10;

			if (timeProcessing <= 0 || runningProcess.getWaitingTime() <= 0) {
				endProcess();
			}
			consumer.accept(null);
		}
	}

	private void endProcess() {
		consumer.accept(runningProcess);
		setRunningProcess(null);
	}

	public void setRunningProcess(@Nullable Process runningProcess) {
		this.runningProcess = runningProcess;
		this.timeProcessing = runningProcess != null ? runningProcess.getProcessTime() : 0;
	}

	public void setRunningProcess(Process runningProcess, double timeProcessing) {
		this.runningProcess = runningProcess;
		this.timeProcessing = timeProcessing;
	}

	@Nullable
	public Process getRunningProcess() {
		return runningProcess;
	}

	public double getTimeProcessing() {
		return timeProcessing;
	}

	public void pause() {
		paused = !paused;
	}

	public boolean isRunning() {
		return runningProcess != null;
	}
}
