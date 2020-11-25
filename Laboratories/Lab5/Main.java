package Lab5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        List<Process> processList = generateProcesses();
        ProcessScheduler processScheduler = new ProcessScheduler(processList, 20, 10);
        processScheduler.startTask1();
        System.out.println("\n\n\n\n\n\n\n");
        processScheduler.startTask2();
    }

    private static List<Process> generateProcesses() {
        Random random = new Random();
        List<Process> processes = new ArrayList<>();
        int count = 4 + random.nextInt(4);
        System.out.println("Создается " + count + " процессов:");
        for (int i = 1; i <= count; i++) {
            int leadTime = 5 + random.nextInt(30);
            boolean workWithInput = random.nextBoolean();
            processes.add(new Process(leadTime, workWithInput));
            System.out.println("Создан процесс " + i + " который будет работать " + leadTime + " единиц времени ");
            if (workWithInput) {
                System.out.println("и будет взаимодействовать с устройством ввода/вывода.");
            }
        }
        return processes;
    }
}
