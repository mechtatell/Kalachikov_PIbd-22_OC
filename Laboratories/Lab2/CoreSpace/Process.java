package Lab2.CoreSpace;

public class Process {

    private final int id;
    private int leadTime;
    private final Core core;

    public Process(int id, Core core) {
        this.id = id;
        this.core = core;
    }

    public void setLeadTime(int leadTime) {
        this.leadTime = leadTime;
    }

    public boolean startProcess(int maxTime) {
        if (leadTime > maxTime) {
            leadTime -= maxTime;
            System.out.println("Процесс " + id + " прерван (осталось отработать " + leadTime + ")");
            System.out.println("---переключение---\n");
            return false;
        } else {
            System.out.println("---Процесс " + id + " завершил работу---\n");
            return true;
        }
    }

    public void createThread(int id, int leadTime) {
        core.getThreads().add(new Thread(id, leadTime, this.id));
        System.out.println("Создан поток " + id + " в процессе " + this.id + " который требует на выполнение " + leadTime + " секунд");
    }

    public int getId() {
        return id;
    }
}
