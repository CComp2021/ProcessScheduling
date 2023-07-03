package com.github.rok.os;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Memory {

	public ArrayList<Process> processList = new ArrayList<>(10);
	// Um placeholder para preencher o gráfico
	private Process nullProcess = new Process(0, 0.0001);

	int lastId = 0;

	public Memory() {
		for (int i = 0; i < 10; i++) {
			processList.add(nullProcess);
		}
	}

	public Process createProcess(double waitingTime) {
		return new Process(++lastId, waitingTime);
	}

	public void addRandomProcessToMemory() {
		addProcessToMemory(Math.random() * 10 + 1);
	}

	public int getNextEmpty() {
		int lastEmpty = 0;
		while (processList.get(lastEmpty) != nullProcess) {
			lastEmpty++;
			if (lastEmpty == 10)
				return -1;
		}
		return lastEmpty;
	}

	public void addProcessToMemory(double waitingTime) {
		if (getNextEmpty() == -1) return; // TODO: tratar erro de memória cheia
		processList.set(getNextEmpty(), createProcess(waitingTime));
	}

	public Process getProcess(int id) {
		for (Process process : processList) {
			if (process.getId() == id)
				return process;
		}
		return null;
	}

	public void removeProcess(int id) {
		for (int i = 0; i < processList.size(); i++) {
			if (processList.get(i).getId() == id) {
				processList.set(i, nullProcess);
				return;
			}
		}
	}

	public void removeProcess(Process process) {
		removeProcess(process.getId());
	}

	@Nullable
	public Process getProcessByListPos(int pos) {
		if (processList.get(pos) == nullProcess)
			return null;
		return processList.get(pos);
	}

	@Nullable
	public Process getFirstProcess() {
		for (Process process : processList) {
			if (process != nullProcess)
				return process;
		}
		return null;
	}

	@Nullable
	public Process getLastProcess() {
		for (int i = processList.size() - 1; i >= 0; i--) {
			if (processList.get(i) != nullProcess)
				return processList.get(i);
		}
		return null;
	}

	public boolean isEmpty() {
		int count = 0;
		while (processList.get(count) == nullProcess) {
			count++;
			if (count == 10)
				return true;
		}
		return false;
	}
}
