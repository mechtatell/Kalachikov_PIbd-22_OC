package Lab4;

public class Disc {

    private final int size;
    private final int sectorsSize;
    private final Sector[] sectorsArray;

    public Disc(int size, int sectorsSize) {
        this.size = size;
        this.sectorsSize = sectorsSize;
        sectorsArray = getSectorsArray(size / sectorsSize);
    }

    public Sector[] getSectorsArray() {
        return sectorsArray;
    }

    private Sector[] getSectorsArray(int sectorsCount) {
        Sector[] sectorsArray = new Sector[sectorsCount];
        for (int i = 0; i < sectorsCount; i++) {
            sectorsArray[i] = new Sector(SectorState.EMPTY, -1);
        }
        return sectorsArray;
    }

    public int getSize() {
        return size;
    }

    public int getSectorsSize() {
        return sectorsSize;
    }

    public SectorState getSectorState(int index) {
        return sectorsArray[index].getSectorState();
    }
}
