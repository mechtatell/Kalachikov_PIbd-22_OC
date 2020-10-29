package Lab2.UserSpace;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Core {

    private final Queue<Process> processes;

    public void schedule() {
        int maxTime = 10;
        Process currentProcess = processes.poll();
        while (currentProcess != null) {
            if (!currentProcess.startProcess(maxTime)) {
                processes.add(currentProcess);
            }
            currentProcess = processes.poll();
        }
    }

    private Process createProcess(int id) {
        Process process = new Process(id);
        processes.add(process);
        System.out.println("Создан процесс " + id);
        return process;
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
        init();
    }
}
