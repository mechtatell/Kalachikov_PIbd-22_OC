package Lab4;

public class File {
    private final String name;
    private final int size;
    private int referenceToCell;

    public File(String name, int size, int referenceToCell) {
        this.name = name;
        this.size = size;
        this.referenceToCell = referenceToCell;
    }

    public int getReferenceToCell() {
        return referenceToCell;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setReferenceToCell(int referenceToCell) {
        this.referenceToCell = referenceToCell;
    }
}
