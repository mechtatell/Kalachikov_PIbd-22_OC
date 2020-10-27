package Lab4;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
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
        catalogNode.add(new DefaultMutableTreeNode(new File("", -1, -1)));
        if (((File)(((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject())).getName().equals("")) {
            node.remove(0);
        }

        int reference = drawPanel.getFileSystem().memoryAllocation(0);
        if (reference == -1) {
            JOptionPane.showConfirmDialog(frame, "На диске нет места",
                    "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        node.add(catalogNode);
        File parent = (File) node.getUserObject();
        parent.setSize(parent.getSize() + 1);
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
        if (((File)(((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject())).getName().equals("")) {
            node.remove(0);
        }

        int reference = drawPanel.getFileSystem().memoryAllocation(size);
        if (reference == -1) {
            JOptionPane.showConfirmDialog(frame, "На диске нет места",
                    "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        node.add(fileNode);
        File parent = (File) node.getUserObject();
        parent.setSize(parent.getSize() + size + 1);
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
            if (((File)(((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject())).getName().equals("")) {
                break;
            }
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
            File parentFile = (File) parent.getUserObject();
            parentFile.setSize(parentFile.getSize() - ((File) node.getUserObject()).getSize() - 1);
            parent.remove(node);
            if (parent.getChildCount() == 0) {
                parent.add(new DefaultMutableTreeNode(new File("", -1, -1)));
            }

            if (node.isLeaf() || ((File)(((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject())).getName().equals("")) {
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
            if (((File)(((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject())).getName().equals("")) {
                break;
            }
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
            if (((File)(((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject())).getName().equals("")) {
                node.remove(0);
            }

            File newFile = (File) newNode.getUserObject();
            if (newNode.isLeaf()) {
                newFile.setReferenceToCell(drawPanel.getFileSystem().memoryAllocation(newFile.getSize()));
            } else {
                allocateCatalog(newNode);
            }
            if (newFile.getReferenceToCell() == -1) {
                JOptionPane.showConfirmDialog(frame, "На диске нет места",
                        "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            } else {
                newNode.setUserObject(newFile);
                node.add(newNode);
                frame.getFileManagerTree().updateUI();
                File parentFile = (File) node.getUserObject();
                parentFile.setSize(parentFile.getSize() + newFile.getSize() + 1);
                valueChanged();
                frame.repaint();
            }
        } else {
            JOptionPane.showConfirmDialog(frame, "Буфер обмена пуст",
                    "Ошибка", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

    private DefaultMutableTreeNode cloneNode(DefaultMutableTreeNode node) {
        File nodeFile = (File) node.getUserObject();
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new File(node.toString(), nodeFile.getSize(), nodeFile.getReferenceToCell()));
        for (int i = 0; i < node.getChildCount(); i++) {
            if (node.getChildAt(i).isLeaf()) {
                DefaultMutableTreeNode nodeChild = (DefaultMutableTreeNode) node.getChildAt(i);
                File file = (File) nodeChild.getUserObject();
                newNode.add(new DefaultMutableTreeNode(new File(nodeChild.toString(), file.getSize(), file.getReferenceToCell())));
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
            if (((File)(((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject())).getName().equals("")) {
                catalog.remove(0);
            }
            catalog.add(buffer);
            frame.getFileManagerTree().updateUI();
        }
    }

    private void allocateCatalog(DefaultMutableTreeNode node) {
        File catalog = (File) node.getUserObject();
        catalog.setReferenceToCell(drawPanel.getFileSystem().memoryAllocation(0));
        node.setUserObject(catalog);
        for (int i = 0; i < node.getChildCount(); i++) {
            if (node.getChildAt(i).isLeaf()) {
                DefaultMutableTreeNode nodeChild = (DefaultMutableTreeNode) node.getChildAt(i);
                File file = (File) nodeChild.getUserObject();
                file.setReferenceToCell(drawPanel.getFileSystem().memoryAllocation(file.getSize()));
                nodeChild.setUserObject(file);
            } else {
                allocateCatalog((DefaultMutableTreeNode) node.getChildAt(i));
            }
        }
    }
}
