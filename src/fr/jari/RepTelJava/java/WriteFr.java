package fr.jari.RepTelJava.java;

import java.awt.*;
import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.nio.file.Path;

public class WriteFr {
    private String filename;
    public String output;
    public Color color;

    public void setFilename(String filename){ // Sets the filename
        this.filename = filename + ".txt";
    } public String getFilename(){ // Returns the filename
        return this.filename;
    }

    public void create(String filename) {
        create(filename, null);
    }
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
                this.output = "Fichier créé: " + myObj.getName();
                System.out.println("File created: " + myObj.getName());
            } else {
                this.color = Color.red;
                this.output = "Le fichier " + myObj.getName() + " existe déjà";
                System.out.println("File already exists.");
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            this.color = Color.red;
            this.output = "Erreur";
            e.printStackTrace();
        } if (path!=null) {
            this.filename = filename + ".txt";
        }
    }

    public void write(String content, FileWriter myWriter) { // Writes inside the file
        try {
            myWriter.write(content);
            System.out.println("Successfully wrote to the file.");
            this.color = Color.green;
            this.output = "Ajouté avec succès au fichier";
        } catch (IOException e) {
            System.out.println("An error occurred.");
            this.color = Color.red;
            this.output = "Erreur";
            e.printStackTrace();
        }
    }
}
