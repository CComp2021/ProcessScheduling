package com.github.rok.os;

import com.github.rok.MainPanel;
import org.jetbrains.annotations.Nullable;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class CPU {

	private Process runningProcess;
	private long processSpeed; // processo divido por 10 ( default 2 = 0.2)
	private double timeProcessing;
	MainPanel panel;

	public CPU(MainPanel panel) {
		this.panel = panel;
		this.processSpeed = 2;
		new Thread(this::process).start();

	}

	private void process() {
		while (true) {
			try {
				Thread.sleep(processSpeed * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (runningProcess == null) continue;
			runningProcess.addProcessTime((double) processSpeed /10);
			timeProcessing -= (double) processSpeed/10;

			if (timeProcessing <= 0 || runningProcess.getWaitingTime() <= 0) {
				endProcess();
			}

			panel.updateCPUChart();
		}
	}

	private void endProcess() {

		if (panel.getMemory().getProcess(runningProcess.getId()).getWaitingTime() <= 0)
			panel.getMemory().removeProcess(runningProcess);

		if (panel.getMemory().isEmpty())
			panel.clearCPUChart();

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
}
