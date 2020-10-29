package fr.jari.RepTelJava.java;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


@SuppressWarnings("BusyWait")
public class Main{
    // Console output query and display when writing
    public static void displayWrite(mainGUI frame, Write w) throws InterruptedException {
        frame.consoleOutput.setForeground(w.color);
        frame.consoleOutput.setText("> ------------------");
        Thread.sleep(500);
        frame.consoleOutput.setText("> " + w.output);
        w.output = "";
    }

    // Default setup
    public static void buttonActions(mainGUI frame) throws InterruptedException {
        buttonActions(frame, null, Color.green);
    }

    // Button interactions
    public static void buttonActions(mainGUI frame, String text, Color color) throws InterruptedException {
        frame.consoleOutput.setForeground(color);
        if (text != null){
            frame.consoleOutput.setText("> " + text);
        } else{
            frame.consoleOutput.setText("> Click on the 'Ok' button to exit to Menu");
        }
        frame.button.setVisible(true);
        while (!frame.clicked) {
            Thread.sleep(100);
        }
        frame.button.setVisible(false);
        frame.clicked = false;
    } public static boolean testType(String s){
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    // Main
    public static void main(String[] args) throws IOException, InterruptedException {
        // GUI init
        mainGUI frame = new mainGUI();
        frame.setVisible(true);
        // Write and Read init
        Write w = new Write();
        Read r = new Read();
        // Path query
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString();

        while(true) {
            boolean hasFiles = false;
            frame.label2.setText("Current file:  Unknown");
            // Search for files in directory
            StringBuilder files = new StringBuilder();
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles == null){
                files.append("<br/ ").append(" No files");
            } else {
                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile()) {
                        if (listOfFile.getName().endsWith(".txt")) {
                            hasFiles = true;
                            files.append("<br/> - ").append(listOfFile.getName().replace(".txt", ""));
                        }
                    }
                }
            }
            if (!hasFiles) { // New file since no pre-existing file
                frame.consoleOutput.setForeground(Color.red);
                frame.consoleOutput.setText("> No existing Files");
                frame.label1.setText("Enter a filename to be created: ");
                while (!frame.isPressed()) {
                    Thread.sleep(100);
                } if (frame.isPressed()) {
                    String fileName = frame.input;
                    w.create(fileName);
                    displayWrite(frame, w);
                    if (w.color == Color.green) {
                        Thread.sleep(100);
                        frame.setPressed(false);
                    }
                }frame.setPressed(false);
            }
            if (hasFiles){ // Selection of the file
                frame.label1.setText("<html> Which file do you wish to access? " + files + " </html>");
                while (!frame.isPressed()) {
                    Thread.sleep(100);
                }
                if (frame.isPressed()) {
                    String file = frame.input;
                    w.setFilename(file);
                    r.setFilename(file);
                }
            } frame.setPressed(false);

