package Lab4;

import javax.swing.*;
import java.awt.*;

public class MessagePanels {

    public static JPanel getDiscPanel() {
        JPanel panelCreateDisc = new JPanel();
        JLabel labelSize = new JLabel("Введите размер дискового раздела");
        JLabel labelSectorsSize = new JLabel("Введите размер сектора диска");
        JTextField textFieldSize = new JTextField("2400", 10);
        JTextField textFieldSectorsSize = new JTextField("1", 10);
        panelCreateDisc.setLayout(new GridLayout(5, 1));
        panelCreateDisc.add(labelSize);
        panelCreateDisc.add(textFieldSize);
        panelCreateDisc.add(Box.createVerticalStrut(10));
        panelCreateDisc.add(labelSectorsSize);
        panelCreateDisc.add(textFieldSectorsSize);
        return panelCreateDisc;
    }

    public static JPanel getCatalogPanel() {
        JPanel panelCreateCatalog = new JPanel();
        JLabel labelName = new JLabel("Введите имя нового каталога");
        JTextField textFieldName = new JTextField(10);
        panelCreateCatalog.setLayout(new GridLayout(2, 1));
        panelCreateCatalog.add(labelName);
        panelCreateCatalog.add(textFieldName);
        return panelCreateCatalog;
    }

    public static JPanel getFilePanel() {
        JPanel panelCreateFile = new JPanel();
        JLabel labelName = new JLabel("Введите имя нового файла");
        JLabel labelSize = new JLabel("Введите размер нового файла");
        JTextField textFieldName = new JTextField(10);
        JTextField textFieldSize = new JTextField(10);
        panelCreateFile.setLayout(new GridLayout(5, 1));
        panelCreateFile.add(labelName);
        panelCreateFile.add(textFieldName);
        panelCreateFile.add(Box.createVerticalStrut(10));
        panelCreateFile.add(labelSize);
        panelCreateFile.add(textFieldSize);
        return panelCreateFile;
    }
}
