package Lab4;

import java.util.ArrayList;

public class EmptyClustersList {

    private final ArrayList<Integer> emptyClustersList;

    public EmptyClustersList(Disc disc) {
        this.emptyClustersList = new ArrayList<>();
        for (int i = 0; i < disc.getSectorsArray().length; i++) {
            if (disc.getSectorsArray()[i].getSectorState() == SectorState.EMPTY) {
                emptyClustersList.add(i);
            }
        }
    }

    public int getEmptyClustersCount() {
        return emptyClustersList.size();
    }

    public void fillCluster(int value) {
        emptyClustersList.remove(value);
    }

    public void freeCluster(int value) {emptyClustersList.add(value); }

    public int getCluster(int index) {
        return emptyClustersList.get(index);
    }
}
