package com.github.rok.os;

import org.jetbrains.annotations.Nullable;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
public interface IMemory {
	int getNextEmptySlot();

	Process getProcess(int id);

	@Nullable Process getProcessOnListPos(int pos);

	@Nullable Process getFirstProcess();

	@Nullable Process getLastProcess();

	boolean isEmpty();
}
