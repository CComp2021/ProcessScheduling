package com.github.rok.algorithm;

import com.github.rok.interfaces.IMainController;
import com.github.rok.interfaces.AlgorithmInterface;
import com.github.rok.os.Process;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
public class AlgorithmFIFO implements AlgorithmInterface {
	private final IMainController controller;
	public AlgorithmFIFO(IMainController controller) {
		this.controller = controller;
	}
	@Override
	public void execute() {
		Process lowestIdProcess = controller.getIMemory().getLowestIdProcess();
		if (lowestIdProcess == null) return;
		controller.addProcessToCPU(lowestIdProcess, lowestIdProcess.getWaitingTime()/2);
	}
}
