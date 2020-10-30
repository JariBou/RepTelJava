package fr.jari.RepTelJava.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class startGui extends JFrame{
    private JComboBox<String> languageChoice;
    private JPanel panel1;
    private JButton confirm;
    private JLabel label;
    private boolean clicked;




    public String setChoiceBox() throws InterruptedException {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("English", "Français"));
        for (String s : list){
            languageChoice.addItem(s);
        }
        confirm.setVisible(true);
        while (!clicked) {
            //noinspection BusyWait
            Thread.sleep(100);
        } clicked = false;
        int choice = languageChoice.getSelectedIndex();
        languageChoice.removeAllItems();
        return list.get(choice);
    }





    public startGui(){

        Path currentRelativePath = Paths.get("");
        String imPath = currentRelativePath.toAbsolutePath().toString();
        ImageIcon icon = new ImageIcon(imPath + "/icons/mainIcon.png");
        setIconImage(icon.getImage());
        label.setText("Choose the language");
        confirm.setText("Confirm");
        add(panel1);
        setTitle("RepTel.java");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        label.setVisible(true);
        confirm.setVisible(true);
        languageChoice.setVisible(true);
        panel1.setVisible(true);
        confirm.addActionListener(e -> clicked = true);


    }


}
