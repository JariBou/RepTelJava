package fr.jari.RepTelJava.java;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@SuppressWarnings("BusyWait")
public class MainEng {

    // Console output query and display when writing
    public static void displayWrite(mainGUI f, WriteEng w) throws InterruptedException {
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
        if (text != null) {
            f.consoleOutput.setText("> " + text);
        } else {
            f.consoleOutput.setText("> Click on the 'Ok' button to exit to Menu");
        }
        f.button.setVisible(true);
        while (!f.clicked) {
            Thread.sleep(100);
        }
        f.button.setVisible(false);
        f.clicked = false;
    }

    /**
     * @param s The String
     * @return boolean
     * @implNote
     * true -> The String can be converted to a Double
     * -> It is only composed of numbers
     * <p>
     * false -> The String cannot be converted to a Double
     * -> It is composed of at least 1 String
     */
    public static boolean testType(String s) {
        try {
            Double.parseDouble(s);
            return true; // Double
        } catch (NumberFormatException ignored) {
            return false; // String
        }
    }

    /**
     *
     * @param str The word
     * @return str
     * @implNote
     *Returns the input string with a capital first letter and the rest in lower_case
     */
    public static String capitalize(String str) {
        if (str == null) return null;
        str = str.toLowerCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     *
     * @param f mainGui instance
     * @param w WriteEng instance
     * @param r Read instance
     * @throws InterruptedException Consequence of Thread.sleep
     * @implNote Creates a new file in the current .jar directory
     */
    public static void createFile(mainGUI f, WriteEng w, Read r) throws InterruptedException {
        f.label1.setText("Enter a filename to be created: ");
        while (!f.isPressed()) {
            Thread.sleep(100);
        }
        String fileName = f.input;
        w.create(fileName);
        displayWrite(f, w);
        w.setFilename(fileName);
        r.setFilename(fileName);
        if (w.color == Color.green) {
            f.setConsoleOutput("> File Created", Color.green);
            Thread.sleep(100);
        }
        f.setPressed(false);
    }


    // Main
    public static void main(String[] args, String language, String country) throws IOException, InterruptedException {

        LangFunctions lang = new LangFunctions();
        ResourceBundle rb = lang.setLang(language, country);

        Color GREEN = Color.green;
        Color RED = Color.red;
        // GUI init
        mainGUI f = new mainGUI();
        f.setVisible(true);
        f.setPressed(false);
        // Write and Read init
        WriteEng w = new WriteEng();
        Read r = new Read();
        // Path query
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString();

        // Loop for switching files
        while (true) {
            // Search for files in directory ----------------------------
            boolean hasFiles = false;
            f.label2.setText(lang.get("curr_file") + "  Unknown");
            StringBuilder files = new StringBuilder();
            ArrayList<String> filesList = new ArrayList<>();
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles == null) {
                files.append("<br/ ").append(" No files");
            }
            else {
                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile()) { // If is a file, not a directory
                        if (listOfFile.getName().endsWith(".txt")) { // If is a .txt file
                            hasFiles = true;
                            filesList.add(listOfFile.getName().replace(".txt", ""));
                            files.append("<br/> - ").append(listOfFile.getName().replace(".txt", ""));
                        }
                    }
                }
            }

