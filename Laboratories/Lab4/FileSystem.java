package Lab4;

import java.awt.*;
import java.util.Random;

public class FileSystem {

    private final Disc disc;
    private final EmptyClustersList clustersList;

    public FileSystem(Disc disc) {
        this.disc = disc;
        clustersList = new EmptyClustersList(disc);
        memoryAllocation(0);
    }

    public void renderTable(Graphics2D g) {
        int cellSize = 10;
        int margin = 30;
        for (int i = 0; i <= disc.getSectorsArray().length / 40; i++) {
            for (int j = 0; j < 40; j++) {
                if (i * 40 + j >= disc.getSectorsArray().length) {
                    return;
                }
                switch (disc.getSectorsArray()[i * 40 + j].getSectorState()) {
                    case EMPTY -> g.setColor(Color.LIGHT_GRAY);
                    case FILLED -> g.setColor(Color.BLUE);
                    case SELECTED -> g.setColor(Color.RED);
                }
                g.fillRect(margin + j * cellSize, margin + i * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(margin + j * cellSize, margin + i * cellSize, cellSize, cellSize);
            }
        }
    }

    public int memoryAllocation(int size) {
        int cellCount = (int) Math.ceil((double) (size + 1) / (double) disc.getSectorsSize());
        int startSector = -1;
        Random random = new Random();
        Sector prevSector = null;
        if (cellCount > clustersList.getEmptyClustersCount()) {
            return startSector;
        }
        int i = 0;
        while (i < cellCount) {
            int indexCluster = random.nextInt(clustersList.getEmptyClustersCount());
            int index = clustersList.getCluster(indexCluster);
            if (disc.getSectorsArray()[index].getSectorState() == SectorState.EMPTY) {
                disc.getSectorsArray()[index].setSectorState(SectorState.FILLED);
                clustersList.fillCluster(indexCluster);
                if (prevSector != null) {
                    prevSector.setNextSector(index);
                } else {
                    startSector = index;
                }
                prevSector = disc.getSectorsArray()[index];
                i++;
            }
        }
        return startSector;
    }

    public Disc getDisc() {
        return disc;
    }

    public EmptyClustersList getClustersList() {
        return clustersList;
    }
}
