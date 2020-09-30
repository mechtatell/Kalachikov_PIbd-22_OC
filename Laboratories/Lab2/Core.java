package com.company;

import java.util.*;

public class Core {

    private final HashMap<Integer, Process> processes;
    private HashMap<Process, Thread> threads;

    public void schedule() {
        Process currentProcess = findProcessWithSmallestLeadTime();
        while (currentProcess != null) {
            int maxTime = 5;
            if (currentProcess.startProcess(maxTime)) {
                processes.remove(currentProcess.getId());
            }
            currentProcess = findProcessWithSmallestLeadTime();
        }
    }

    private void createProcess(int id) {
        processes.put(id, new Process(id, this));
        System.out.println("Создан процесс " + id);
    }

    public HashMap<Process, Thread> getThreads() {
        return threads;
    }

    private void init() {
        Random random = new Random();
        for (int id = 1; id < random.nextInt(7) + 3; id++) {
            createProcess(id);
            int processLeadTime = 0;
            for (int idThread = 1; idThread < random.nextInt(7) + 3; idThread++) {
                int threadLeadTime = random.nextInt(10) + 3;
                processes.get(id).createThread(idThread, threadLeadTime);
                processLeadTime += threadLeadTime;
            }
            processes.get(id).setLeadTime(processLeadTime);
            System.out.println("Процесс " + id + " требует на выполнение " + processLeadTime);
        }
    }

    public Core() {
        processes = new HashMap<>();
        threads = new HashMap<>();
        init();
    }

    private Process findProcessWithSmallestLeadTime() {
        int minTime = 15;
        Process processWithSmallestLeadTime = null;
        for (Map.Entry<Integer, Process> entry : processes.entrySet()) {
            if (entry.getValue().getLeadTime() < minTime) {
                minTime = entry.getValue().getLeadTime();
                processWithSmallestLeadTime = entry.getValue();
            }
        }
        return processWithSmallestLeadTime;
    }

    public Thread findThreadWithSmallestLeadTime(Process process) {
        int minTime = 15;
        Thread threadWithSmallestLeadTime = null;
        for (Map.Entry<Process, Thread> entry : threads.entrySet()) {
            if (entry.getValue().getLeadTime() < minTime && entry.getKey() == process) {
                minTime = entry.getValue().getLeadTime();
                threadWithSmallestLeadTime = entry.getValue();
            }
        }
        return threadWithSmallestLeadTime;
    }

    public void setThreads(HashMap<Process, Thread> threads) {
        this.threads = threads;
    }
}
