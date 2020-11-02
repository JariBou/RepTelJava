package fr.jari.RepTelJava.java;

import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class Read {
    private String filename;

    /**
     * @param filename filename
     * @implNote Sets the file that will be used
     */
    public void setFilename(String filename) { // Sets the filename
        this.filename = filename + ".txt";
    }

    /**
     * @return Filename
     * @implNote Returns the filename of the current used file
     */
    public String getFilename() { // Returns the filename
        return this.filename;
    }

    /**
     * Returns the content of the file
     *
     * @param myReader Scanner instance
     * @return fileContent - Content of the file as ArrayList
     */
    public ArrayList<String> read(Scanner myReader) { // Returns the content of the file as an ArrayList<String>
        ArrayList<String> fileContent = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            fileContent.add(data);
        }
        myReader.close();
        return fileContent;
    }
}