package com.github.rok.os;

import com.github.rok.MainPanel;
import com.github.rok.interfaces.IReport;
import com.github.rok.panel.ReportPanel;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * @author Rok, Pedro Lucas N M Machado created on 07/07/2023
 */
public class Report implements IReport {

    private final ReportPanel reportPanel;

    private final CPU cpu;
    private double cpuRunningTime;
    private final MainPanel mainPanel;

    private long startingTime;
    private long pausedTime;

    private boolean running = false;

    private final Map<Process, Long> endedProcesses;
    public Report(ReportPanel reportPanel, CPU cpu, Map<Process, Long> ended, MainPanel mainPanel) {
        this.reportPanel = reportPanel;
        this.cpu = cpu;
        this.endedProcesses = ended;
        this.mainPanel = mainPanel;
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Thread thread = new Thread(this::tick);
        executorService.scheduleAtFixedRate(thread, 0, 10, TimeUnit.MILLISECONDS);
    }

    int seconds = 0;

    private void tick() {
        if (!running) return;
        if (mainPanel.isPaused()) {
            pausedTime += 10;
            return;
        }

        if (cpu.getRunningProcess() != null) {
            cpuRunningTime += 10;
        }
        if (cpu.isScaling()) {
            cpuRunningTime += 2;
        }


        reportPanel.setCpuValue((cpuRunningTime / getTimeRunning()));

        if (endedProcesses.isEmpty()) return;

        double averageReturnTime = 0;
        double averageQueueTime = 0;
        for (Map.Entry<Process, Long> entry : endedProcesses.entrySet()) {
            averageReturnTime += entry.getValue();
            averageQueueTime += entry.getValue() - (entry.getKey().getTimeScaling() + (entry.getKey().getTimeOnCPU() * 1000));
        }

        reportPanel.setThroughputValue((double) endedProcesses.size() / ((double) (getTimeRunning() / 1000) / 60));
        averageReturnTime /= endedProcesses.size();
        averageQueueTime /= endedProcesses.size();
        reportPanel.setReturnProcessValue(averageReturnTime / 1000);
        reportPanel.setQueueValue(averageQueueTime / 1000);
    }

    private long getTimeRunning() {
        return System.currentTimeMillis() - startingTime - pausedTime;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
        if (running) {
            reportPanel.setReturnProcessValue(0);
            reportPanel.setThroughputValue(0);
            reportPanel.setCpuValue(0);
            reportPanel.setQueueValue(0);
            startingTime = System.currentTimeMillis();
            return;
        }
        cpuRunningTime = 0;
        pausedTime = 0;
    }


}
