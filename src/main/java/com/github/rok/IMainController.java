package com.github.rok;

import com.github.rok.os.ICPU;
import com.github.rok.os.IMemory;
import com.github.rok.os.Process;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
public interface IMainController {


	void addProcessToCPU(Process process, double timeProcessing);

	IMemory getIMemory();

	ICPU getICPU();
}
