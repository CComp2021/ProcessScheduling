package com.github.rok.os;

import com.github.rok.interfaces.IController;
import com.github.rok.os.interfaces.ICPU;
import org.jetbrains.annotations.Nullable;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class CPU implements ICPU {

	private Process runningProcess;
	private Process originalProcess;

	private double processSpeed;

	private double initialTime;
	private double alreadyProcessed;
	private double timeProcessing;

	private final IController controller;
	private final CPUScaling scaling;

	private boolean paused = true;

	public CPU(IController controller) {
		new Thread(this::process).start();
		scaling = new CPUScaling();
		this.controller = controller;
	}

	private void process() {
		while (true) {
			try {
				Thread.sleep(10); // processo acelerado
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			scaling.tick(this);
			if (scaling.isScaling()) {
				controller.scalingTick(scaling.isToMemory(), scaling.getPercentageComplete(), STATE.SCALING);
				continue;
			}

			if (paused) continue;

			if (runningProcess == null) continue;
			runningProcess.addProcessTime(processSpeed / 100);
			if (timeProcessing > 0)
				timeProcessing -= 0.01;

			if (timeProcessing <= 0 || runningProcess.getWaitingTime() <= 0) {
				scaling.scale(runningProcess, initialTime - alreadyProcessed, true);
			}
			controller.updateTick();
		}
	}

	@Override
	public void endProcess() {
		if (runningProcess == null) return;
		originalProcess.consume(runningProcess);
		setRunningProcess(null);
		controller.sendToMemory(originalProcess);
	}

	public void setState(STATE state) {
		controller.scalingTick(false, 0, state);
	}

	public void addProcessToCPU(Process process, double timeProcessing) {
		if (runningProcess != null) return;
		scaling.scale(process, timeProcessing, false);
	}

	private void setRunningProcess(@Nullable Process runningProcess) {
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
		this.initialTime = timeProcessing;
	}

	@Nullable
	@Override
	public Process getRunningProcess() {
		return runningProcess;
	}


	@Override
	public double getTimeProcessing() {
		return timeProcessing;
	}

	public double getAlreadyProcessed() {
		return alreadyProcessed;
	}

	public void pause(boolean paused) {
		this.paused = paused;
	}

	@Override
	public boolean isRunning() {
		return runningProcess != null;
	}

	public void setScalingDelay(double speed) {
		scaling.setDelay(speed);
	}

	public void setProcessSpeed(double processSpeed) {
		this.processSpeed = processSpeed;
	}

	public double getProcessSpeed() {
		return processSpeed;
	}
	public double getInitialTime() {
		return initialTime;
	}

	public enum STATE {
		WAITING, SCALING, COMPUTING
	}
}
