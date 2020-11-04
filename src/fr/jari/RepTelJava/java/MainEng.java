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
        f.consoleOutput.setText("> " + w.output); // for the lang implementation:  lang.get(w.output)
        w.output = "";                            // Basically w.output return the key to read the property files
    }

    // Default setup
    public static void buttonActions(mainGUI f, LangFunctions lang) throws InterruptedException {
        buttonActions(f, null, Color.green, lang);
    }

    // Button interactions
    public static void buttonActions(mainGUI f, String text, Color color, LangFunctions lang) throws InterruptedException {
        f.consoleOutput.setForeground(color);
        if (text != null) {
            f.consoleOutput.setText("> " + text);
        } else {
            f.consoleOutput.setText("> " + lang.get("button_console"));
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
        System.out.println(args.length);
        LangFunctions lang = new LangFunctions();
        lang.setLanguage(language); lang.setCountry(country);
        lang.setLang(language, country);

        Color GREEN = Color.green;
        Color RED = Color.red;
        // GUI init
        mainGUI f = new mainGUI(language, country);
        f.setVisible(true);
        f.setPressed(false);
        // Write and Read init
        WriteEng w = new WriteEng(language, country);
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
                files.append("<br/ ").append(lang.get("no_files"));
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
                    String newFile = f.setChoiceBox(new ArrayList<>(Arrays.asList(lang.get("yes"), lang.get("no"))), false);
                    if (newFile.equals(lang.get("yes"))) {
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
                // Here we change the language if changed in the menu bar
                lang.setLang(lang.getLanguage(), lang.getCountry());


                // Menu display
                f.label2.setText(lang.get("curr_file") + w.getFilename());
                f.label1.setText(lang.get("main_menu"));
                f.consoleOutput.setText("> ");
                //------------------------------------------------

                while (!f.isPressed()) {
                    Thread.sleep(100);
                    if (f.langChange()){
                        lang.setLang(f.getLang(), f.getCountry());
                        lang.setLanguage(f.getLang()); lang.setCountry(f.getCountry());
                        w.setLanguage(f.getLang(), f.getCountry());
                        f.label2.setText(lang.get("curr_file") + w.getFilename());
                        f.label1.setText(lang.get("main_menu"));
                        f.rstLangChange();
                    }
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
                            f.label1.setText(lang.get("enter_name0"));
                            while (!f.isPressed()) {
                                Thread.sleep(100);
                            }
                            String name = capitalize(f.input);
                            if (name.equals("0")) {
                                break;
                            }
                            f.label1.setText(lang.get("enter_name1") + name);
                            ArrayList<String> filecontent = r.read(myReader);
                            myReader.close();
                            if (filecontent.contains(name)) {
                                String add = f.setChoiceBox(new ArrayList<>(Arrays.asList(lang.get("yes"), lang.get("no"))), "> " + lang.get("name_assigned"), RED);

                                if (!lang.getLanguage().equals("eng")){
                                    if (add.equals(lang.get("yes"))){ add = "yes";}
                                    else if (add.equals(lang.get("no"))){ add = "no";}
                                }

                                f.setPressed(false);
                                if (add.equals(lang.get("yes"))) {
                                    FileWriter myWriter = new FileWriter(filename, false); // if contained need to rewrite everything
                                    boolean found = false;
                                    int start = filecontent.indexOf(name);
                                    for (String i : filecontent.subList(start, filecontent.size())) {
                                        if (found) {
                                            if (!testType(i)) {
                                                int pos = filecontent.indexOf(i);
                                                f.consoleOutput.setText("> ");
                                                f.label1.setText("<html> " + f.label1.getText() + lang.get("enter_number"));
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
                                f.label1.setText("<html> " + f.label1.getText() + lang.get("enter_number"));
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
                        f.label1.setText(lang.get("search_pers")); // add smth to chnge the text displayed when there is only 1 number
                        File myRead = new File(r.getFilename());
                        Scanner myReader = new Scanner(myRead);
                        ArrayList<String> fileContent = r.read(myReader);
                        StringBuilder numbers = new StringBuilder();
                        ArrayList<String> numbersList = new ArrayList<>();
                        myReader.close();
                        while (true) {
                            boolean found = false;
                            String searchE = "";
                            while (!f.isPressed()) {
                                Thread.sleep(100);
                            }
                            String search = capitalize(f.input);
                            f.setPressed(false);
                            int start = fileContent.indexOf(search);
                            int count = 0;
                            if (start == -1) {
                                buttonActions(f, lang.get("button_persNotF"), RED, lang);
                                f.setConsoleOutput("> " + lang.get("enter_name"), GREEN);
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
                                String p, q;
                                switch (lang.getLanguage()){

                                    case "eng"-> {
                                        if (search.endsWith("s")) {
                                            searchE = search + "'";
                                        } else {
                                            searchE = search + "'s";
                                        }
                                        if (numbers.length() > 1) {
                                            p = "them?";
                                            q = " numbers are:";
                                        } else {
                                            p = "it?";
                                            q = " number is";
                                        }

                                        f.label1.setText("<html>" + searchE + q + numbers + "<br/" + "Do you want to modify "
                                                + p + "<br/> <html/>");
                                    }

                                    case "fr" -> {
                                        if (numbers.length() > 1) {
                                            p = "les";
                                            q = " sont:";
                                        } else {
                                            p = "le";
                                            q = " est:";
                                        }
                                        searchE = search;
                                        f.label1.setText("<html>" + capitalize(p) + " numéros de " + search + q + numbers + "<br/" + "Voulez-vous "
                                                + p + " modifier?" + "<br/> <html/>");
                                    }
                                }

                                choice = f.setChoiceBox(new ArrayList<>(Arrays.asList(lang.get("no"), lang.get("yes"))));

                                if (!choice.equals("eng")){if (choice.equals(lang.get("yes"))){choice = "Yes";}}

                                if (choice.equals("Yes")){
                                    f.label1.setText("<html>" + search + numbers + "<br/" + lang.get("choose_opt") +
                                            "<br/>" + lang.get("mod_numb")  + lang.get("add_numb") + lang.get("rem_number")
                                            + lang.get("delete_pers") + " <html/>");
                                } else {
                                    break switchloop;
                                }

                                choice = f.setChoiceBox(new ArrayList<>(Arrays.asList(lang.get("modify"), lang.get("add"), lang.get("remove"), lang.get("delete"))));
                                if (!lang.getLanguage().equals("eng")){
                                    if (choice.equals(lang.get("modify"))){ choice = "modify";}
                                    else if (choice.equals(lang.get("add"))){choice = "add";}
                                    else if (choice.equals(lang.get("remove"))){choice = "remove";}
                                    else if (choice.equals(lang.get("delete"))){choice = "delete";}
                                } else{
                                    choice = choice.toLowerCase();
                                }

                                myRead = new File(r.getFilename());
                                myReader = new Scanner(myRead);
                                String filename = w.getFilename();
                                ArrayList<String> filecontent = r.read(myReader);
                                myReader.close();
                                switch (choice) {
                                    case "modify" -> {
                                        if (lang.getLanguage().equals("eng")) {
                                            f.label1.setText("<html>" + searchE + " numbers are: " + numbers + "<html/>"); // print the numbers with an associated number
                                        } else {
                                            f.label1.setText("<html>Les numéros de " + searchE + " sont: " + numbers + "<html/>");
                                        }
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
                                                f.setConsoleOutput("> " + lang.get("enter_newNumber"), GREEN);
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
                                                    f.label1.setText(lang.get("add_number"));
                                                    while (!f.isPressed()) {
                                                        Thread.sleep(100);
                                                    }
                                                    String number = f.input;
                                                    filecontent.add(pos, number);
                                                    f.setPressed(false);
                                                    filecontentStr = new StringBuilder();
                                                    for (String k : filecontent) {
                                                        filecontentStr.append(k).append("\n");
                                                        System.out.println("Building String");
                                                    }
                                                    w.write(filecontentStr.toString(), myWriter);
                                                    displayWrite(f, w);
                                                    myWriter.close();
                                                    System.out.println("Breaking");
                                                    break switchloop;
                                                }
                                            }
                                            if (search.equals(line)) {
                                                found = true;
                                            }
                                        }
                                        if (found) {
                                            myWriter = new FileWriter(filename, false); // if contained need to rewrite everything
                                            int pos = filecontent.size();
                                            f.label1.setText(lang.get("add_number"));
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
                                        if (lang.getLanguage().equals("eng")) {
                                            f.label1.setText("<html>" + searchE + " numbers are: " + numbers + "<html/>"); // print the numbers with an associated number
                                        } else {
                                            f.label1.setText("<html>Les numéros de " + searchE + " sont: " + numbers + "<html/>");
                                        }
                                        int hardCountR = 1;
                                        ArrayList<String> countNumbR = new ArrayList<>();
                                        while (hardCountR <= count) {
                                            countNumbR.add(Integer.toString(hardCountR));
                                            hardCountR++;
                                        }
                                        int posNR = Integer.parseInt(f.setChoiceBox(countNumbR));
                                        filecontent.remove(numbersList.get(posNR - 1)); // problem if there are 2 numbers that are the same
                                        f.setConsoleOutput("> " + lang.get("number_removed"), GREEN);
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
                                        f.setConsoleOutput("> " + lang.get("delete_success"), GREEN);
                                        Thread.sleep(500);
                                    } // working
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
                        f.label1.setText("<html>" + lang.get("number_pStored") + count + lang.get("know_names") + "<br/></html>");
                        String name = f.setChoiceBox(new ArrayList<>(Arrays.asList(lang.get("yes"), lang.get("no"))));
                        if (!lang.getLanguage().equals("eng")){
                            if (name.equals(lang.get("yes"))){ name = "yes";}
                        }
                        if (name.equals("yes")) {
                            StringBuilder strNames = new StringBuilder();
                            for (String s : names) {
                                strNames.append(" <br/> - ").append(s);
                            }
                            f.label1.setText("<html>"+ lang.get("their_names") + strNames + "<html/>");      // When too much names problem, they all on same line, and they appear as lists
                            buttonActions(f, lang);
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
                                filecontent.append(lang.get("list_of_names")).append("<br/><br/>").append(line).append(" :");
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
