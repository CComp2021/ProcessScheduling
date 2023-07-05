package com.github.rok.os;

import org.jetbrains.annotations.Nullable;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
public interface ICPU {

	void setRunningProcess(Process process, double timeProcessing);

	void endProcess();

	@Nullable Process getRunningProcess();

	double getTimeProcessing();

	boolean isRunning();
}
