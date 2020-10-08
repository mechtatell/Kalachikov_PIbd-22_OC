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
                if (maxProcessTime - time < maxThreadTime) {
                    if (currentThread.startThread(maxProcessTime - time)) {
                        time += threadLeadTime;
                        currentThread = threads.poll();
                    } else {
                        time += maxThreadTime;
                        threads.addLast(currentThread);
                    }
                }
                else if (currentThread.isPriority()) {
                    if (currentThread.startThread(maxThreadTime * 2)) {
                        time += threadLeadTime;
                        currentThread = threads.poll();
                    } else {
                        time += maxThreadTime * 2;
                        threads.addLast(currentThread);
                        currentThread = threads.pollFirst();
                    }
                }
                else if (currentThread.startThread(maxThreadTime)) {
                    time += threadLeadTime;
                    currentThread = threads.poll();
                } else {
                    time += maxThreadTime;
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
        threads.add(new Thread(id, leadTime, this.id));
        System.out.println("Создан поток " + id + " в процессе " + this.id + " который требует на выполнение " + leadTime + " секунд");
    }
}
