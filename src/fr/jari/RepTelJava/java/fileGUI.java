package fr.jari.RepTelJava.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class fileGUI extends JFrame{
    private JPanel mainPanel;
    private JSlider sliderSize;
    private JLabel sizeLabel;
    public JTextArea textArea;

    JScrollPane scroll = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    Font defaultFont = new Font("Arial", Font.PLAIN, 25);
    public Font setFontSize(int size){ // Changes the font size, used for button
        return new Font("Arial", Font.PLAIN, size);
    }

    public fileGUI(String filename){
        textArea.setFont(defaultFont);
        textArea.setVisible(true);
        sizeLabel.setVisible(true);
        //scroll.setVisible(true);
        add(mainPanel);
        setTitle(filename);
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



        sliderSize.addChangeListener(e -> { // Detects if the slider state changed
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                int size = source.getValue();
                textArea.setFont(setFontSize(size)); // sets the font size according to the value of the slider

            }
        });



    }
}
