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
    public JButton exportNotes;
    private JComboBox<String> choiceBox;
    public JLabel fontSizeLabel;
    private JLabel consoleLabel;
    public String input; // Value of the Input Display
    public boolean pressed;
    public boolean clicked;
    private boolean state;

    public String Lang;
    public String Country;

    Color GREY = Color.darkGray;
    Color GOLD = Color.orange;
    Color WHITE = Color.white;
    Color BLACK = Color.black;

    JMenuBar menuBar;
    JMenu settings;
    JMenu theme, langMenu;
    JMenuItem dark, light, about, eng, fr;

    WriteEng w = new WriteEng();
    Font defaultFont = new Font("Arial", Font.PLAIN, 25);

    public Font setFontSize(int size) { // Changes the font size, used for button
        return new Font("Arial", Font.PLAIN, size);
    }

    public boolean isPressed() { // Returns a boolean of the state of the enter key
        return pressed;
    }

    public void setPressed(boolean state) { // Sets the boolean state of the enter key
        this.pressed = state;
    }

    public String getLang(){return this.Lang;}
    public String getCountry(){return this.Country;}

    // Default set
    public String setChoiceBox(ArrayList<String> list) throws InterruptedException {
        return setChoiceBox(list, null, Color.green, true);
    }

    public String setChoiceBox(ArrayList<String> list, boolean display) throws InterruptedException {
        return setChoiceBox(list, null, Color.green, display);
    }

    public String setChoiceBox(ArrayList<String> list, String message, Color color) throws InterruptedException {
        return setChoiceBox(list, message, color, true);
    }

    // Sets the text to display in the console
    public void setConsoleOutput(String text, Color color) {
        consoleOutput.setForeground(color);
        consoleOutput.setText(text);
    }

    /**
     * Sets the theme
     *
     * @param textColor   Color of the text
     * @param bgColor     Color of the background
     * @param borderColor Color of the borders
     */
    public void setTheme(Color textColor, Color bgColor, Color borderColor) {
        rootPanel.setBackground(bgColor);
        menuPanel.setBackground(bgColor);
        menuPanel.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1, borderColor));
        notes.setBackground(bgColor);
        notes.setForeground(textColor);
        notes.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1, borderColor));
        notesLabel.setForeground(textColor);
        textField1.setBackground(bgColor);
        textField1.setForeground(textColor);
        label1.setForeground(textColor);
        label2.setForeground(textColor);
        consoleLabel.setForeground(textColor);
        sliderSize.setBackground(bgColor);
        sliderSize.setForeground(textColor);
        fontSizeLabel.setForeground(textColor);
    }

    /**
     * @param list    List of the elements to be added to the box
     * @param message Sets a custom message to output to console
     * @param color   Color of the console
     * @param display Displays whether or not a message will be displayed
     * @return The String value of the chosen element
     * @throws InterruptedException Consequence of the Thread.sleep
     */
    public String setChoiceBox(ArrayList<String> list, String message, Color color, boolean display) throws InterruptedException {
        choiceBox.setVisible(true);
        for (String s : list) {
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
        }
        clicked = false;
        int choice = choiceBox.getSelectedIndex();
        choiceBox.setVisible(false);
        button.setVisible(false);
        choiceBox.removeAllItems();
        return list.get(choice);
    }

    public boolean langChange(){
        return this.state;
    }
    public void rstLangChange(){
        this.state = false;
    }

    // Initializer
    public mainGUI(String language, String country) {
        LangFunctions lang = new LangFunctions();
        lang.setLang(language, country);
        this.Lang = "eng"; this.Country = "ENG"; this.state = false;

        // Menu bar
        menuBar = new JMenuBar();
        settings = new JMenu(lang.get("settings"));
        menuBar.add(settings);
        theme = new JMenu("Theme");
        settings.add(theme);
        dark = new JMenuItem("Dark");
        light = new JMenuItem("Light");
        theme.add(dark);
        theme.add(light);
        langMenu = new JMenu(lang.get("language"));
        settings.add(langMenu);
        eng = new JMenuItem("English");
        fr = new JMenuItem("Français");
        langMenu.add(eng);
        langMenu.add(fr);
        about = new JMenuItem(lang.get("about"));
        settings.add(about);
        setJMenuBar(menuBar);

        // Default theme, and Visual Init
        setTheme(BLACK, WHITE, GREY);
        label1.setFont(defaultFont);
        consoleOutput.setBackground(Color.black);
        button.setVisible(false);
        notesLabel.setVisible(true);
        menuPanel.setVisible(true);
        choiceBox.setVisible(false);
        //Icon set
        Path currentRelativePath = Paths.get("");
        String imPath = currentRelativePath.toAbsolutePath().toString();
        ImageIcon icon = new ImageIcon(imPath + "/icons/mainIcon.png");
        setIconImage(icon.getImage());

        // Gui Init
        add(rootPanel);
        setTitle("RepTel.java");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { // Detects if a key is pressed
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Detects if the key pressed is Enter
                    setPressed(true);
                    input = textField1.getText();
                    textField1.setText("");
                }
            }
        });
        sliderSize.addChangeListener(e -> { // Detects if the slider state changed
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                int size = source.getValue();
                label1.setFont(setFontSize(size)); // Sets the font size according to the value of the slider
            }
        });
        button.addActionListener(e -> clicked = true); // Button clicked detection
        exportNotes.addActionListener(e -> { // Exports notes to .txt File
            String content = notes.getText();
            JFrame parentFrame = new JFrame();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(lang.get("save_notes"));
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

        // Actions listeners of the toolBar elements
        dark.addActionListener(e -> setTheme(GOLD, GREY, Color.lightGray));
        light.addActionListener(e -> setTheme(BLACK, WHITE, GREY));

        eng.addActionListener(e -> {
            this.Lang = "eng"; this.Country = "ENG"; this.state = true;
            lang.setLanguage(this.Lang); lang.setCountry(this.Country);
            lang.setLang(this.Lang, this.Country);
        });
        fr.addActionListener(e -> {
            this.Lang = "fr"; this.Country = "FR"; this.state = true;
            lang.setLanguage(this.Lang); lang.setCountry(this.Country);
            lang.setLang(this.Lang, this.Country);
        });

        about.addActionListener(e -> {
            JOptionPane optionPane = new JOptionPane(lang.get("credits"),
                    JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = optionPane.createDialog(lang.get("about"));
            dialog.setIconImage(icon.getImage());
            dialog.setAlwaysOnTop(true); // to show top of all other application
            dialog.setVisible(true); // to visible the dialog
        });

        // Window listener to detect if the window is closed, and if so runs System.exit(1)
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
