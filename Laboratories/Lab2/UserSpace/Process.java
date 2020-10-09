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

    public boolean startProcess(int maxProcessTime) {

        System.out.println("Процесс " + id + " начал работу!");

        if (!threads.isEmpty()) {

            int maxThreadTime = maxProcessTime / threads.size();
            System.out.println("Время выделенное каждому потоку - " + maxThreadTime);
            Thread currentThread = threads.pollFirst();
            int time = 0;
            while (currentThread != null && time < maxProcessTime) {
                int threadLeadTime = currentThread.getLeadTime();
                int currentThreadTime;

                if (currentThread.isPriority()) {
                    currentThreadTime = Math.min(maxProcessTime - time, maxThreadTime * 2);
                } else {
                    currentThreadTime = Math.min(maxProcessTime - time, maxThreadTime);
                }

                if (currentThread.startThread(currentThreadTime)) {
                    time += threadLeadTime;
                    currentThread = threads.poll();
                } else {
                    time += currentThreadTime;
                    threads.addLast(currentThread);
                    currentThread = threads.pollFirst();
                }
            }

            if (currentThread != null) {
                threads.addFirst(currentThread);
            }
        }

        if (leadTime > maxProcessTime) {
            leadTime -= maxProcessTime;
            System.out.println("Процесс " + id + " прерван (осталось отработать " + leadTime + ")");
            System.out.println("---переключение---\n");
            return false;
        } else {
            System.out.println("---Процесс " + id + " завершил работу---\n");
            return true;
        }
    }

    public void createThread(int id, int leadTime) {
        Thread thread = new Thread(id, leadTime);
        threads.add(thread);
        String priority = "";
        if (thread.isPriority()) priority = " (Приоритетный)";
        System.out.println("Создан поток " + id + " в процессе " + this.id + " который требует на выполнение " + leadTime + " секунд" + priority);
    }
}
