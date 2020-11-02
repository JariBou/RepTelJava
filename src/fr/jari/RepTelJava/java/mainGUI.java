package fr.jari.RepTelJava.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class mainGUI extends JFrame{
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
    public JButton exportNotes;
    private JComboBox<String> choiceBox;
    public JLabel fontSizeLabel;
    private JLabel consoleLabel;
    public String input; // Value of the Input Display
    public boolean pressed;
    public boolean clicked;
    Color GREY = Color.darkGray;
    Color GOLD = Color.orange;
    Color WHITE = Color.white;
    Color BLACK = Color.black;

    JMenuBar menuBar;
    JMenu settings;
    JMenu theme;
    JMenuItem dark, light, about;




    WriteEng w = new WriteEng();

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
        return setChoiceBox(list, null, Color.green, true);
    } public String setChoiceBox(ArrayList<String> list, boolean display) throws InterruptedException {
        return setChoiceBox(list, null, Color.green, display);
    } public String setChoiceBox(ArrayList<String> list, String message, Color color) throws InterruptedException {
        return setChoiceBox(list, message, color, true);
    }

    public void setConsoleOutput(String text, Color color){
        consoleOutput.setForeground(color);
        consoleOutput.setText(text);
    }

    public String setChoiceBox(ArrayList<String> list, String message, Color color, boolean display) throws InterruptedException {
        choiceBox.setVisible(true);
        for (String s : list){
            choiceBox.addItem(s);
        }
        consoleOutput.setForeground(color);
        if (display) {
            consoleOutput.setText(Objects.requireNonNullElse(message, "> Select your choice, then click the 'ok' button"));
        }
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
        menuBar = new JMenuBar();
        settings = new JMenu("Settings");
        menuBar.add(settings);
        theme = new JMenu("Theme");
        settings.add(theme);
        about = new JMenuItem("about");
        settings.add(about);
        dark = new JMenuItem("Dark");
        light = new JMenuItem("Light");
        theme.add(dark);
        theme.add(light);
        notes.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1, GREY));
        menuPanel.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1, Color.lightGray));
        button.setVisible(false);
        notesLabel.setVisible(true);
        menuPanel.setVisible(true);
        choiceBox.setVisible(false);
        label1.setFont(defaultFont);
        consoleOutput.setBackground(Color.black);
        add(rootPanel);
        setJMenuBar(menuBar);
        Path currentRelativePath = Paths.get("");
        String imPath = currentRelativePath.toAbsolutePath().toString();
        ImageIcon icon = new ImageIcon(imPath + "/icons/mainIcon.png");
        setIconImage(icon.getImage());
        setTitle("RepTel.java");
        setSize(800, 700);
        setLocationRelativeTo(null);
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

        dark.addActionListener(e ->{
            rootPanel.setBackground(GREY);
            menuPanel.setBackground(GREY);
            notes.setBackground(GREY);
            menuPanel.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1, Color.lightGray));
            notes.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Color.lightGray));
            notesLabel.setForeground(GOLD);
            textField1.setBackground(GREY);
            label1.setForeground(GOLD);
            label2.setForeground(GOLD);
            consoleLabel.setForeground(GOLD);
            textField1.setForeground(GOLD);
            sliderSize.setBackground(GREY);
            fontSizeLabel.setForeground(GOLD);
            sliderSize.setForeground(GOLD);
        }  );
        light.addActionListener(e -> {
            rootPanel.setBackground(WHITE);
            menuPanel.setBackground(WHITE);
            notes.setBackground(WHITE);
            menuPanel.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1, GREY));
            notes.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1, GREY));
            notesLabel.setForeground(BLACK);
            textField1.setBackground(WHITE);
            label1.setForeground(BLACK);
            label2.setForeground(BLACK);
            consoleLabel.setForeground(BLACK);
            textField1.setForeground(BLACK);
            sliderSize.setBackground(WHITE);
            fontSizeLabel.setForeground(BLACK);
            sliderSize.setForeground(BLACK);
        });
        about.addActionListener(e -> {

            JOptionPane optionPane = new JOptionPane("<html>Credits: <br/>Made by JariBou<br/><br/>Version: Beta 0.5.6</html>",
                    JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = optionPane.createDialog("About");
            dialog.setIconImage(icon.getImage());
            dialog.setAlwaysOnTop(true); // to show top of all other application
            dialog.setVisible(true); // to visible the dialog

            //showMessageDialog(null, "<html><div style='text-align: center;'>Credits: <br/>Made by JariBou<br/><br/>Version: Beta 0.5.6</div></html>");
        });

        this.addWindowListener(new WindowListener() {


            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(1);
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

    }
}
