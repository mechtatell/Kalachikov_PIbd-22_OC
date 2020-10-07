package Lab2;

public class Thread {
    private final int id;
    private int leadTime;
    private final int processID;

    public Thread(int id, int leadTime, int processID) {
        this.id = id;
        this.leadTime = leadTime;
        this.processID = processID;
    }

    public boolean startThread(int maxTime) {
        System.out.print("Поток " + id + " начал работу");
        if (leadTime > maxTime) {
            leadTime -= maxTime;
            System.out.println(" и приостановлен (осталось отработать " + leadTime + ")");
            return false;
        } else {
            System.out.println(" и успешно завершен (отработал все " + leadTime + ")");
            return true;
        }
    }

    public int getId() {
        return id;
    }

    public int getLeadTime() {
        return leadTime;
    }

    public int getProcessID() {
        return processID;
    }
}
