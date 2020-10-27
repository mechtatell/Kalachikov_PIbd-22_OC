package Lab4;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

public class Main {

    private static Frame frame;
    private static DrawPanel drawPanel;
    private static DefaultMutableTreeNode buffer;

    public static void main(String[] args) {
        frame = Frame.getFrame();
        frame.setVisible(true);

        drawPanel = new DrawPanel(frame.getWidth(), frame.getHeight());
        frame.initButtons();

        createDisc();
        frame.initTree();
        ((File) ((DefaultMutableTreeNode) frame.getFileManager().getModel().getRoot()).getUserObject()).setReferenceToCell(drawPanel.getFileSystemMonitor().memoryAllocation(0));

        frame.getContentPane().add(drawPanel);
        drawPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        frame.getButtonCreateCatalog().addActionListener(e -> createCatalog());
        frame.getButtonCreateFile().addActionListener(e -> createFile());
        frame.getFileManager().addTreeSelectionListener(e -> valueChanged());
        frame.getButtonRemove().addActionListener(e -> remove());
        frame.getButtonCopy().addActionListener(e -> copy());
        frame.getButtonPaste().addActionListener(e -> paste());
        frame.getButtonMove().addActionListener(e -> move());
    }

    private static void createDisc() {
        int discSize = 0;
        int discSectorSize = 0;

        JPanel discPanel = MessagePanels.getDiscPanel();
        int result = JOptionPane.showConfirmDialog(frame, discPanel, "Создание диска", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                discSize = Integer.parseInt(((JTextField) discPanel.getComponent(1)).getText());
                discSectorSize = Integer.parseInt(((JTextField) discPanel.getComponent(4)).getText());
                if (discSize <= 0 || discSectorSize <= 0 || discSize / discSectorSize > 2400 || discSize < discSectorSize)
                    throw new Exception();
            } catch (Exception e) {
                int resultError = JOptionPane.showConfirmDialog(frame, "Введите корректные данные", "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                switch (resultError) {
                    case JOptionPane.OK_OPTION -> {
                        createDisc();
                        return;
                    }
                    case JOptionPane.CANCEL_OPTION -> System.exit(0);
                }
            }
        } else {
            int resultError = JOptionPane.showConfirmDialog(frame, "Создание диска обязательно", "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            if (resultError == JOptionPane.OK_OPTION) {
                createDisc();
                return;
            } else {
                System.exit(0);
            }
        }

