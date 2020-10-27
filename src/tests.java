import fr.jari.RepTelJava.java.Read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class tests {
    public static void main(String[] args) throws FileNotFoundException {

        // Read test to make possible the detection of number/name to allow multi-number support
        Read r = new Read();
        r.setFilename("test");
        File myRead = new File(r.getFilename());
        Scanner myReader = new Scanner(myRead);
        ArrayList<String> fileContent = r.read(myReader);
        System.out.println("Enter the person's name: ");
        boolean found = false;
        String search = "tom";
        StringBuilder numbers = new StringBuilder();
        for (String s : fileContent) {
            System.out.println(s);
            if (found) {
                try {
                    Double.parseDouble(s);
                    numbers.append(" - ").append(s);

                } catch (NumberFormatException ignored) {
                }
            }

            if (search.equals(s)) {
                found = true;
            }

        }
        System.out.println("<html> "+numbers+" <html/>");
        if (!found) {
            System.out.println("Person not found");
        }




    }
}




