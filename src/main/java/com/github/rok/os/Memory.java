package com.github.rok.os;

import com.github.rok.interfaces.IController;
import com.github.rok.os.interfaces.IMemory;
import com.github.rok.utils.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Memory implements IMemory {

    public ArrayList<Process> processList = new ArrayList<>(10);
    private static final Map<Process, Long> endedProcesses = new HashMap<>();

    // Um placeholder para preencher o gráfico
    private final Process nullProcess = new Process(0, 0.0001, 0, 0);

    private int generationSpeed = 300;
    private double nextToGenerate = 0;
    private boolean paused = true;
    private final IController controller;

    private int processMaxSize = 10;
    private int processMinSize = 1;

    int lastId = 0;

    public Memory(IController controller) {
        this.controller = controller;
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Thread thread = new Thread(this::generate);
        executorService.scheduleAtFixedRate(thread, 0, 10, TimeUnit.MILLISECONDS);
        for (int i = 0; i < 10; i++) {
            processList.add(nullProcess);
        }
    }

    public void generate() {
        if (paused) return;
        nextToGenerate -= 1;
        controller.memoryTick(nextToGenerate);
        if (nextToGenerate <= 0) {
            addRandomProcessToMemory();
            controller.memoryTick(nextToGenerate);
            nextToGenerate = generationSpeed;
        }
    }

    public Process createProcess(double waitingTime, int priority) {
        return new Process(++lastId, waitingTime, System.currentTimeMillis(), priority);
    }

    public void addRandomProcessToMemory() {
        addProcessToMemory(Utils.generateRandomNumber(processMinSize, processMaxSize), (int) Utils.generateRandomNumber(controller.getPriorMin(), controller.getPriorMax()));
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
        nextPos = -1;
        lastId = 0;
        endedProcesses.clear();
        nextToGenerate = generationSpeed;
    }

    public void addProcessToMemory(double waitingTime, int priority) {
        if (getNextEmptySlot() == -1) return; // TODO: tratar erro de memória cheia
        Process process = createProcess(waitingTime, priority);
        controller.memoryNewProcessTick(process);
        processList.set(getNextEmptySlot(), process);
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
            Process process = processList.get(i);
            if (process.getId() == id) {
                endedProcesses.put(process.clone(), System.currentTimeMillis() - process.getArrivalTime());
                if (getListPosById(id)+1 < processList.size()) {
                    for (int j = getListPosById(id); j < processList.size() - 1; j++) {
                        processList.set(j, processList.get(j + 1));
                    }
                }
                removedMemory = true;
                processList.set(9, nullProcess);
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
    public List<Process> getProcessList() {
        List<Process> processList = new ArrayList<>();
        for (Process process : this.processList) {
            if (process != nullProcess)
                processList.add(process);
        }
        return processList;
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
    public @Nullable Process getLowestPriorityProcess() {
        Process lowestPriority = getFirstProcess();
        if (lowestPriority == null) return null;
        for (Process process : processList) {
            if (process == nullProcess) continue;
            if (process.getPriority() < lowestPriority.getPriority())
                lowestPriority = process;
        }
        return lowestPriority;
    }

    @Override
    public @Nullable Process getHighestPriorityProcess() {
        Process highestPriority = getFirstProcess();
        if (highestPriority == null) return null;
        for (Process process : processList) {
            if (process == nullProcess) continue;
            if (process.getPriority() > highestPriority.getPriority())
                highestPriority = process;
        }
        return highestPriority;
    }
    @Override
    public @Nullable Process getHighestPriorityProcessBelow(Process targetProcess) {
        Process highestPriorityBelow = null;
        int targetPriority = targetProcess.getPriority();

        for (Process process : processList) {
            if (process == nullProcess || process == targetProcess) {
                continue;
            }

            int processPriority = process.getPriority();

            if (processPriority <= targetPriority) {

                if (highestPriorityBelow == null || processPriority > highestPriorityBelow.getPriority()) {
                    highestPriorityBelow = process;
                }
            }
        }

        return highestPriorityBelow;
    }

    private int nextPos = -1;
    private boolean removedMemory = false;
    @Override
    public @Nullable Process getNextProcessOnList() {
        if (!removedMemory) {
            nextPos++;
        } else if (nextPos == -1) {
            nextPos = 0;
        }
        while (nextPos < processList.size() && nullProcess == processList.get(nextPos)) {
            nextPos++;
        }
        if (nextPos >= processList.size()) {
            nextPos = -1;
            return null;
        }
        removedMemory = false;
        return processList.get(nextPos);
    }

    @Override
    public @Nullable Process getNextProcessByInitial(int initialPos) {
        int nextIPos = initialPos +1;
        Process nextProcess = nullProcess;
        while (nextIPos < processList.size() && nextProcess == nullProcess) {
            nextProcess = processList.get(nextIPos);
            nextIPos++;
        }
        if (nextPos >= processList.size()) {
            nextIPos = 0;
        }
        while (nextProcess == nullProcess && nextIPos < initialPos) {
            nextProcess = processList.get(nextIPos);
            nextIPos++;
        }
        if (nextProcess == nullProcess) {
            return null;
        }
        return processList.get(nextIPos);
    }

    @Override
    public int getListPosById(int id) {
        for (int i = 0; i < processList.size(); i++) {
            if (processList.get(i).getId() == id)
                return i;
        }
        return -1;
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

    @Override
    public int getMemorySize() {
        int size = 0;
        for (Process process : processList) {
            if (process != nullProcess)
                size ++;
        }
        return size;
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
    public Map<Process, Long> getEnded() {
        return endedProcesses;
    }
}
