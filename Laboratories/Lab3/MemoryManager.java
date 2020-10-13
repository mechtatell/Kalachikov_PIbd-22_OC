package Lab3;

import java.util.ArrayList;
import java.util.List;

public class MemoryManager {

    private final PageTable physicalMemory;
    private final List<Process> processList;

    public void start() {
        //количество рассматриваемых итераций
        int iterationCount = 5;
        for (int i = 0; i < iterationCount; i++) {
            for (Process process : processList) {
                process.start(this);
            }
        }
    }

    public MemoryManager() {
        int memorySize = 32;
        processList = new ArrayList<>();
        physicalMemory = new PageTable(memorySize);
        processInit();
    }

    public void printTable() {
        System.out.println("Индекс\t||\tПризнак обращения\t||\tНомер страницы\t||\tНомер процесса");
        for (int i = 0; i < physicalMemory.getPageTable().length; i++) {
            System.out.println(i + "\t\t||\t" + physicalMemory.getPageTable()[i].getAppealStatus() + "\t\t\t\t\t||\t" +
                    physicalMemory.getPageTable()[i].getId() + "\t\t\t\t||\t" + physicalMemory.getPageTable()[i].getProcessId());
        }
        System.out.println();
    }

    public PageTable getPhysicalMemory() {
        return physicalMemory;
    }

    private void processInit() {
        for (int i = 1; i < 3 + Math.random() * 2; i++) {
            processList.add(new Process(i));
        }
        System.out.println("\n===========\n");
    }
}
