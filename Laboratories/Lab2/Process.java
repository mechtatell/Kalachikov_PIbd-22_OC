package com.company;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Process {

    private int id;
    private int leadTime;
    private Core core;

    public Process(int id, Core core) {
        this.id = id;
        this.core = core;
    }

    public void setLeadTime(int leadTime) {
        this.leadTime = leadTime;
    }

    public boolean startProcess(int maxTime) {
        System.out.println("Процесс " + id + " начал работу (" + leadTime + ")");
        int time = 0;
        Thread currentThread = core.findThreadWithSmallestLeadTime(this);
        while (time < maxTime && currentThread != null) {
            if (currentThread.startThread(maxTime)) {
                time += currentThread.getLeadTime();
            }
            currentThread = core.findThreadWithSmallestLeadTime(this);
        }
        if (leadTime > maxTime) {
            leadTime -= maxTime;
            System.out.println("Процесс " + id + " прерван (" + leadTime + ")");
            return false;
        } else {
            System.out.println("Процесс " + id + " завершил работу");
            return true;
        }
    }

    public void createThread(int id, int leadTime) {
        core.getThreads().put(this, new Thread(id, leadTime));
        System.out.println("Создан поток " + id + " который требует на выполнение " + leadTime + " секунд");
    }

    public int getId() {
        return id;
    }

    public int getLeadTime() {
        return leadTime;
    }
}
