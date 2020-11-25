package Lab5;

public class Process {

    private final int id;
    private int leadTime;
    private static int count;

    private int lock;
    private boolean workWithInput;

    public Process(int leadTime, boolean workWithInput) {
        count++;
        id = count;
        lock = -1;
        this.leadTime = leadTime;
        this.workWithInput = workWithInput;
    }

    private Process(int id, int leadTime, boolean workWithInput) {
        this.id = id;
        this.leadTime = leadTime;
        this.workWithInput = workWithInput;
    }

    public int getLeadTime() {
        return leadTime;
    }

    public int getId() {
        return id;
    }

    public void decreaseLeadTime(int time) {
        leadTime -= time;
    }

    public boolean isWorkWithInput() {
        return workWithInput;
    }

    public void lock(int startTime) {
        lock = startTime;
    }

    public void unlock() {
        lock = -1;
    }

    public int getLockStartTime() {
        return lock;
    }

    public Process cloneProcess() {
        return new Process(id, leadTime, workWithInput);
    }

    public void markInputWorkDone() {
        workWithInput = false;
    }
}
