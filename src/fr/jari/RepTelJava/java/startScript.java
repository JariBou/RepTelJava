package fr.jari.RepTelJava.java;

import java.io.IOException;

public class startScript {

    public static void main(String[] args) throws InterruptedException, IOException {
        startGui st = new startGui();
        st.setVisible(true);
        String language = st.setChoiceBox();
        String lang = "eng";
        if (language.equals("Français")){lang = "fr";}
        st.setVisible(false);
        st.dispose();
        MainEng.main(args, lang, lang.toUpperCase());
    }
}
