package Lab3;

public class PageTable {

    private final PageCell[] pageTable;

    public PageTable(int memorySize) {
        pageTable = new PageCell[memorySize / PageCell.getBitCount()];
    }

    public PageCell[] getPageTable() {
        return pageTable;
    }
}
