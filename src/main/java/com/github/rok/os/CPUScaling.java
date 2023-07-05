package com.github.rok.os;

import com.github.rok.utils.Utils;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
public class CPUScaling {

	private double DELAY = 0.2;
	private double clock;
	private boolean scaling = false;
	private boolean toMemory = false;

	private Process waitingProcess;
	private double processTime;

	public CPUScaling() {
		clock = DELAY;
	}
	protected void tick(CPU cpu) {
		if (!scaling) return;
		if (clock > 0) {
			clock -= 0.01;
			return;
		}
		if (toMemory) {
			cpu.endProcess();
			cpu.setState(CPU.STATE.WAITING);
		} else {
			cpu.setRunningProcess(waitingProcess, processTime);
			cpu.setState(CPU.STATE.COMPUTING);
		}
		scaling = false;
	}

	protected void scale(Process process, double timeProcessing, boolean toMemory) {
		if (process == null) return;
		if (scaling) return;
		waitingProcess = process;
		processTime = timeProcessing;
		clock = DELAY/2;
		scaling = true;
		this.toMemory = toMemory;
	}

	public boolean isScaling() {
		return scaling;
	}
	public boolean isToMemory() {
		return toMemory;
	}

	public double getPercentageComplete() {
		return Utils.getPercentageToValue(DELAY, clock);
	}

	public void setDelay(double DELAY) {
		this.DELAY = DELAY;
	}
}