            menuloop: // Menu loop, used to change the file
            while (true) {
                // Menu display
                frame.label2.setText("Current file:  " + w.getFilename());
                frame.label1.setText("<html>0 - Quit <br/> 1 - Write <br/> 2 - Read <br/> 3 - Get number of persons <br/> 4 - Create new file <br/> 5 - Switch File </html>");
                frame.consoleOutput.setText("> ");
                frame.setPressed(false);
                String choice = "0";
                while (!frame.isPressed()) {
                    Thread.sleep(100);
                    if (frame.isPressed()) {
                        choice = frame.input;
                        break;
                    }
                }frame.setPressed(false);
                switchloop: // Switch loop, used to exit the switch loop
                switch (choice) {
                    case "0" -> System.exit(0); // Quit
                    case "1" -> { // Write in the file
                        String filename = w.getFilename();
                        File myRead = new File(r.getFilename());
                        while (true) {
                            Scanner myReader = new Scanner(myRead);
                            frame.label1.setText("Enter a name (0 to quit): ");
                            while (!frame.isPressed()) {
                                Thread.sleep(100);
                            }
                            if (frame.isPressed()) {
                                String name = frame.input;
                                if (name.equals("0")) {
                                    break;
                                }
                                frame.label1.setText("Enter a name: " + name);
                                ArrayList<String> filecontent = r.read(myReader);
                                if (filecontent.contains(name)){
                                    frame.consoleOutput.setForeground(Color.red);
                                    frame.consoleOutput.setText("> Name already assigned, add a number to that person? Y/N");
                                    frame.setPressed(false);
                                    while (!frame.isPressed()) {
                                        Thread.sleep(100);
                                    }
                                    if (frame.isPressed()) {
                                        String add = frame.input;
                                        frame.setPressed(false);
                                        if (add.equals("Y")){
                                            FileWriter myWriter = new FileWriter(filename, false); // if contained need to rewrite everything
                                            boolean found = false;
                                            int start = filecontent.indexOf(name);
                                            for (String i : filecontent.subList(start, filecontent.size())) {
                                                if (found) {
                                                    if (!testType(i)) {
                                                        int pos = filecontent.indexOf(i);
                                                        frame.consoleOutput.setText("> ");
                                                        frame.label1.setText("<html> " + frame.label1.getText() + " <br/> Enter a number: </html>");
                                                        while(!frame.isPressed()){
                                                            Thread.sleep(100);
                                                        } if (frame.isPressed()){
                                                            String number = frame.input;
                                                            filecontent.add(pos, number);
                                                        } frame.setPressed(false);
                                                        StringBuilder filecontentStr = new StringBuilder();
                                                        for (String k : filecontent){
                                                            filecontentStr.append(k).append("\n");
                                                        } w.write(filecontentStr.toString(), myWriter);
                                                        displayWrite(frame, w);
                                                        myWriter.close();
                                                        break;
                                                    }
                                                }
                                                if (name.equals(i)) {
                                                    found = true;
                                                }
                                            }
                                        }
                                        if (add.equals("N")) {
                                            break;
                                        }
                                    } myReader.close();
                                    frame.setPressed(false);
                                }
                                else{
                                    FileWriter myWriter = new FileWriter(filename, true); // if not you good bruv
                                    frame.setPressed(false);
                                    frame.label1.setText("<html> " + frame.label1.getText() + " <br/> Enter a number: </html>");
                                    while (!frame.isPressed()) {
                                        Thread.sleep(100);
                                    }
                                    if (frame.isPressed()) {
                                        String number = frame.input;
                                        if (number.equals("0")) {
                                            break;
                                        }
                                        frame.label1.setText(frame.label1.getText() + number);
                                        w.write(name + "\n" + number + "\n", myWriter);
                                        displayWrite(frame, w);
                                    }
                                    frame.setPressed(false);
                                    myWriter.close();
                                }
                            }
                        }
                    }

                    case "2" -> { // Search in file
                        frame.label1.setText("Enter the person's name to search for: ");
                        File myRead = new File(r.getFilename());
                        Scanner myReader = new Scanner(myRead);
                        ArrayList<String> fileContent = r.read(myReader);
                        StringBuilder numbers = new StringBuilder();
                        while (true) {
                            boolean found = false;
                            String search = "";
                            while (!frame.isPressed()) {
                                Thread.sleep(100);
                                if (frame.isPressed()) {
                                    search = frame.input;
                                    break;
                                }
                            }
                            frame.setPressed(false);
                            int start = fileContent.indexOf(search);
                            for (String i : fileContent.subList(start, fileContent.size())) {
                                if (found) {
                                    if (testType(i)) {
                                        numbers.append(" <br/> - ").append(i);
                                    } else {
                                        break;
                                    }
                                }
                                if (search.equals(i)) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                buttonActions(frame, "Person not found, click 'Ok' to try again", Color.red);
                                frame.consoleOutput.setForeground(Color.green);
                                frame.consoleOutput.setText("> Please enter a name");
                            } else {
                                if (search.endsWith("s")) {
                                    search += "'";
                                } else {
                                    search += "'s";
                                } frame.label1.setText("<html>" + search + " numbers are: " + numbers +"<br/" + "Do you want to modify? M/N" + "<br/>" + " <html/>");
                                buttonActions(frame);
                                frame.label1.setText("OK");
                                myReader.close();
                                break switchloop;
                            }
                        }
                    }

                    case "3" -> { // Get number of persons in file, and fetch names if needed
                        File myRead = new File(r.getFilename());
                        Scanner myReader = new Scanner(myRead);
                        ArrayList<String> fileContent = r.read(myReader);
                        ArrayList<String> names = new ArrayList<>();
                        int count = 0;
                        for (String s : fileContent) {
                            try {
                                Long.parseLong(s);
                            } catch (NumberFormatException e) {
                                names.add(s);
                                count++;
                            }
                        }
                        frame.label1.setText("<html> The number of persons stored is: " + count + " <br/> Do you want to know their names? Y/N </html>");
                        while (!frame.isPressed()) {
                            Thread.sleep(100);
                        }
                        if (frame.isPressed()) {
                            String name = frame.input;
                            if (name.equals("Y")) {
                                StringBuilder strNames = new StringBuilder();
                                for (String s : names) {
                                    strNames.append(" <br/> - ").append(s);
                                }
                                frame.label1.setText("<html> Their names are: " + strNames + "<html/>");      // When too much names problem, they all on same line, and they appear as lists
                                buttonActions(frame);
                            }
                        }
                        frame.setPressed(false);
                    }

                    case "4" -> { // Create a new file
                        while (true) {
                            frame.label1.setText("Enter the filename: ");
                            while (!frame.isPressed()) {
                                Thread.sleep(100);
                            }
                            if (frame.isPressed()) {
                                String fileName = frame.input;
                                w.create(fileName);
                                displayWrite(frame, w);
                                if (w.color == Color.green) {
                                    Thread.sleep(100);
                                    frame.setPressed(false);
                                    break;
                                }
                            }
                            frame.setPressed(false);
                        }
                    }

                    case "5" -> { // Breaks the Menu loop to change file
                        break menuloop;
                    }
                }
                frame.input = null;
            }
        }
    }
}