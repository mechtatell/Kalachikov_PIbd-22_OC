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
        frame.setSize(790, 700);
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
        JButton buttonShowInfo = new JButton("Инфо");

        getContentPane().add(buttonCreateFile);
        getContentPane().add(buttonCreateCatalog);
        getContentPane().add(buttonRemove);
        getContentPane().add(buttonCopy);
        getContentPane().add(buttonPaste);
        getContentPane().add(buttonMove);
        getContentPane().add(buttonShowInfo);

        buttonCreateFile.setBounds(500, 360, 240, 30);
        buttonCreateCatalog.setBounds(500, 400, 240, 30);
        buttonRemove.setBounds(500, 440, 240, 30);
        buttonCopy.setBounds(500, 480, 115, 30);
        buttonPaste.setBounds(625, 480, 115, 30);
        buttonMove.setBounds(500, 520, 240, 30);
        buttonShowInfo.setBounds(500, 560, 240, 30);

        buttonCreateCatalog.addActionListener(e -> fileManager.createCatalog());
        buttonCreateFile.addActionListener(e -> fileManager.createFile());
        buttonRemove.addActionListener(e -> fileManager.remove());
        buttonCopy.addActionListener(e -> fileManager.copy());
        buttonPaste.addActionListener(e -> fileManager.paste());
        buttonMove.addActionListener(e -> fileManager.move());
        buttonShowInfo.addActionListener(e -> showInfo());
    }

    public void initTree(FileManager fileManager, FileSystem fileSystem) {
        File rootCatalog = new File("root", 0, -1, fileSystem.getDisc());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootCatalog, true);
        rootCatalog.setReferenceToCell(fileSystem.memoryAllocation(0));
        fileManagerTree = new JTree(root);
        root.add(new DefaultMutableTreeNode(new File("", -1, -1, fileSystem.getDisc())));

        JScrollPane scrollPane = new JScrollPane(fileManagerTree);
        getContentPane().add(scrollPane);
        scrollPane.setBounds(500, 30, 240, 300);
        fileManagerTree.addTreeSelectionListener(e -> fileManager.valueChanged());
        repaint();
    }

    public JTree getFileManagerTree() {
        return fileManagerTree;
    }

    public void showInfo() {
        JOptionPane.showMessageDialog(this, "Здравствуйте! Реализовывал алгоритм размещения с" +
                " использованием связного списка,\n каждый сектор(Sector) имеет ссылку(индекс) на следующий сектор(изначально -1). " +
                "При выделении памяти берется рандомная ячейка\n в списке свободных кластеров, кластер занимается и в предыдущий кластер записывается " +
                "индекс нового кластера.\n\nИзначально в диске создается корневой каталог root, и каждый каталог занимает 1 условную единицу веса.\n" +
                "Так же одна ячейка выделяется на список свободных кластеров, она будет всегда синяя. При создании диска может получиться\nтолько 2400 " +
                "секторов, иначе обработается исключение. При создании файла к его размеру добавляется одна единица веса для\nхранения атрибутов. В каждом пустом каталоге находится пустой файл, " +
                "чтобы пустые каталоги отображались как каталоги, а не как файлы.\nКак только в каталоге появляются другие файлы, пустой файл удаляется.\n\n" +
                "Постарался обработать все возможные исключения, приложение должно работать стабильно", "Инфо", JOptionPane.PLAIN_MESSAGE);
    }
}
