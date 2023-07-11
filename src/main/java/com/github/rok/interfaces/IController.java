package com.github.rok.interfaces;

import com.github.rok.os.CPU;
import com.github.rok.os.Process;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
public interface IController {

	void updateTick();

	void scalingTick(boolean toMemory, double completePercentage, CPU.STATE isRunning);

	void memoryTick(double tick);
	void sendToMemory(Process process);

	void removeControlButtons();

	void setRunning(boolean running);
	boolean isRunning();

	void memoryNewProcessTick(Process newProcess);

	int getPriorMax();
	int getPriorMin();
}
