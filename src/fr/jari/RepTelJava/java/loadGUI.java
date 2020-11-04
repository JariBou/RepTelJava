package fr.jari.RepTelJava.java;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class loadGUI extends JFrame{
    private JProgressBar progressBar1;
    private JPanel panel1;
    private JLabel loading;

    public loadGUI() throws IOException {

        BufferedImage myPicture = ImageIO.read(new File("icons/mainIcon.png"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        picLabel.setVisible(true);
        add(picLabel);
        loading.setText("Loading... Please wait...");
        loading.setVisible(true);
        add(loading);

        progressBar1 = new JProgressBar(0, 100);
        progressBar1.setVisible(true);
        progressBar1.setValue(50);
        add(progressBar1);


        setSize(700, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);


    }

}
