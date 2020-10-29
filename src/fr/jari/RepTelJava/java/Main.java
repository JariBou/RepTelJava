package fr.jari.RepTelJava.java;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


@SuppressWarnings("BusyWait")
public class Main{
    // Console output query and display when writing
    public static void displayWrite(mainGUI f, Write w) throws InterruptedException {
        f.consoleOutput.setForeground(w.color);
        f.consoleOutput.setText("> ------------------");
        Thread.sleep(500);
        f.consoleOutput.setText("> " + w.output);
        w.output = "";
    }

    // Default setup
    public static void buttonActions(mainGUI f) throws InterruptedException {
        buttonActions(f, null, Color.green);
    }

    // Button interactions
    public static void buttonActions(mainGUI f, String text, Color color) throws InterruptedException {
        f.consoleOutput.setForeground(color);
        if (text != null){
            f.consoleOutput.setText("> " + text);
        } else{
            f.consoleOutput.setText("> Click on the 'Ok' button to exit to Menu");
        }
        f.button.setVisible(true);
        while (!f.clicked) {
            Thread.sleep(100);
        }
        f.button.setVisible(false);
        f.clicked = false;
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
        mainGUI f = new mainGUI();
        f.setVisible(true);
        // Write and Read init
        Write w = new Write();
        Read r = new Read();
        // Path query
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString();

        while(true) {
            boolean hasFiles = false;
            f.label2.setText("Current file:  Unknown");
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
                f.consoleOutput.setForeground(Color.red);
                f.consoleOutput.setText("> No existing Files");
                f.label1.setText("Enter a filename to be created: ");
                while (!f.isPressed()) {
                    Thread.sleep(100);
                } if (f.isPressed()) {
                    String fileName = f.input;
                    w.create(fileName);
                    displayWrite(f, w);
                    if (w.color == Color.green) {
                        Thread.sleep(100);
                        f.setPressed(false);
                    }
                }f.setPressed(false);
            }
            if (hasFiles){ // Selection of the file
                f.label1.setText("<html> Which file do you wish to access? " + files + " </html>");
                while (!f.isPressed()) {
                    Thread.sleep(100);
                }
                if (f.isPressed()) {
                    String file = f.input;
                    w.setFilename(file);
                    r.setFilename(file);
                }
            } f.setPressed(false);

            menuloop: // Menu loop, used to change the file
            while (true) {
                // Menu display
                f.label2.setText("Current file:  " + w.getFilename());
                f.label1.setText("<html>0 - Quit <br/> 1 - Write <br/> 2 - Read <br/> 3 - Get number of persons <br/> 4 - Create new file <br/> 5 - Switch File </html>");
                f.consoleOutput.setText("> ");
                f.setPressed(false);
                String choice = "0";
                while (!f.isPressed()) {
                    Thread.sleep(100);
                    if (f.isPressed()) {
                        choice = f.input;
                        break;
                    }
                }f.setPressed(false);
                switchloop: // Switch loop, used to exit the switch loop
                switch (choice) {
                    case "0" -> System.exit(0); // Quit
                    case "1" -> { // Write in the file
                        String filename = w.getFilename();
                        File myRead = new File(r.getFilename());
                        while (true) {
                            Scanner myReader = new Scanner(myRead);
                            f.label1.setText("Enter a name (0 to quit): ");
                            while (!f.isPressed()) {
                                Thread.sleep(100);
                            }
                            if (f.isPressed()) {
                                String name = f.input;
                                if (name.equals("0")) {
                                    break;
                                }
                                f.label1.setText("Enter a name: " + name);
                                ArrayList<String> filecontent = r.read(myReader);
                                if (filecontent.contains(name)){
                                    f.consoleOutput.setForeground(Color.red);
                                    f.consoleOutput.setText("> Name already assigned, add a number to that person? Y/N");
                                    String add = f.setChoiceBox(new ArrayList<>(Arrays.asList("Y", "N")));
                                    f.setPressed(false);
                                    if (add.equals("Y")){
                                        FileWriter myWriter = new FileWriter(filename, false); // if contained need to rewrite everything
                                        boolean found = false;
                                        int start = filecontent.indexOf(name);
                                        for (String i : filecontent.subList(start, filecontent.size())) {
                                            if (found) {
                                                if (!testType(i)) {
                                                    int pos = filecontent.indexOf(i);
                                                    f.consoleOutput.setText("> ");
                                                    f.label1.setText("<html> " + f.label1.getText() + " <br/> Enter a number: </html>");
                                                    while(!f.isPressed()){
                                                        Thread.sleep(100);
                                                    } if (f.isPressed()){
                                                        String number = f.input;
                                                        filecontent.add(pos, number);
                                                    } f.setPressed(false);
                                                    StringBuilder filecontentStr = new StringBuilder();
                                                    for (String k : filecontent){
                                                        filecontentStr.append(k).append("\n");
                                                    } w.write(filecontentStr.toString(), myWriter);
                                                    displayWrite(f, w);
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
                                    myReader.close();
                                    f.setPressed(false);
                                }
                                else{
                                    FileWriter myWriter = new FileWriter(filename, true); // if not you good bruv
                                    f.setPressed(false);
                                    f.label1.setText("<html> " + f.label1.getText() + " <br/> Enter a number: </html>");
                                    while (!f.isPressed()) {
                                        Thread.sleep(100);
                                    }
                                    if (f.isPressed()) {
                                        String number = f.input;
                                        if (number.equals("0")) {
                                            break;
                                        }
                                        f.label1.setText(f.label1.getText() + number);
                                        w.write(name + "\n" + number + "\n", myWriter);
                                        displayWrite(f, w);
                                    }
                                    f.setPressed(false);
                                    myWriter.close();
                                }
                            }
                        }
                    }

                    case "2" -> { // Search in file
                        f.label1.setText("Enter the person's name to search for: ");
                        File myRead = new File(r.getFilename());
                        Scanner myReader = new Scanner(myRead);
                        ArrayList<String> fileContent = r.read(myReader);
                        StringBuilder numbers = new StringBuilder();
                        while (true) {
                            boolean found = false;
                            String searchE = "";
                            String search = "";
                            while (!f.isPressed()) {
                                Thread.sleep(100);
                                if (f.isPressed()) {
                                    search = f.input;
                                    break;
                                }
                            }
                            f.setPressed(false);
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
                                buttonActions(f, "Person not found, click 'Ok' to try again", Color.red);
                                f.consoleOutput.setForeground(Color.green);
                                f.consoleOutput.setText("> Please enter a name");
                            } else {
                                if (search.endsWith("s")) {
                                    searchE = search + "'";
                                } else {
                                    searchE = search + "'s";
                                } f.label1.setText("<html>" + searchE + " numbers are: " + numbers +"<br/" + "Do you want to modify them? M/N" + "<br/>" + " <html/>");

                                choice =  f.setChoiceBox(new ArrayList<>( Arrays.asList("M", "N")));
                                if (choice.equals("M")){
                                    f.label1.setText("<html>" + searchE + " numbers are: " + numbers +"<br/" + "Choose your option from the list:" +
                                            "<br/>" + "- Modify a number <br/>" + "- Add a number <br/>" + "- Remove a number <br/>"
                                            + "- Delete the person <br/>" + " <html/>");
                                    choice = f.setChoiceBox(new ArrayList<>( Arrays.asList("modify", "add", "remove", "delete")));
                                    switch (choice){
                                        case "modify":
                                            break;

                                        case "add":
                                            String filename = w.getFilename();
                                            myRead = new File(r.getFilename());
                                            myReader = new Scanner(myRead);
                                            ArrayList<String> filecontent = r.read(myReader);
                                            found = false;
                                            start = filecontent.indexOf(search);
                                            for (String line : filecontent.subList(start, filecontent.size())) {
                                                if (found) {
                                                    if (!testType(line)) {
                                                        FileWriter myWriter = new FileWriter(filename, false); // if contained need to rewrite everything
                                                        int pos = filecontent.indexOf(line);
                                                        f.label1.setText("Enter the number you wish to add");
                                                        while(!f.isPressed()){
                                                            Thread.sleep(100);
                                                        } if (f.isPressed()){
                                                            String number = f.input;
                                                            filecontent.add(pos, number);
                                                        } f.setPressed(false);
                                                        StringBuilder filecontentStr = new StringBuilder();
                                                        for (String k : filecontent){
                                                            filecontentStr.append(k).append("\n");
                                                        } w.write(filecontentStr.toString(), myWriter);
                                                        displayWrite(f, w);
                                                        myWriter.close();
                                                        break;
                                                    }
                                                }
                                                if (search.equals(line)) {
                                                    found = true;
                                                }
                                            }

                                        case "remove":
                                            break;

                                        case "delete":
                                            break;
                                    }


                                } else{
                                    myReader.close();
                                    break switchloop;
                                }




                                buttonActions(f);
                                f.label1.setText("OK");
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
                        f.label1.setText("<html> The number of persons stored is: " + count + " <br/> Do you want to know their names? Y/N </html>");
                        String name = f.setChoiceBox(new ArrayList<>( Arrays.asList("Y", "N")));
                        if (name.equals("Y")) {
                            StringBuilder strNames = new StringBuilder();
                            for (String s : names) {
                                strNames.append(" <br/> - ").append(s);
                            }
                            f.label1.setText("<html> Their names are: " + strNames + "<html/>");      // When too much names problem, they all on same line, and they appear as lists
                            buttonActions(f);
                        }
                        f.setPressed(false);
                    }

                    case "4" -> { // Create a new file
                        while (true) {
                            f.label1.setText("Enter the filename: ");
                            while (!f.isPressed()) {
                                Thread.sleep(100);
                            }
                            if (f.isPressed()) {
                                String fileName = f.input;
                                w.create(fileName);
                                displayWrite(f, w);
                                if (w.color == Color.green) {
                                    Thread.sleep(100);
                                    f.setPressed(false);
                                    break;
                                }
                            }
                            f.setPressed(false);
                        }
                    }

                    case "5" -> { // Breaks the Menu loop to change file
                        break menuloop;
                    }
                }
                f.input = null;
            }
        }
    }
}