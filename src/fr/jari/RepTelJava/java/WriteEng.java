package fr.jari.RepTelJava.java;

import java.awt.*;
import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.nio.file.Path;

public class WriteEng {
    private String filename;
    public String output;
    public Color color;

    /**
     * @param filename filename
     * @implNote Sets the file that will be used
     */
    public void setFilename(String filename){ // Sets the filename
        this.filename = filename + ".txt";
    }

    /**
     * @return Filename
     * @implNote Returns the filename of the current used file
     */
    public String getFilename(){ // Returns the filename
        return this.filename;
    }

    // Default set
    public void create(String filename) {
        create(filename, null);
    }

    /**
     * @param filename The name of the file to be created
     * @param path The path of the file to be created, if none the the file is created in the current .jar directory
     */
    public void create(String filename, Path path) { // Creates a new file, handles pre-existing files
        try {
            File myObj;
            if (path != null) {
                myObj = new File(path + ".txt");
            } else {
                myObj = new File(filename + ".txt");

            }
            if (myObj.createNewFile()) {
                this.color = Color.green;
                this.output = "File created: " + myObj.getName();
                System.out.println("File created: " + myObj.getName());
            } else {
                this.color = Color.red;
                this.output = "File " + myObj.getName() + " already exists";
                System.out.println("File already exists.");
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            this.color = Color.red;
            this.output = "An error occurred";
            e.printStackTrace();
        } if (path!=null) {
            this.filename = filename + ".txt";
        }
    }

    /**
     * Function to write in a file
     *
     * @param content Content to be written
     * @param myWriter FileWriter instance
     */
    public void write(String content, FileWriter myWriter) { // Writes inside the file
        try {
            myWriter.write(content);
            System.out.println("Successfully wrote to the file.");
            this.color = Color.green;
            this.output = "Successfully wrote to the file.";
        } catch (IOException e) {
            System.out.println("An error occurred.");
            this.color = Color.red;
            this.output = "An error occurred";
            e.printStackTrace();
        }
    }
}
