package com.github.rok.interfaces;

import com.github.rok.os.Process;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
public interface AlgorithmInterface {

	/**
	 * @implNote Esse método é chamado quando o controlador quer enviar um processo para o processador
	 */
	void execute();

	/**
	 * @implNote Esse método é chamado sempre que um processo novo é inserido na memória
	 */
	void tickNewProcess(Process newProcess);
}
