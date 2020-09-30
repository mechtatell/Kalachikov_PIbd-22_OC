package com.company;

public class Thread {
    private int id;
    private int leadTime;

    public Thread(int id, int leadTime) {
        this.id = id;
        this.leadTime = leadTime;
    }

    public boolean startThread(int maxTime) {
        System.out.println("Поток " + id + " начал работу");
        if (leadTime > maxTime) {
            System.out.println("Поток " + id + " преостановлен");
            leadTime -= maxTime;
            return false;
        } else {
            System.out.println("Поток " + id + " завершил работу");
            return true;
        }
    }

    public int getId() {
        return id;
    }

    public int getLeadTime() {
        return leadTime;
    }
}
