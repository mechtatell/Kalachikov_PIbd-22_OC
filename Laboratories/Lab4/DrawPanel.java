package Lab4;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {

    private final int frameWidth;
    private final int frameHeight;
    private FileSystem fileSystem;

    public DrawPanel(int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, frameWidth, frameHeight);
        if (fileSystem != null) {
            fileSystem.renderTable(g2);
        }
        g2.setColor(Color.black);
        g2.fillRect(458, 0, 4, 700);
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public Sector[] getDiskArray() {
        return fileSystem.getDisc().getSectorsArray();
    }
}
