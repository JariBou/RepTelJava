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

public class mainGUI extends JFrame {
    private JTextField textField1; // Input
    public JLabel label1; // Main Display
    private JPanel rootPanel;
    public JLabel label2; // Secondary Display
    private JTextArea notes; // Notes field
    private JLabel notesLabel;
    private JPanel menuPanel;
    private JSlider sliderSize; // Slider that changes font size
    public JLabel consoleOutput; // Console Display
    public JButton button; // Button
    private JButton exportNotes;
    private JComboBox<String> choiceBox;
    public String input; // Value of the Input Display
    public boolean pressed;
    public boolean clicked;


    Write w = new Write();

    Font defaultFont = new Font("Arial", Font.PLAIN, 25);
    public Font setFontSize(int size){ // Changes the font size, used for button
        return new Font("Arial", Font.PLAIN, size);
    }
    public boolean isPressed() { // Returns a boolean of the state of the enter key
        return pressed;
    } public void setPressed(boolean state){ // Sets the boolean state of the enter key
        this.pressed = state;
    }

    public String setChoiceBox(ArrayList<String> list) throws InterruptedException {
        return setChoiceBox(list, null, Color.green);
    }

    public void setConsoleOutput(String text, Color color){
        consoleOutput.setForeground(color);
        consoleOutput.setText(text);
    }


    public String setChoiceBox(ArrayList<String> list, String message, Color color) throws InterruptedException {
        choiceBox.setVisible(true);
        for (String s : list){
            choiceBox.addItem(s);
        }
        consoleOutput.setForeground(color);
        consoleOutput.setText(Objects.requireNonNullElse(message, "> Select your choice, then click the 'ok' button"));
        button.setVisible(true);
        while (!clicked) {
            //noinspection BusyWait
            Thread.sleep(100);
        } clicked = false;
        int choice = choiceBox.getSelectedIndex();
        choiceBox.setVisible(false);
        button.setVisible(false);
        choiceBox.removeAllItems();
        return list.get(choice);

    }

    public mainGUI(){
        //label1.setHorizontalTextPosition(JLabel.LEFT);
        button.setVisible(false);
        notesLabel.setVisible(true);
        menuPanel.setVisible(true);
        choiceBox.setVisible(false);
        label1.setFont(defaultFont);
        consoleOutput.setBackground(Color.black);
        add(rootPanel);
        setTitle("RepTel.java");
        setSize(800, 1000);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { // Detects if a key is pressed
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER){ // Detects if the key pressed is Enter
                    setPressed(true);
                    input = textField1.getText();
                    if (input.equals("close")){
                        System.exit(1);
                    }
                    textField1.setText("");
                }
            }
        }); sliderSize.addChangeListener(e -> { // Detects if the slider state changed
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                int size = source.getValue();
                label1.setFont(setFontSize(size)); // sets the font size according to the value of the slider

            }
        }); button.addActionListener(e -> clicked = true); // Button clicked detection
        exportNotes.addActionListener(e -> { // Exports notes to .txt File
            String content = notes.getText();
            JFrame parentFrame = new JFrame();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            int userSelection = fileChooser.showSaveDialog(parentFrame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                Path path = Path.of(fileToSave.getAbsolutePath());
                w.create("", path);
                try {
                    String path2 = (path + ".txt").replace("\\", "\\\\");
                    FileWriter myWriterGUI = new FileWriter(path2, false);
                    myWriterGUI.write(content);
                    myWriterGUI.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }
}
