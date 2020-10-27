package Lab4;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

public class FileManager {

    private final Frame frame;
    private final DrawPanel drawPanel;
    private DefaultMutableTreeNode buffer;

    public FileManager(Frame frame, DrawPanel drawPanel) {
        this.frame = frame;
        this.drawPanel = drawPanel;
        buffer = null;
    }

    public void createDisc() {
        int discSize = 0;
        int discSectorSize = 0;

        JPanel discPanel = MessagePanels.getDiscPanel();
        int result = JOptionPane.showConfirmDialog(frame, discPanel, "Создание диска",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                discSize = Integer.parseInt(((JTextField) discPanel.getComponent(1)).getText());
                discSectorSize = Integer.parseInt(((JTextField) discPanel.getComponent(4)).getText());
                if (discSize <= 0 || discSectorSize <= 0 || discSize / discSectorSize > 2400 || discSize < discSectorSize)
                    throw new Exception();
            } catch (Exception e) {
                int resultError = JOptionPane.showConfirmDialog(frame, "Введите корректные данные", "Ошибка",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                switch (resultError) {
                    case JOptionPane.OK_OPTION:
                        createDisc();
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        System.exit(0);
                }
            }
        } else {
            int resultError = JOptionPane.showConfirmDialog(frame, "Создание диска обязательно", "Ошибка",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            if (resultError == JOptionPane.OK_OPTION) {
                createDisc();
                return;
            } else {
                System.exit(0);
            }
        }

        Disc disc = new Disc(discSize, discSectorSize);
        drawPanel.setFileSystem(new FileSystem(disc));
        frame.repaint();
    }

    public void createCatalog() {
        String name;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.getFileManagerTree().getLastSelectedPathComponent();
        if (node == null || !node.getAllowsChildren()) {
            JOptionPane.showConfirmDialog(frame, "Выберите каталог, где требуется создать новый каталог",
                    "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel catalogPanel = MessagePanels.getCatalogPanel();
        int result = JOptionPane.showConfirmDialog(frame, catalogPanel, "Создание каталога",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            name = ((JTextField) catalogPanel.getComponent(1)).getText();
            if (name.equals("")) {
                int resultError = JOptionPane.showConfirmDialog(frame, "Введите корректные данные",
                        "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                if (resultError == JOptionPane.OK_OPTION) {
                    createCatalog();
                }
                return;
            }
        } else {
            return;
        }

        File newCatalog = new File(name, 1, -1);
        DefaultMutableTreeNode catalogNode = new DefaultMutableTreeNode(newCatalog, true);
        catalogNode.add(new DefaultMutableTreeNode(""));
        if (((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject().equals("")) {
            node.remove(0);
        }

        int reference = drawPanel.getFileSystem().memoryAllocation(0);
        if (reference == -1) {
            JOptionPane.showConfirmDialog(frame, "На диске нет места",
                    "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        node.add(catalogNode);
        newCatalog.setReferenceToCell(reference);
        frame.getFileManagerTree().updateUI();
        valueChanged();
        frame.getFileManagerTree().expandPath(new TreePath(node.getPath()));
        frame.repaint();
    }

    public void createFile() {
        String name;
        int size;

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.getFileManagerTree().getLastSelectedPathComponent();
        if (node == null || !node.getAllowsChildren()) {
            JOptionPane.showConfirmDialog(frame, "Выберите каталог, где требуется создать новый файл",
                    "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel filePanel = MessagePanels.getFilePanel();
        int result = JOptionPane.showConfirmDialog(frame, filePanel, "Создание файла",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                name = ((JTextField) filePanel.getComponent(1)).getText();
                size = Integer.parseInt(((JTextField) filePanel.getComponent(4)).getText());
                if (name.equals("") || size < 0 || size > drawPanel.getFileSystem().getDisc().getSize())
                    throw new Exception();
            } catch (Exception e) {
                int resultError = JOptionPane.showConfirmDialog(frame, "Введите корректные данные",
                        "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                if (resultError == JOptionPane.OK_OPTION) {
                    createCatalog();
                }
                return;
            }
        } else {
            return;
        }

        File newFile = new File(name, size, -1);
        DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(newFile, false);
        if (((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject().equals("")) {
            node.remove(0);
        }

        int reference = drawPanel.getFileSystem().memoryAllocation(size);
        if (reference == -1) {
            JOptionPane.showConfirmDialog(frame, "На диске нет места",
                    "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        node.add(fileNode);
        newFile.setReferenceToCell(reference);
        frame.getFileManagerTree().updateUI();
        valueChanged();
        frame.getFileManagerTree().expandPath(new TreePath(node.getPath()));
        frame.repaint();
    }

    public void valueChanged() {
        for (Sector sector : drawPanel.getDiskArray()) {
            if (sector.getSectorState() == SectorState.SELECTED) {
                sector.setSectorState(SectorState.FILLED);
            }
        }

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                frame.getFileManagerTree().getLastSelectedPathComponent();

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

    private void markLeaf(int reference) {
        while (reference != -1) {
            drawPanel.getDiskArray()[reference].setSectorState(SectorState.SELECTED);
            reference = drawPanel.getDiskArray()[reference].getNextSector();
        }
    }

    private void markCatalog(DefaultMutableTreeNode node) {
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

    public void remove() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                frame.getFileManagerTree().getLastSelectedPathComponent();

        if (node == null || node.getParent() == null) {
            JOptionPane.showConfirmDialog(frame, "Выберите файл, который хотите удалить",
                    "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(frame, "Вы действительно хотите удалить файл " + node.getUserObject() + "?",
                "Удаление файла", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
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
            frame.getFileManagerTree().updateUI();
        }
    }

    private void removeLeaf(int reference) {
        while (reference != -1) {
            drawPanel.getDiskArray()[reference].setSectorState(SectorState.EMPTY);
            drawPanel.getFileSystem().getClustersList().freeCluster(reference);
            int currentReference = reference;
            reference = drawPanel.getDiskArray()[reference].getNextSector();
            drawPanel.getDiskArray()[currentReference].setNextSector(-1);
            frame.repaint();
        }
    }

    private void removeCatalog(DefaultMutableTreeNode node) {
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

    public void copy() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.getFileManagerTree().getLastSelectedPathComponent();

        if (node == null || node.getParent() == null) {
            JOptionPane.showConfirmDialog(frame, "Выберите файл, который хотите скопировать",
                    "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }

        buffer = node;
        JOptionPane.showMessageDialog(frame, "Файл " + node.getUserObject() + " скопирован!",
                "Инфо", JOptionPane.INFORMATION_MESSAGE);
    }

    public void paste() {
        if (buffer != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.getFileManagerTree().getLastSelectedPathComponent();

            if (node == null || !node.getAllowsChildren()) {
                JOptionPane.showConfirmDialog(frame, "Выберите каталог, куда желаете вставить файл",
                        "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode newNode = cloneNode(buffer);
            if (((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject().equals("")) {
                node.remove(0);
            }
            node.add(newNode);
            frame.getFileManagerTree().updateUI();
        } else {
            JOptionPane.showConfirmDialog(frame, "Буфер обмена пуст",
                    "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

    private DefaultMutableTreeNode cloneNode(DefaultMutableTreeNode node) {
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

    public void move() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.getFileManagerTree().getLastSelectedPathComponent();

        if (node == null) {
            JOptionPane.showConfirmDialog(frame, "Выберите файл, который хотите переместить",
                    "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultMutableTreeNode buffer = cloneNode((DefaultMutableTreeNode) frame.getFileManagerTree().getLastSelectedPathComponent());
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        parent.remove(node);
        frame.getFileManagerTree().updateUI();

        JTree tree = new JTree((DefaultMutableTreeNode) frame.getFileManagerTree().getModel().getRoot());
        int result = JOptionPane.showConfirmDialog(frame, tree);

        if (result == JOptionPane.OK_OPTION) {
            DefaultMutableTreeNode catalog = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (catalog == null || !catalog.getAllowsChildren()) {
                JOptionPane.showConfirmDialog(frame, "Выбран некорректный файл, попробуйте еще раз",
                        "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (((DefaultMutableTreeNode) catalog.getChildAt(0)).getUserObject().equals("")) {
                catalog.remove(0);
            }
            catalog.add(buffer);
            frame.getFileManagerTree().updateUI();
        }
    }
}
