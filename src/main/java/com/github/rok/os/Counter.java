package com.github.rok.os;

import com.github.rok.interfaces.IController;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * @author Rok, Pedro Lucas N M Machado created on 05/07/2023
 */
public class Counter {

    private long finalTime;
    private boolean running = false;
    private final IController controller;

    private boolean paused = false;
    private long pausedTime = 0;

    public Counter(IController controller) {
        this.controller = controller;
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Thread thread = new Thread(this::process);
        executorService.scheduleAtFixedRate(thread, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void setCounter(int minutes, int seconds) {
        if (running) return;
        running = true;
        paused = false;
        pausedTime = 0;
        finalTime = System.currentTimeMillis() + (((minutes * 60L) + seconds) * 1000L);
    }

    private void process() {

        if (!running) return;
        if (paused) {
            pausedTime += 10;
        }
        if (System.currentTimeMillis() < finalTime + pausedTime && controller.isRunning()) return;
        controller.removeControlButtons();
        controller.setRunning(false);
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public long getTime() {
        return finalTime + pausedTime - System.currentTimeMillis() + 1000;
    }
}
