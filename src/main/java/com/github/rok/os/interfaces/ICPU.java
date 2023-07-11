package com.github.rok.os.interfaces;

import com.github.rok.os.Process;
import org.jetbrains.annotations.Nullable;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
/**
 * A interface ICPU representa a Unidade Central de Processamento (CPU) em um sistema.
 * Ela define os métodos e comportamentos relacionados à execução de processos.
 */
public interface ICPU {



	/**
	 * Obtém o processo em execução na CPU.
	 *
	 * @return O processo em execução na CPU, ou null se não houver processo em execução.
	 */
	@Nullable Process getRunningProcess();

	/**
	 * Obtém o tempo de processamento atual da CPU.
	 *
	 * @return O tempo já computado do processo atual da CPU.
	 */
	double getTimeProcessing();

	/**
	 * Verifica se a CPU está em execução.
	 *
	 * @return true se a CPU estiver em execução, false caso contrário.
	 */
	boolean isRunning();

	void stopProcess();
}
