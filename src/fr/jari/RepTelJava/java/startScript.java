package fr.jari.RepTelJava.java;

import java.io.IOException;

public class startScript {

    public static void main(String[] args) throws InterruptedException, IOException {
        startGui st = new startGui();
        st.setVisible(true);
        String language = st.setChoiceBox();
        if (language.equals("Français")){
            st.setVisible(false);
            st.dispose();
            MainFr.main(args);
        }
        if (language.equals("English")){
            st.setVisible(false);
            st.dispose();
            MainEng.main(args);
        }
    }

}
