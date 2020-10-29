import fr.jari.RepTelJava.java.Read;
import fr.jari.RepTelJava.java.mainGUI;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class tests {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        mainGUI frame = new mainGUI();
        frame.setVisible(true);
        ArrayList<String> list = new ArrayList<>();
        list.add("Y");
        list.add("N");
        String box = frame.setChoiceBox(list);
        switch (box) {
            case "Y" -> System.out.println("WTF");
            case "N" -> System.out.println("Nope");
        }






    }
}




