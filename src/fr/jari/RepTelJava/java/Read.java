package fr.jari.RepTelJava.java;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class Read {
    private String filename;
    public String output;
    public Color color;

    public void setFilename(String filename) {
        this.filename = filename + ".txt";
    }

    public String getFilename() {
        return this.filename;
    }

    public ArrayList<String> read(Scanner myReader) {
        ArrayList<String> fileContent = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            fileContent.add(data);
        }
        myReader.close();
        return fileContent;
    }
}