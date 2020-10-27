package Lab4;

public class File {
    private String name;
    private int size;
    private int referenceToCell;

    public File(String name, int size, int referenceToCell) {
        this.name = name;
        this.size = size;
        this.referenceToCell = referenceToCell;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
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
