package Lab4;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class Frame extends JFrame {

    private JTree fileManagerTree;

    private Frame(String name) {
        super(name);
    }

    public static Frame getFrame() {
        Frame frame = new Frame("Файловый менеджер");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        return frame;
    }

    public void initButtons(FileManager fileManager) {
        JButton buttonCreateCatalog = new JButton("Создать каталог");
        JButton buttonCreateFile = new JButton("Создать файл");
        JButton buttonRemove = new JButton("Удалить");
        JButton buttonCopy = new JButton("Копировать");
        JButton buttonPaste = new JButton("Вставить");
        JButton buttonMove = new JButton("Переместить");
        JButton buttonShowInfo = new JButton("Документация");

        getContentPane().add(buttonCreateFile);
        getContentPane().add(buttonCreateCatalog);
        getContentPane().add(buttonRemove);
        getContentPane().add(buttonCopy);
        getContentPane().add(buttonPaste);
        getContentPane().add(buttonMove);
        getContentPane().add(buttonShowInfo);

        buttonCreateFile.setBounds(915, 200, 240, 30);
        buttonCreateCatalog.setBounds(915, 240, 240, 30);
        buttonRemove.setBounds(915, 280, 240, 30);
        buttonCopy.setBounds(915, 320, 115, 30);
        buttonPaste.setBounds(1040, 320, 115, 30);
        buttonMove.setBounds(915, 360, 240, 30);
        buttonShowInfo.setBounds(915, 400, 240, 30);

        buttonCreateCatalog.addActionListener(e -> fileManager.createCatalog());
        buttonCreateFile.addActionListener(e -> fileManager.createFile());
        buttonRemove.addActionListener(e -> fileManager.remove());
        buttonCopy.addActionListener(e -> fileManager.copy());
        buttonPaste.addActionListener(e -> fileManager.paste());
        buttonMove.addActionListener(e -> fileManager.move());
    }

    public void initTree(FileManager fileManager, FileSystem fileSystem) {
        File rootCatalog = new File("root", 0, -1);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootCatalog, true);
        rootCatalog.setReferenceToCell(fileSystem.memoryAllocation(0));
        fileManagerTree = new JTree(root);
        root.add(new DefaultMutableTreeNode(""));

        getContentPane().add(fileManagerTree);
        fileManagerTree.setBounds(500, 30, 300, 650);
        fileManagerTree.addTreeSelectionListener(e -> fileManager.valueChanged());
        repaint();
    }

    public JTree getFileManagerTree() {
        return fileManagerTree;
    }
}
