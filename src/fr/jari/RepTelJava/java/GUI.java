package fr.jari.RepTelJava.java;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

public class GUI extends JFrame {
    private JTextField textField1;
    public JLabel label1;
    private JPanel rootPanel;
    public JLabel label2;
    private JTextArea notes;
    private JLabel notesLabel;
    private JPanel menuPanel;
    private JSlider sliderSize;
    public JLabel consoleOutput;
    public JButton button;
    public String input;
    public boolean pressed;
    public boolean clicked;



    Font defaultFont = new Font("Arial", Font.PLAIN, 25);
    public Font setFontSize(int size){
        return new Font("Arial", Font.PLAIN, size);
    }

    public boolean isPressed() {
        return pressed;
    } public void setPressed(boolean state){
        this.pressed = state;
    }

    public GUI(){
        //label1.setHorizontalTextPosition(JLabel.LEFT);
        button.setVisible(false);
        clicked = false;
        label1.setFont(defaultFont);
        consoleOutput.setBackground(Color.black);
        add(rootPanel);
        setTitle("RepTel.java");
        setSize(800, 1000);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    setPressed(true);
                    input = textField1.getText();
                    textField1.setText("");
                }
            }

        }); sliderSize.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                int size = source.getValue();
                label1.setFont(setFontSize(size));

            }
        }); button.addActionListener(e -> clicked = true);
    }
}
