package com.github.rok.os;

import com.github.rok.IController;
import com.github.rok.utils.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Memory implements IMemory {

	public ArrayList<Process> processList = new ArrayList<>(10);
	// Um placeholder para preencher o gráfico
	private final Process nullProcess = new Process(0, 0.0001);

	private int generationSpeed = 300;
	private double nextToGenerate = 0;
	private boolean paused = true;
	private final IController controller;

	private int processMaxSize = 10;
	private int processMinSize = 1;

	int lastId = 0;

	public Memory(IController controller) {
		this.controller = controller;
		new Thread(this::generate).start();
		for (int i = 0; i < 10; i++) {
			processList.add(nullProcess);
		}
	}

	public void generate() {
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (paused) continue;
			nextToGenerate -= 1;
			controller.memoryTick(nextToGenerate);
			if (nextToGenerate <= 0) {
				addRandomProcessToMemory();
				controller.memoryTick(nextToGenerate);
				nextToGenerate = generationSpeed;
			}
		}
	}

	public Process createProcess(double waitingTime) {
		return new Process(++lastId, waitingTime);
	}

	public void addRandomProcessToMemory() {
		addProcessToMemory(Utils.generateRandomNumber(processMinSize, processMaxSize));
	}

	@Override
	public int getNextEmptySlot() {
		int lastEmpty = 0;
		while (processList.get(lastEmpty) != nullProcess) {
			lastEmpty++;
			if (lastEmpty == 10)
				return -1;
		}
		return lastEmpty;
	}

	public void clearMemory() {
		for (int i = 0; i < processList.size(); i++) {
			processList.set(i, nullProcess);
		}
		nextToGenerate = generationSpeed;
	}

	public void addProcessToMemory(double waitingTime) {
		if (getNextEmptySlot() == -1) return; // TODO: tratar erro de memória cheia
		processList.set(getNextEmptySlot(), createProcess(waitingTime));
	}

	public void setProcessSize(int max, int min) {
		this.processMaxSize = max;
		this.processMinSize = min;
	}

	@Override
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
	@Override
	public Process getProcessOnListPos(int pos) {
		if (processList.get(pos) == nullProcess)
			return null;
		return processList.get(pos);
	}

	@Nullable
	@Override
	public Process getFirstProcess() {
		for (Process process : processList) {
			if (process != nullProcess)
				return process;
		}
		return null;
	}

	@Nullable
	@Override
	public Process getLastProcess() {
		for (int i = processList.size() - 1; i >= 0; i--) {
			if (processList.get(i) != nullProcess)
				return processList.get(i);
		}
		return null;
	}

	@Override
	public @Nullable Process getLowestIdProcess() {
		Process lowestId = getFirstProcess();
		if (lowestId == null) return null;
		for (Process process : processList) {
			if (process == nullProcess) continue;
			if (process.getId() < lowestId.getId())
				lowestId = process;
		}
		return lowestId;
	}

	@Override
	public @Nullable Process getHighestIdProcess() {
		Process highestId = getFirstProcess();
		if (highestId == null) return null;
		for (Process process : processList) {
			if (process.getId() > highestId.getId())
				highestId = process;
		}
		return highestId;
	}

	@Override
	public @Nullable Process getLowestTimeProcess() {
		Process lowestTime = getFirstProcess();
		if (lowestTime == null) return null;
		for (Process process : processList) {
			if (process == nullProcess) continue;
			if (process.getWaitingTime() < lowestTime.getWaitingTime())
				lowestTime = process;
		}
		return lowestTime;
	}

	@Override
	public @Nullable Process getHighestTimeProcess() {
		Process highestTime = getFirstProcess();
		if (highestTime == null) return null;
		for (Process process : processList) {
			if (process == nullProcess) continue;
			if (process.getWaitingTime() > highestTime.getWaitingTime())
				highestTime = process;
		}
		return highestTime;
	}

	@Override
	public boolean isEmpty() {
		int count = 0;
		while (processList.get(count) == nullProcess) {
			count++;
			if (count == 10)
				return true;
		}
		return false;
	}

	public double getGenerationSpeed() {
		return generationSpeed;
	}

	public void setGenerationSpeed(int generationSpeed) {
		this.generationSpeed = generationSpeed;
		this.nextToGenerate = generationSpeed;
	}
	public void pause(boolean paused) {
		this.paused = paused;

	}
}