            if (!hasFiles) { // New file since no pre-existing file
                f.setConsoleOutput("> " + lang.get("no_files"), RED);
                createFile(f, w, r);
            }
            if (hasFiles) { // Selection of the file
                f.label1.setText("<html>" + lang.get("file_access") + files + " </html>");
                while (!f.isPressed()) {
                    Thread.sleep(100);
                }
                String file = f.input;
                if (filesList.contains(file)) {
                    w.setFilename(file);
                    r.setFilename(file);
                } else {
                    f.setConsoleOutput((">" + lang.get("file")  + file + lang.get("create_file")), RED);
                    String newFile = f.setChoiceBox(new ArrayList<>(Arrays.asList("Yes", "No")), false);
                    if (newFile.equals("Yes")) {
                        createFile(f, w, r);
                    } else {
                        System.exit(0);
                    }
                }
            } f.setPressed(false);
            // --------------------------------------------------------------
            menuloop:
            // Menu loop, used to change the file
            while (true) {
                // Menu display
                f.label2.setText(lang.get("curr_file") + w.getFilename());
                f.label1.setText(lang.get("main_menu"));
                f.consoleOutput.setText("> ");
                //------------------------------------------------
                while (!f.isPressed()) {
                    Thread.sleep(100);
                }
                String choice = f.input;
                f.setPressed(false);
                switchloop:
                // Switch loop, used to exit the switch loop
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
                            String name = capitalize(f.input);
                            if (name.equals("0")) {
                                break;
                            }
                            f.label1.setText("Enter a name: " + name);
                            ArrayList<String> filecontent = r.read(myReader);
                            myReader.close();
                            if (filecontent.contains(name)) {
                                String add = f.setChoiceBox(new ArrayList<>(Arrays.asList("Yes", "No")), "> Name already assigned, add a number to that person? Y/N", RED);
                                f.setPressed(false);
                                if (add.equals("Yes")) {
                                    FileWriter myWriter = new FileWriter(filename, false); // if contained need to rewrite everything
                                    boolean found = false;
                                    int start = filecontent.indexOf(name);
                                    for (String i : filecontent.subList(start, filecontent.size())) {
                                        if (found) {
                                            if (!testType(i)) {
                                                int pos = filecontent.indexOf(i);
                                                f.consoleOutput.setText("> ");
                                                f.label1.setText("<html> " + f.label1.getText() + " <br/> Enter a number: </html>");
                                                while (!f.isPressed()) {
                                                    Thread.sleep(100);
                                                }
                                                String number = f.input;
                                                filecontent.add(pos, number);
                                                f.setPressed(false);
                                                StringBuilder filecontentStr = new StringBuilder();
                                                for (String k : filecontent) {
                                                    filecontentStr.append(k).append("\n");
                                                }
                                                w.write(filecontentStr.toString(), myWriter);
                                                displayWrite(f, w);
                                                myWriter.close();
                                                break;
                                            }
                                        }
                                        if (name.equals(i)) {
                                            found = true;
                                        }
                                    }
                                }else{break;}
                                f.setPressed(false);
                            }
                            else {
                                FileWriter myWriter = new FileWriter(filename, true); // if not you good bruv
                                f.setPressed(false);
                                f.label1.setText("<html> " + f.label1.getText() + " <br/> Enter a number: </html>");
                                while (!f.isPressed()) {
                                    Thread.sleep(100);
                                }
                                String number = f.input;
                                if (number.equals("0")) {
                                    break;
                                }
                                f.label1.setText(f.label1.getText() + number);
                                w.write(name + "\n" + number + "\n", myWriter);
                                displayWrite(f, w);
                                f.setPressed(false);
                                myWriter.close();
                            }

                        }
                    }

                    case "2" -> { // Search in file
                        FileWriter myWriter;
                        StringBuilder filecontentStr;
                        f.label1.setText("Enter the person's name to search for: "); // add smth to chnge the text displayed when there is only 1 number
                        File myRead = new File(r.getFilename());
                        Scanner myReader = new Scanner(myRead);
                        ArrayList<String> fileContent = r.read(myReader);
                        StringBuilder numbers = new StringBuilder();
                        ArrayList<String> numbersList = new ArrayList<>();
                        myReader.close();
                        while (true) {
                            boolean found = false;
                            String searchE;
                            while (!f.isPressed()) {
                                Thread.sleep(100);
                            }
                            String search = capitalize(f.input);
                            f.setPressed(false);
                            int start = fileContent.indexOf(search);
                            int count = 0;
                            if (start == -1) {
                                buttonActions(f, "Person not found, click 'Ok' to try again", RED);
                                f.setConsoleOutput("> Please enter a name", GREEN);
                            }
                            else {
                                for (String i : fileContent.subList(start, fileContent.size())) {
                                    if (found) {
                                        if (testType(i)) {
                                            count++;
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
                                String p;
                                if (search.endsWith("s")) {
                                    searchE = search + "'";
                                } else {
                                    searchE = search + "'s";
                                }

                                if (numbersList.size() > 1) {
                                    searchE += " numbers are: ";
                                    p = "them";
                                } else {
                                    searchE += " number is: ";
                                    p = "it";
                                }

                                f.label1.setText("<html>" + searchE + numbers + "<br/" + "Do you want to modify "
                                        + p + "? M/N" + "<br/>" + " <html/>");
                                choice = f.setChoiceBox(new ArrayList<>(Arrays.asList("No", "Yes")));
                                if (choice.equals("Yes")) {
                                    f.label1.setText("<html>" + searchE + numbers + "<br/" + "Choose your option from the list:" +
                                            "<br/>" + "- Modify a number <br/>" + "- Add a number <br/>" + "- Remove a number <br/>"
                                            + "- Delete the person <br/>" + " <html/>");
                                    choice = f.setChoiceBox(new ArrayList<>(Arrays.asList("modify", "add", "remove", "delete")));
                                    myRead = new File(r.getFilename());
                                    myReader = new Scanner(myRead);
                                    String filename = w.getFilename();
                                    ArrayList<String> filecontent = r.read(myReader);
                                    myReader.close();
                                    switch (choice) {
                                        case "modify" -> {
                                            f.label1.setText("<html>" + searchE + " numbers are: " + numbers + "<html/>"); // print the numbers with an associated number
                                            int hardCount = 1;
                                            ArrayList<String> countNumb = new ArrayList<>();
                                            while (hardCount <= count) {
                                                countNumb.add(Integer.toString(hardCount));
                                                hardCount++;
                                            }
                                            int posN = Integer.parseInt(f.setChoiceBox(countNumb));
                                            for (int per = fileContent.indexOf(search); per < filecontent.size(); per++) {
                                                if (fileContent.get(per).equals(numbersList.get(posN - 1))) {
                                                    //then you modify this one filecontent.set(per)
                                                    f.setConsoleOutput("> Enter the new number", GREEN);
                                                    while (!f.isPressed()) {
                                                        Thread.sleep(100);
                                                    }
                                                    String newNumber = f.input;
                                                    fileContent.set(per, newNumber);
                                                    myWriter = new FileWriter(filename, false);
                                                    filecontentStr = new StringBuilder();
                                                    for (String a : fileContent) {
                                                        filecontentStr.append(a).append("\n");
                                                    }
                                                    w.write(filecontentStr.toString(), myWriter);
                                                    myWriter.close();
                                                    break;
                                                }
                                            }
                                        } // not yet fixed; case of duplicate numbers
                                        case "add" -> {
                                            found = false;
                                            start = filecontent.indexOf(search);
                                            for (String line : filecontent.subList(start, filecontent.size())) {
                                                if (found) {
                                                    if (!testType(line)) {
                                                        myWriter = new FileWriter(filename, false); // if contained need to rewrite everything
                                                        int pos = filecontent.indexOf(line);
                                                        f.label1.setText("Enter the number you wish to add");
                                                        while (!f.isPressed()) {
                                                            Thread.sleep(100);
                                                        }
                                                        String number = f.input;
                                                        filecontent.add(pos, number);
                                                        f.setPressed(false);
                                                        filecontentStr = new StringBuilder();
                                                        for (String k : filecontent) {
                                                            filecontentStr.append(k).append("\n");
                                                        }
                                                        w.write(filecontentStr.toString(), myWriter);
                                                        displayWrite(f, w);
                                                        myWriter.close();
                                                        break;
                                                    }
                                                }
                                                if (search.equals(line)) {
                                                    found = true;
                                                }
                                            }
                                            if (found) {
                                                myWriter = new FileWriter(filename, false); // if contained need to rewrite everything
                                                int pos = filecontent.size();
                                                f.label1.setText("Enter the number you wish to add");
                                                while (!f.isPressed()) {
                                                    Thread.sleep(100);
                                                }
                                                String number = f.input;
                                                filecontent.add(pos, number);
                                                f.setPressed(false);
                                                filecontentStr = new StringBuilder();
                                                for (String k : filecontent) {
                                                    filecontentStr.append(k).append("\n");
                                                }
                                                w.write(filecontentStr.toString(), myWriter);
                                                displayWrite(f, w);
                                                myWriter.close();
                                            }
                                        } // working
                                        case "remove" -> {
                                            f.label1.setText("<html>" + searchE + " numbers are: " + numbers + "<html/>"); // print the numbers with an associated number
                                            int hardCountR = 1;
                                            ArrayList<String> countNumbR = new ArrayList<>();
                                            while (hardCountR <= count) {
                                                countNumbR.add(Integer.toString(hardCountR));
                                                hardCountR++;
                                            }
                                            int posNR = Integer.parseInt(f.setChoiceBox(countNumbR));
                                            filecontent.remove(numbersList.get(posNR - 1)); // problem if there are 2 numbers that are the same
                                            f.setConsoleOutput("> Number removed", GREEN);
                                            Thread.sleep(500);
                                            myWriter = new FileWriter(filename, false);
                                            filecontentStr = new StringBuilder();
                                            for (String o : filecontent) {
                                                filecontentStr.append(o).append("\n");
                                            }
                                            w.write(filecontentStr.toString(), myWriter);
                                            myWriter.close();
                                        } // not yet fixed; case of duplicate numbers
                                        case "delete" -> {
                                            found = false;
                                            filecontentStr = new StringBuilder();
                                            myWriter = new FileWriter(filename, false);
                                            for (String content : filecontent) {
                                                if (found) {
                                                    if (!testType(content)) {
                                                        found = false;
                                                    }
                                                }
                                                if (content.equals(search)) {
                                                    found = true;
                                                }
                                                if (!found) {
                                                    filecontentStr.append(content).append("\n");
                                                }
                                            }
                                            w.write(filecontentStr.toString(), myWriter);
                                            displayWrite(f, w);
                                            myWriter.close();
                                            f.setConsoleOutput("> That person has been successfully deleted", GREEN);
                                            Thread.sleep(500);
                                        } // working
                                    }
                                }
                                break switchloop;
                            }
                        }
                    }

                    case "3" -> { // Get number of persons in file, and fetch names if needed
                        File myRead = new File(r.getFilename());
                        Scanner myReader = new Scanner(myRead);
                        ArrayList<String> fileContent = r.read(myReader);
                        ArrayList<String> names = new ArrayList<>();
                        myReader.close();
                        int count = 0;
                        for (String s : fileContent) {
                            if (!testType(s)){
                                names.add(s);
                                count++;
                            }
                        }
                        f.label1.setText("<html> The number of persons stored is: " + count + " <br/> Do you want to know their names? Y/N </html>");
                        String name = f.setChoiceBox(new ArrayList<>(Arrays.asList("Y", "N")));
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

                    case "4" -> createFile(f, w ,r); // Create a new file

                    case "5" -> { // Breaks the Menu loop to change file
                        break menuloop;
                    }

                    case "6" -> {
                        File myRead = new File(r.getFilename());
                        Scanner myReader = new Scanner(myRead);
                        ArrayList<String> fileContent = r.read(myReader);
                        StringBuilder filecontent = new StringBuilder();
                        filecontent.append("<html>");
                        boolean first = true;
                        for (String line : fileContent) {
                            if (!testType(line) && !first) {
                                filecontent.append("<br/><br/>").append(line).append(" :");
                            }
                            if (first) {
                                filecontent.append("List of Names:<br/><br/>").append(line).append(" :");
                                first = false;
                            }
                            if (testType(line)) {
                                filecontent.append("<br/>- ").append(line);
                            }
                        }
                        filecontent.append("</html>");
                        fileGUI fg = new fileGUI(w.getFilename());
                        fg.setVisible(true);
                        fg.display.setText(String.valueOf(filecontent));
                    }
                }
            }
        }
    }
}
