package Lab2;

import java.util.*;

public class Core {

    private final Queue<Process> processes;
    private final Queue<Thread> threads;
    private int maxTime = 10;

    public void schedule() {
        Process currentProcess = processes.poll();
        while (currentProcess != null) {
            if (currentProcess.startProcess(maxTime)) {
                currentProcess = processes.poll();
            } else {
                processes.add(currentProcess);
                currentProcess = processes.poll();
            }
        }
    }

    private Process createProcess(int id) {
        Process process = new Process(id, this);
        processes.add(process);
        System.out.println("Создан процесс " + id);
        return process;
    }

    public Queue<Thread> getThreads() {
        return threads;
    }

    private void init() {
        Random random = new Random();
        for (int id = 1; id < random.nextInt(2) + 3; id++) {
            Process process = createProcess(id);
            int processLeadTime = 0;
            for (int idThread = 1; idThread < random.nextInt(2) + 3; idThread++) {
                int threadLeadTime = random.nextInt(10) + 3;
                process.createThread(idThread, threadLeadTime);
                processLeadTime += threadLeadTime;
            }
            process.setLeadTime(processLeadTime);
            System.out.println("Процесс " + id + " требует на выполнение " + processLeadTime + '\n');
        }
        System.out.println("\n-----\n");
    }

    public Core() {
        processes = new LinkedList<>();
        threads = new LinkedList<>();
        init();
    }

    public Queue<Thread> getThreads(int idProcess) {
        Queue<Thread> queue = new LinkedList<>();
        int size = threads.size();
        for (int i = 0; i < size && !threads.isEmpty(); i++) {
            if (threads.peek().getProcessID() == idProcess) {
                queue.add(threads.poll());
            } else {
                threads.add(threads.poll());
            }
        }
        return queue;
    }

    public void returnToThreads(Queue<Thread> queue) {
        while (!queue.isEmpty()) {
            threads.add(queue.poll());
        }
    }
}
