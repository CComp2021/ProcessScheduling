package com.github.rok.os;

import com.github.rok.interfaces.IController;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
public class Counter {

	private long finalTime;
	private boolean running = false;
	private final IController controller;
	public Counter(IController controller) {
		this.controller = controller;
		new Thread(this::process).start();
	}

	public void setCounter(int minutes, int seconds) {
		if (running) return;
		running = true;
		finalTime = System.currentTimeMillis() + (((minutes * 60L) + seconds) * 1000L);
	}

	private void process() {
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!running) continue;
			if (System.currentTimeMillis() < finalTime && controller.isRunning()) continue;
			controller.removeControlButtons();
			controller.setRunning(false);
			running = false;
		}
	}

	public boolean isRunning() {
		return running;
	}

	public long getTime(){
		return finalTime - System.currentTimeMillis() +1000;
	}
}
