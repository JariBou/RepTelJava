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
        f.setConsoleOutput("> ------------------", w.color);
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
    }
    /** Returns a boolean value:
     *  true -> The String can be converted to a Double
     *  -> It is only composed of numbers
     *
     *  false -> The String cannot be converted to a Double
     *  -> It is composed of at least 1 String
     */
    public static boolean testType(String s){
        try {
            Double.parseDouble(s);
            return true; // Double
        } catch (NumberFormatException ignored) {
            return false; // String
        }
    }
    public static String capitalize(String str)
    {
        if(str == null) return null;
        str = str.toLowerCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Main
    public static void main(String[] args) throws IOException, InterruptedException {
        Color GREEN = Color.green;
        Color RED = Color.red;
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
            ArrayList<String> filesList = new ArrayList<>();
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles == null){
                files.append("<br/ ").append(" No files");
            } else {
                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile()) {
                        if (listOfFile.getName().endsWith(".txt")) {
                            hasFiles = true;
                            filesList.add(listOfFile.getName().replace(".txt", ""));
                            files.append("<br/> - ").append(listOfFile.getName().replace(".txt", ""));
                        }
                    }
                }
            }
            if (!hasFiles) { // New file since no pre-existing file
                f.setConsoleOutput("> No existing Files", RED);
                f.label1.setText("Enter a filename to be created: ");
                while (!f.isPressed()) {
                    Thread.sleep(100);
                } if (f.isPressed()) {
                    String fileName = f.input;
                    w.create(fileName);
                    displayWrite(f, w);
                    w.setFilename(fileName);
                    r.setFilename(fileName);
                    if (w.color == Color.green) {
                        Thread.sleep(100);
                        f.setPressed(false);
                    }
                }f.setPressed(false);
            }
            if (hasFiles){ // Selection of the file
                while (true) {
                    f.label1.setText("<html> Which file do you wish to access? " + files + " </html>");
                    while (!f.isPressed()) {
                        Thread.sleep(100);
                    }
                    if (f.isPressed()) {
                        String file = f.input;
                        if (filesList.contains(file)) {
                            w.setFilename(file);
                            r.setFilename(file);
                            break;
                        } else {
                            f.setConsoleOutput(("> File " + file + ".txt doesn't exist, Create?"), RED);
                            String newFile = f.setChoiceBox(new ArrayList<>( Arrays.asList("Yes", "No")), false);
                            if (newFile.equals("Yes")){
                                f.label1.setText("Enter a filename to be created: ");
                                while (!f.isPressed()) {
                                    Thread.sleep(100);
                                } if (f.isPressed()) {
                                    String fileName = f.input;
                                    w.create(fileName);
                                    displayWrite(f, w);
                                    w.setFilename(fileName);
                                    r.setFilename(fileName);
                                    if (w.color == Color.green) {
                                        Thread.sleep(100);
                                        f.setPressed(false);
                                    }
                                }f.setPressed(false);
                                break;
                            } else {
                                System.exit(0);
                            }
                        }
                    }
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
                                String name = capitalize(f.input);
                                if (name.equals("0")) {
                                    break;
                                }
                                f.label1.setText("Enter a name: " + name);
                                ArrayList<String> filecontent = r.read(myReader);
                                if (filecontent.contains(name)){
                                    String add = f.setChoiceBox(new ArrayList<>(Arrays.asList("Y", "N")), "> Name already assigned, add a number to that person? Y/N", RED);
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
                        ArrayList<String> numbersList = new ArrayList<>();
                        while (true) {
                            boolean found = false;
                            String searchE;
                            String search = "";
                            while (!f.isPressed()) {
                                Thread.sleep(100);
                                if (f.isPressed()) {
                                    search = capitalize(f.input);
                                    break;
                                }
                            }
                            f.setPressed(false);
                            int start = fileContent.indexOf(search);
                            int count = 0;
                            for (String i : fileContent.subList(start, fileContent.size())) {
                                if (found) {
                                    if (testType(i)) {
                                        count ++;
                                        numbers.append(" <br/>").append(count).append("- ").append(i);
                                        numbersList.add(i);
                                    } else {
                                        break;
                                    }
                                }
                                if (search.equals(i)) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                buttonActions(f, "Person not found, click 'Ok' to try again", RED);
                                f.setConsoleOutput("> Please enter a name", GREEN);
                            } else {
                                if (search.endsWith("s")) {
                                    searchE = search + "'";
                                } else {
                                    searchE = search + "'s";
                                } f.label1.setText("<html>" + searchE + " numbers are: " + numbers +"<br/" + "Do you want to modify them? M/N" + "<br/>" + " <html/>");

                                choice =  f.setChoiceBox(new ArrayList<>( Arrays.asList("N", "M")));
                                if (choice.equals("M")){
                                    f.label1.setText("<html>" + searchE + " numbers are: " + numbers +"<br/" + "Choose your option from the list:" +
                                            "<br/>" + "- Modify a number <br/>" + "- Add a number <br/>" + "- Remove a number <br/>"
                                            + "- Delete the person <br/>" + " <html/>");
                                    choice = f.setChoiceBox(new ArrayList<>( Arrays.asList("modify", "add", "remove", "delete")));
                                    myRead = new File(r.getFilename());
                                    myReader = new Scanner(myRead);
                                    String filename = w.getFilename();
                                    ArrayList<String> filecontent = r.read(myReader);
                                    switch (choice){
                                        case "modify":
                                            f.label1.setText("<html>" + searchE + " numbers are: " + numbers +"<html/>"); // print the numbers with an associated number
                                            int hardCount = 1;
                                            ArrayList<String> countNumb = new ArrayList<>();
                                            while(hardCount <= count){
                                                countNumb.add(Integer.toString(hardCount));
                                                hardCount ++;
                                            }
                                            int posN = Integer.parseInt(f.setChoiceBox(countNumb));
                                            String oldNumber = fileContent.get(posN); // old number
                                            f.setConsoleOutput("> Enter the new number", GREEN);
                                            while (!f.isPressed()){
                                                Thread.sleep(100);
                                            }
                                            String newNumber = f.input;
                                            filecontent.set(filecontent.indexOf(oldNumber), newNumber);
                                            FileWriter myWriter = new FileWriter(filename, false);
                                            StringBuilder filecontentStr = new StringBuilder();
                                            for (String a : filecontent){
                                                filecontentStr.append(a).append("\n");
                                            }
                                            w.write(filecontentStr.toString(), myWriter);
                                            myWriter.close();
                                            break;

                                        case "add":
                                            found = false;
                                            start = filecontent.indexOf(search);
                                            for (String line : filecontent.subList(start, filecontent.size())) {
                                                System.out.println("line: "+ line);
                                                System.out.println("search: "+ search);
                                                System.out.println(found);
                                                if (found) {
                                                    if (!testType(line)) {
                                                        myWriter = new FileWriter(filename, false); // if contained need to rewrite everything
                                                        int pos = filecontent.indexOf(line);
                                                        f.label1.setText("Enter the number you wish to add");
                                                        while(!f.isPressed()){
                                                            Thread.sleep(100);
                                                        } if (f.isPressed()){
                                                            String number = f.input;
                                                            filecontent.add(pos, number);
                                                        } f.setPressed(false);
                                                        filecontentStr = new StringBuilder();
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
                                            } if (found){
                                            myWriter = new FileWriter(filename, false); // if contained need to rewrite everything
                                            int pos = filecontent.size();
                                            f.label1.setText("Enter the number you wish to add");
                                            while(!f.isPressed()){
                                                Thread.sleep(100);
                                            } if (f.isPressed()){
                                                String number = f.input;
                                                filecontent.add(pos, number);
                                            } f.setPressed(false);
                                            filecontentStr = new StringBuilder();
                                            for (String k : filecontent){
                                                filecontentStr.append(k).append("\n");
                                            } w.write(filecontentStr.toString(), myWriter);
                                            displayWrite(f, w);
                                            myWriter.close();
                                            }
                                            break;

                                        case "remove":
                                            f.label1.setText("<html>" + searchE + " numbers are: " + numbers +"<html/>"); // print the numbers with an associated number
                                            int hardCountR = 1;
                                            ArrayList<String> countNumbR = new ArrayList<>();
                                            while(hardCountR <= count){
                                                countNumbR.add(Integer.toString(hardCountR));
                                                hardCountR ++;
                                            }
                                            int posNR = Integer.parseInt(f.setChoiceBox(countNumbR));
                                            filecontent.remove(numbersList.get(posNR-1));
                                            f.setConsoleOutput("> Number removed", GREEN);
                                            Thread.sleep(500);
                                            myWriter = new FileWriter(filename, false);
                                            filecontentStr = new StringBuilder();
                                            for (String o : filecontent){
                                                filecontentStr.append(o).append("\n");
                                            }
                                            w.write(filecontentStr.toString(), myWriter);
                                            myWriter.close();
                                            break;

                                        case "delete":
                                            found = false;
                                            filecontentStr = new StringBuilder();
                                            myWriter = new FileWriter(filename, false);
                                            for (String content : filecontent){
                                                if (found){
                                                    if (!testType(content)){
                                                        found = false;
                                                    }
                                                }
                                                if (content.equals(search)){
                                                    found = true;
                                                }
                                                if (!found){
                                                    filecontentStr.append(content).append("\n");
                                                }
                                            }
                                            w.write(filecontentStr.toString(), myWriter);
                                            displayWrite(f, w);
                                            myWriter.close();
                                            f.setConsoleOutput("> That person has been successfully deleted", GREEN);
                                            Thread.sleep(500);
                                            break;
                                    }
                                } else{
                                    System.out.println("not in switch");
                                    myReader.close();
                                    break switchloop;
                                }
                                System.out.println("somewhere");
                                break switchloop;
                            }myReader.close();
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

                    case "6" -> {
                        File myRead = new File(r.getFilename());
                        Scanner myReader = new Scanner(myRead);
                        ArrayList<String> fileContent = r.read(myReader);
                        StringBuilder filecontent = new StringBuilder();
                        filecontent.append("<html>");
                        int first = 1;
                        for (String line : fileContent){
                            if (!testType(line) && first == 0){
                                filecontent.append("<br/><br/>").append(line).append(" :");
                            } if (first == 1){
                                filecontent.append("List of Names:<br/><br/>").append(line).append(" :");
                                first = 0;
                            } if (testType(line)){
                                filecontent.append("<br/>- ").append(line);
                            }
                        }filecontent.append("</html>");
                        fileGUI fg = new fileGUI(w.getFilename());
                        fg.setVisible(true);
                        fg.display.setText(String.valueOf(filecontent));
                        //String all = new Scanner(new File(w.getFilename())).useDelimiter("\\A").next();
                        //fg.display.setText(all);
                    }
                }
                f.input = null;
            }
        }
    }
}