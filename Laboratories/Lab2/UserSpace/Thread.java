package Lab2.UserSpace;

public class Thread {
    private final int id;
    private int leadTime;
    private final boolean priority;

    public Thread(int id, int leadTime) {
        this.id = id;
        this.leadTime = leadTime;
        priority = Math.random() > 0.5;
    }

    public boolean startThread(int maxTime) {
        System.out.print("Поток " + id + " начал работу");
        String priority = "";
        if (this.priority) priority = " (Приоритетный)";
        if (leadTime > maxTime) {
            leadTime -= maxTime;
            System.out.println(" и приостановлен (осталось отработать " + leadTime + ", сейчас отработал " + maxTime + ")" + priority);
            return false;
        } else {
            System.out.println(" и успешно завершен (отработал все " + leadTime + ")" + priority);
            return true;
        }
    }

    public int getLeadTime() {
        return leadTime;
    }

    public boolean isPriority() {
        return priority;
    }
}
