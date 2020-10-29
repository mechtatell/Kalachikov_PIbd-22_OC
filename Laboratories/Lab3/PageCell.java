package Lab3;

public class PageCell {

    private static final int bitCount = 4;
    private final int id;
    private final int processId;
    private int idOnPhysicalMemory;
    private int appealStatus;

    public PageCell(int id, int processId) {
        idOnPhysicalMemory = -1;
        this.id = id;
        this.processId = processId;
    }

    public int getId() {
        return id;
    }


    public int getIdOnPhysicalMemory() {
        return idOnPhysicalMemory;
    }

    public void setIdOnPhysicalMemory(int idOnPhysicalMemory) {
        this.idOnPhysicalMemory = idOnPhysicalMemory;
    }

    public int getAppealStatus() {
        return appealStatus;
    }

    public void setAppealStatus(int appealStatus) {
        this.appealStatus = appealStatus;
    }

    public int getProcessId() {
        return processId;
    }

    public static int getBitCount() {
        return bitCount;
    }
}
