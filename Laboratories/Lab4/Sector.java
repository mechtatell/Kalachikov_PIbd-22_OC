package Lab4;

public class Sector {

    private SectorState sectorState;
    private int nextSector;

    public Sector(SectorState sectorState, int nextSector) {
        this.sectorState = sectorState;
        this.nextSector = nextSector;
    }

    public SectorState getSectorState() {
        return sectorState;
    }

    public int getNextSector() {
        return nextSector;
    }

    public void setSectorState(SectorState sectorState) {
        this.sectorState = sectorState;
    }

    public void setNextSector(int nextSector) {
        this.nextSector = nextSector;
    }
}
