package fr.jari.RepTelJava.java;

import java.awt.*;
import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors

public class Write{
    private String filename;
    public String output;
    public Color color;
    public void setFilename(String filename){
        this.filename = filename + ".txt";
    } public String getFilename(){
        return this.filename;
    }

    public void create(String filename) {
        try {
            File myObj = new File(filename + ".txt");
            if (myObj.createNewFile()) {
                this.color = Color.green;
                this.output = "File created: " + myObj.getName();
                System.out.println("File created: " + myObj.getName());
            } else {
                this.color = Color.red;
                this.output = "File " + myObj.getName() + " already exists";
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            this.color = Color.red;
            this.output = "An error occurred";
            e.printStackTrace();
        } this.filename = filename + ".txt";
    }


    public void write(String content, FileWriter myWriter) {
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
