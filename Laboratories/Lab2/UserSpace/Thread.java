package Lab2.UserSpace;

import org.w3c.dom.ls.LSOutput;

public class Thread {
    private final int id;
    private int leadTime;
    private final int processID;
    private boolean priority;

    public Thread(int id, int leadTime, int processID) {
        this.id = id;
        this.leadTime = leadTime;
        this.processID = processID;
        priority = Math.random() > 0.5;
    }

    public boolean startThread(int maxTime) {
        System.out.print("Поток " + id + " начал работу");
        if (leadTime > maxTime) {
            leadTime -= maxTime;
            System.out.print(" и приостановлен (осталось отработать " + leadTime + ", сейчас отработал " + maxTime + ")");
            if (priority) {
                System.out.print(" (Приоритетный)");
            }
            System.out.println();
            return false;
        } else {
            System.out.println(" и успешно завершен (отработал все " + leadTime + ")");
            return true;
        }
    }

    public int getLeadTime() {
        return leadTime;
    }

    public int getProcessID() {
        return processID;
    }

    public boolean isPriority() {
        return priority;
    }
}
