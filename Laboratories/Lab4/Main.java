package Lab4;

public class Main {

    public static void main(String[] args) {

        //Создание окна приложения
        Frame frame = Frame.getFrame();
        frame.setVisible(true);
        frame.showInfo();

        //Инициализация панели отрисовки для файловой системы
        DrawPanel drawPanel = new DrawPanel(frame.getWidth(), frame.getHeight());

        //Создание файлового менеджера для работы дерева
        FileManager fileManager = new FileManager(frame, drawPanel);
        frame.initButtons(fileManager);

        //Создание диска
        fileManager.createDisc();
        frame.initTree(fileManager, drawPanel.getFileSystem());

        //Добавление отрисовки таблицы файловой системы на окно приложения
        frame.getContentPane().add(drawPanel);
        drawPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.repaint();
    }
}
