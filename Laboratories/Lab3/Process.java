package Lab3;

import java.util.ArrayList;

public class Process {

    private final int id;
    private final int initCallSign = 15;
    private ArrayList<PageCell> virtualMemory;

    public void start(MemoryManager memoryManager) {
        PageCell[] physicalMemory = memoryManager.getPhysicalMemory().getPageTable();

        //каждый процесс в зависимости от пользователя обращается к случайной страничке виртуальной памяти
        int index = (int) (Math.random() * virtualMemory.size());

        //если эта страничка уже есть в физической памяти, увеличиваем ее признак обращения
        if (virtualMemory.get(index).getIdOnPhysicalMemory() != -1 && physicalMemory[virtualMemory.get(index).getIdOnPhysicalMemory()] != null) {
            virtualMemory.get(index).setAppealStatus(virtualMemory.get(index).getAppealStatus() + 10);
            System.out.println("Страница " + virtualMemory.get(index).getId() + " процесса " + id +
                    " находится в физической памяти по адресу " + virtualMemory.get(index).getIdOnPhysicalMemory() +
                    " с признаком обращения " + virtualMemory.get(index).getAppealStatus() +
                    "\n===Обращение (признак обращения +10)===\n");
        }

        //если этой странички нет в физической памяти и физическая память заполнена не полностью,
        //записываем страничку в физическую память
        else if (physicalMemory[physicalMemory.length - 1] == null) {
            for (int i = 0; i < physicalMemory.length; i++) {
                if (physicalMemory[i] == null) {
                    virtualMemory.get(index).setAppealStatus(initCallSign);
                    virtualMemory.get(index).setIdOnPhysicalMemory(i);
                    physicalMemory[i] = virtualMemory.get(index);
                    System.out.println("Страница " + virtualMemory.get(index).getId() + " процесса " + id + " занесена в физическую память по адресу " + i);
                    break;
                }
            }
        }

        //если этой странички нет в физической памяти и физическая память заполнена
        //делаем страничное прерывание
        else {
            System.out.print("\nСтраница " + virtualMemory.get(index).getId() + " процесса " + id + " ищется в памяти...");
            System.out.println("\n=======\nСтраничное прерывание\n=======\n");
            memoryManager.printTable();
            int min = 999;
            int idMin = -1;
            for (int i = 0; i < physicalMemory.length; i++) {
                if (physicalMemory[i].getAppealStatus() > 0 && physicalMemory[i].getAppealStatus() < min) {
                    min = physicalMemory[i].getAppealStatus();
                    idMin = i;
                } else if (physicalMemory[i].getAppealStatus() == 0) {
                    idMin = i;
                    break;
                }
            }
            System.out.println("Страница " + physicalMemory[idMin].getId() + " процесса " + physicalMemory[idMin].getProcessId() +
                    " с признаком обращения " + physicalMemory[idMin].getAppealStatus() + " будет замещена на страницу "
                    + virtualMemory.get(index).getId() + " процесса " + id);
            virtualMemory.get(index).setIdOnPhysicalMemory(idMin);
            virtualMemory.get(index).setAppealStatus(initCallSign);
            physicalMemory[idMin] = virtualMemory.get(index);
            System.out.println("\n=======\nСтраничное прерывание обработано\n=======\n");
        }

        for (PageCell pageCell : physicalMemory) {
            if (pageCell != null && pageCell.getAppealStatus() > 0) {
                pageCell.setAppealStatus(pageCell.getAppealStatus() - 1);
            }
        }
    }

    public Process(int id) {
        this.id = id;
        init();
    }

    private void init() {
        virtualMemory = new ArrayList<>();
        int pagesCount = (int) (5 + Math.random() * 5);
        for (int i = 1; i <= pagesCount; i++) {
            virtualMemory.add(new PageCell(i, id));
        }
        System.out.println("Процесс " + id + " требует " + pagesCount + " страниц");
    }
}
