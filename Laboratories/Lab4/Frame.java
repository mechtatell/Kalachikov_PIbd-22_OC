package Lab4;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class Frame extends JFrame {

    private JButton buttonCreateFile;
    private JButton buttonCreateCatalog;
    private JButton buttonRemove;
    private JButton buttonCopy;
    private JButton buttonPaste;
    private JButton buttonMove;
    private JButton buttonShowInfo;
    private JTree fileManager;

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

    public void initButtons() {
        buttonCreateCatalog = new JButton("Создать каталог");
        buttonCreateFile = new JButton("Создать файл");
        buttonRemove = new JButton("Удалить");
        buttonCopy = new JButton("Копировать");
        buttonPaste = new JButton("Вставить");
        buttonMove = new JButton("Переместить");
        buttonShowInfo = new JButton("Документация");

        getContentPane().add(buttonCreateFile);
        getContentPane().add(buttonCreateCatalog);
        getContentPane().add(buttonRemove);
        getContentPane().add(buttonCopy);
        getContentPane().add(buttonPaste);
        getContentPane().add(buttonMove);
        getContentPane().add(buttonShowInfo);

        buttonCreateFile.setBounds(955, 20, 200, 30);
        buttonCreateCatalog.setBounds(955, 60, 200, 30);
        buttonRemove.setBounds(955, 100, 200, 30);
        buttonCopy.setBounds(955, 140, 95, 30);
        buttonPaste.setBounds(1060, 140, 95, 30);
        buttonMove.setBounds(955, 180, 200, 30);
        buttonShowInfo.setBounds(955, 220, 200, 30);
    }

    public void initTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new File("root", 1, 1), true);
        fileManager = new JTree(root);
        root.add(new DefaultMutableTreeNode(""));

        getContentPane().add(fileManager);
        fileManager.setBounds(500, 30, 300, 650);
        repaint();
    }

    public JButton getButtonCreateFile() {
        return buttonCreateFile;
    }

    public JButton getButtonCreateCatalog() {
        return buttonCreateCatalog;
    }

    public JButton getButtonRemove() {
        return buttonRemove;
    }

    public JButton getButtonCopy() {
        return buttonCopy;
    }

    public JButton getButtonPaste() {
        return buttonPaste;
    }

    public JButton getButtonMove() {
        return buttonMove;
    }

    public JButton getButtonShowInfo() {
        return buttonShowInfo;
    }

    public JTree getFileManager() {
        return fileManager;
    }
}
