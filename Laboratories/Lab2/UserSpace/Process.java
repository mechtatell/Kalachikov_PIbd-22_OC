package Lab2.UserSpace;

import java.util.LinkedList;

public class Process {

    private final int id;
    private int leadTime;
    private final LinkedList<Thread> threads;

    public Process(int id) {
        this.id = id;
        this.threads = new LinkedList<>();
    }

    public void setLeadTime(int leadTime) {
        this.leadTime = leadTime;
    }

    public boolean startProcess(int maxTime) {

        System.out.println("Процесс " + id + " начал работу!");

        if (!threads.isEmpty()) {
            Thread currentThread = threads.pollFirst();
            int time = 0;
            while (currentThread != null) {
                if (currentThread.startThread(maxTime - time)) {
                    time += currentThread.getLeadTime();
                    currentThread = threads.poll();
                } else {
                    threads.addFirst(currentThread);
                    break;
                }
            }
        }

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
        threads.add(new Thread(id, leadTime, this.id));
        System.out.println("Создан поток " + id + " в процессе " + this.id + " который требует на выполнение " + leadTime + " секунд");
    }
}
