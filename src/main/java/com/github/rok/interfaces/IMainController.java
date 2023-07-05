package com.github.rok.interfaces;

import com.github.rok.os.interfaces.ICPU;
import com.github.rok.os.Process;
import com.github.rok.os.interfaces.IMemory;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
/**
 * A interface IMainController representa o controlador principal em um sistema.
 * Ele é responsável por gerenciar processos e coordenar as interações entre a CPU e a memória.
 */
public interface IMainController {

	/**
	 * Adiciona um processo à CPU para execução com o tempo de processamento especificado.
	 *
	 * @param process          O processo a ser adicionado à CPU.
	 * @param timeProcessing   O tempo de processamento do processo na CPU.
	 *
	 * @implNote Recomendo usar o método setRunningProcess(process, process.getTimeProcessing())
	 * da CPU para executar o processo até o final.
	 */
	void addProcessToCPU(Process process, double timeProcessing);

	/**
	 * Obtém a instância IMemory associada ao controlador principal.
	 *
	 * @return A instância IMemory usada para gerenciar processos no sistema.
	 */
	IMemory getIMemory();

	/**
	 * Obtém a instância ICPU associada ao controlador principal.
	 *
	 * @return A instância ICPU usada para executar processos no sistema.
	 */
	ICPU getICPU();
}
