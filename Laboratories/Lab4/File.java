package Lab4;

import java.util.LinkedList;
import java.util.List;

public class File {
    private final String name;
    private int size;
    private int referenceToCell;
    private Disc disc;

    public File(String name, int size, int referenceToCell, Disc disc) {
        this.name = name;
        this.size = size;
        this.referenceToCell = referenceToCell;
        this.disc = disc;
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

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public List<Sector> getSectorsList() {
        List<Sector> sectorsList = new LinkedList<>();
        int reference = referenceToCell;
        while (reference != -1) {
            sectorsList.add(disc.getSectorsArray()[reference]);
            reference = disc.getSectorsArray()[reference].getNextSector();
        }
        return sectorsList;
    }
}