        Disc disc = new Disc(discSize, discSectorSize);
        drawPanel.setFileSystemMonitor(new FileSystemMonitor(disc));
        frame.repaint();
    }

    private static void createCatalog() {
        String name = null;

        JPanel catalogPanel = MessagePanels.getCatalogPanel();
        int result = JOptionPane.showConfirmDialog(frame, catalogPanel, "Создание каталога", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            name = ((JTextField) catalogPanel.getComponent(1)).getText();
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.getFileManager().getLastSelectedPathComponent();
        if (node != null) {
            File newCatalog = new File(name, 1, -1);
            DefaultMutableTreeNode catalogNode = new DefaultMutableTreeNode(newCatalog, true);
            catalogNode.add(new DefaultMutableTreeNode(""));
            if (((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject().equals("")) {
                node.remove(0);
            }

            node.add(catalogNode);
            newCatalog.setReferenceToCell(drawPanel.getFileSystemMonitor().memoryAllocation(0));
            frame.getFileManager().updateUI();
            valueChanged();
            frame.getFileManager().expandPath(new TreePath(node.getPath()));
            frame.repaint();
        }
    }

    private static void createFile() {
        String name = null;
        int size = 0;

        JPanel filePanel = MessagePanels.getFilePanel();
        int result = JOptionPane.showConfirmDialog(frame, filePanel, "Создание файла", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            name = ((JTextField) filePanel.getComponent(1)).getText();
            size = Integer.parseInt(((JTextField) filePanel.getComponent(4)).getText());
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.getFileManager().getLastSelectedPathComponent();

        if (node != null) {
            File newFile = new File(name, 1, -1);
            DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(newFile, false);
            if (((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject().equals("")) {
                node.remove(0);
            }
            node.add(fileNode);
            newFile.setReferenceToCell(drawPanel.getFileSystemMonitor().memoryAllocation(size));
            frame.getFileManager().updateUI();
            valueChanged();
            frame.getFileManager().expandPath(new TreePath(node.getPath()));
            frame.repaint();
        }
    }

    private static void valueChanged() {
        for (Sector sector : drawPanel.getDiskArray()) {
            if (sector.getSectorState() == SectorState.SELECTED) {
                sector.setSectorState(SectorState.FILLED);
            }
        }

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                frame.getFileManager().getLastSelectedPathComponent();

        if (node == null)
            return;

        File currentFile = (File) node.getUserObject();
        if (node.isLeaf()) {
            markLeaf(currentFile.getReferenceToCell());
        } else {
            markCatalog(node);
        }
        frame.repaint();
    }

    private static void markLeaf(int reference) {
        while (reference != -1) {
            drawPanel.getDiskArray()[reference].setSectorState(SectorState.SELECTED);
            reference = drawPanel.getDiskArray()[reference].getNextSector();
        }
    }

    private static void markCatalog(DefaultMutableTreeNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            drawPanel.getDiskArray()[((File) node.getUserObject()).getReferenceToCell()].setSectorState(SectorState.SELECTED);
            if (((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject().equals("")) break;
            if (node.getChildAt(i).isLeaf()) {
                markLeaf(((File) ((DefaultMutableTreeNode) node.getChildAt(i)).getUserObject()).getReferenceToCell());
            } else {
                markCatalog((DefaultMutableTreeNode) node.getChildAt(i));
            }
        }
    }

    private static void remove() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                frame.getFileManager().getLastSelectedPathComponent();

        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        parent.remove(node);
        if (parent.getChildCount() == 0) {
            parent.add(new DefaultMutableTreeNode(""));
        }

        if (node.isLeaf() || ((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject().equals("")) {
            removeLeaf(((File) (node.getUserObject())).getReferenceToCell());
        } else {
            removeCatalog(node);
        }
        frame.getFileManager().updateUI();
    }

    private static void removeLeaf(int reference) {
        while (reference != -1) {
            drawPanel.getDiskArray()[reference].setSectorState(SectorState.EMPTY);
            int currentReference = reference;
            reference = drawPanel.getDiskArray()[reference].getNextSector();
            drawPanel.getDiskArray()[currentReference].setNextSector(-1);
            frame.repaint();
        }
    }

    private static void removeCatalog(DefaultMutableTreeNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            drawPanel.getDiskArray()[((File) node.getUserObject()).getReferenceToCell()].setSectorState(SectorState.EMPTY);
            if (((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject().equals("")) break;
            if (node.getChildAt(i).isLeaf()) {
                removeLeaf(((File) ((DefaultMutableTreeNode) node.getChildAt(i)).getUserObject()).getReferenceToCell());
            } else {
                removeCatalog((DefaultMutableTreeNode) node.getChildAt(i));
            }
        }
    }

    private static void copy() {
        buffer = (DefaultMutableTreeNode) frame.getFileManager().getLastSelectedPathComponent();
    }

    private static boolean paste() {
        if (buffer != null) {
            DefaultMutableTreeNode newNode = cloneNode(buffer);
            ((DefaultMutableTreeNode) frame.getFileManager().getLastSelectedPathComponent()).add(newNode);
            frame.getFileManager().updateUI();
            return true;
        } else {
            return false;
        }
    }

    private static DefaultMutableTreeNode cloneNode(DefaultMutableTreeNode node) {
        DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) node.clone();
        for (int i = 0; i < node.getChildCount(); i++) {
            if (node.getChildAt(i).isLeaf()) {
                newNode.add((MutableTreeNode) ((DefaultMutableTreeNode) node.getChildAt(i)).clone());
            } else {
                newNode.add(cloneNode((DefaultMutableTreeNode) node.getChildAt(i)));
            }
        }
        return newNode;
    }

    private static void move() {
        DefaultMutableTreeNode buffer = cloneNode((DefaultMutableTreeNode) frame.getFileManager().getLastSelectedPathComponent());
        ((DefaultMutableTreeNode)((DefaultMutableTreeNode) frame.getFileManager().getLastSelectedPathComponent()).getParent()).remove((MutableTreeNode) frame.getFileManager().getLastSelectedPathComponent());
        frame.getFileManager().updateUI();
        JTree tree = new JTree((DefaultMutableTreeNode)frame.getFileManager().getModel().getRoot());
        int result = JOptionPane.showConfirmDialog(frame, tree);
        if (result == JOptionPane.OK_OPTION) {
            ((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).add(buffer);
            frame.getFileManager().updateUI();
        }

    }
}
