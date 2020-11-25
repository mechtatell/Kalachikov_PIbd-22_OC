package Lab5;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ProcessScheduler {

    private final List<Process> processList;
    private final int maxTime;
    private final int inputTime;

    public ProcessScheduler(List<Process> processList, int maxTime, int inputTime) {
        this.processList = processList;
        this.maxTime = maxTime;
        this.inputTime = inputTime;
    }

    public void startTask1() {
        System.out.println("Программный ввод/вывод");
        System.out.println("************************");
        Queue<Process> currentProcessQueue = cloneQueue(processList);
        Process currentProcess = currentProcessQueue.poll();
        int fullTime = 0;
        while (currentProcess != null) {

            System.out.println("\nПроцесс " + currentProcess.getId() + " начал работу! (оставшееся время "
                    + currentProcess.getLeadTime() + ")");

            int addTime = 0;
            if (currentProcess.isWorkWithInput()) {
                System.out.println("\n===================");
                System.out.println("Процесс " + currentProcess.getId() + " работает с устройством ввода вывода...");
                System.out.println("***Остановка планировщика***");
                System.out.println("На ввод данных потребовалось " + inputTime);
                currentProcess.markInputWorkDone();
                addTime = inputTime;
                System.out.println("Полное затраченное время = " + fullTime + " + " + inputTime + " = " + (fullTime + inputTime));
                fullTime += inputTime;
                System.out.println("Процесс " + currentProcess.getId() + " продолжает работу");
                System.out.println("===================\n");
            }


            if (currentProcess.getLeadTime() <= maxTime - addTime) {
                System.out.println("Процесс " + currentProcess.getId() + " завершил работу (отработал " +
                        currentProcess.getLeadTime() + ")");
                System.out.println("Полное затраченное время = " + fullTime + " + " + currentProcess.getLeadTime() +
                        " = " + (fullTime + currentProcess.getLeadTime()));
                fullTime += currentProcess.getLeadTime();
            } else {
                if (addTime > maxTime) addTime = maxTime;
                System.out.println("Процесс " + currentProcess.getId() + " не завершил работу (отработал " +
                        (maxTime - addTime) + " из " + currentProcess.getLeadTime() + ")");
                System.out.println("Полное затраченное время = " + fullTime + " + " + (maxTime - addTime) +
                        " = " + (fullTime + maxTime - addTime));
                fullTime += maxTime - addTime;
                currentProcess.decreaseLeadTime(maxTime - addTime);
                currentProcessQueue.add(currentProcess);
            }

            currentProcess = currentProcessQueue.poll();
        }

        System.out.println("************************\nВремени всего - " + fullTime);
    }

    public void startTask2() {
        System.out.println("Ввод-вывод, управляемый прерываниями");
        System.out.println("************************");
        Queue<Process> currentProcessQueue = new LinkedList<>(processList);
        Process currentProcess = currentProcessQueue.poll();
        int fullTime = 0;
        int waitTime = 0;
        while (currentProcess != null) {
            if (currentProcess.getLockStartTime() >= 0) {
                currentProcessQueue.add(currentProcess);
                waitTime++;
                fullTime++;
                lockedProcessCheck(currentProcessQueue, fullTime);
                currentProcess = currentProcessQueue.poll();
                continue;
            } else if (waitTime > 0) {
                System.out.println("Ожидание...");
                System.out.println("Полное затраченное время = " + (fullTime - waitTime) + " + " + waitTime +
                        " = " + fullTime);
                waitTime = 0;
            }

            System.out.println("\nПроцесс " + currentProcess.getId() + " начал работу! (оставшееся время " + currentProcess.getLeadTime() + ")");

            if (currentProcess.isWorkWithInput()) {
                System.out.println("Процесс " + currentProcess.getId() + " работает с устройством ввода вывода...");

                System.out.println("***Блокировка процесса***");
                currentProcess.lock(fullTime);
                currentProcessQueue.add(currentProcess);
                currentProcess = currentProcessQueue.poll();
                continue;
            }

            if (currentProcess.getLeadTime() <= maxTime) {
                fullTime += currentProcess.getLeadTime();
                lockedProcessCheck(currentProcessQueue, fullTime);
                System.out.println("Процесс " + currentProcess.getId() + " завершил работу (отработал " +
                        currentProcess.getLeadTime() + ")");
                System.out.println("Полное затраченное время = " + (fullTime - currentProcess.getLeadTime()) + " + " + currentProcess.getLeadTime() +
                        " = " + fullTime);
            } else {
                fullTime += maxTime;
                lockedProcessCheck(currentProcessQueue, fullTime);
                System.out.println("Процесс " + currentProcess.getId() + " не завершил работу (отработал " +
                        maxTime + " из " + currentProcess.getLeadTime() + ")");
                System.out.println("Полное затраченное время = " + (fullTime - maxTime) + " + " + maxTime +
                        " = " + fullTime);
                currentProcess.decreaseLeadTime(maxTime);
                currentProcessQueue.add(currentProcess);
            }
            currentProcess = currentProcessQueue.poll();
        }
        System.out.println("************************\nВремени всего - " + fullTime);
    }

    private void lockedProcessCheck(Queue<Process> processQueue, int currentTime) {
        for (Process process : processQueue) {
            if (process.getLockStartTime() >= 0 && currentTime - process.getLockStartTime() >= inputTime) {
                System.out.println("\n===================");
                System.out.println("Процесс " + process.getId() + " закончил работу с устройством ввода/вывода");
                System.out.println("На ввод данных потребовалось " + inputTime);
                System.out.println("Процесс " + process.getId() + " продолжает работу");
                System.out.println("===================\n");
                process.markInputWorkDone();
                process.unlock();
            }
        }
    }

    private Queue<Process> cloneQueue(List<Process> defaultProcessList) {
        Queue<Process> newProcessList = new LinkedList<>();
        for (Process process : defaultProcessList) {
            newProcessList.add(process.cloneProcess());
        }
        return newProcessList;
    }
}
