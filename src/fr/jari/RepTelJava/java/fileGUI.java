package fr.jari.RepTelJava.java;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;


public class fileGUI extends JFrame {
    private JPanel mainPanel;
    public JLabel display;
    private JScrollPane scroll;

    Font defaultFont = new Font("Arial", Font.PLAIN, 25);

    public fileGUI(String filename) {
        display.setFont(defaultFont);
        display.setVisible(true);
        Path currentRelativePath = Paths.get("");
        String imPath = currentRelativePath.toAbsolutePath().toString();
        ImageIcon icon = new ImageIcon(imPath + "/icons/mainIcon.png");
        setIconImage(icon.getImage());
        setTitle("RepTel.java");
        add(mainPanel);
        scroll.setViewportView(display);// Set the viewport view only when the panel has been initialized
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        getContentPane().add(scroll, BorderLayout.CENTER);// Add the scrollpane, not the panel
        setTitle(filename);
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
