package fr.jari.RepTelJava.java;

import java.util.Locale;
import java.util.ResourceBundle;

public class LangFunctions {
    private ResourceBundle rb;

    public ResourceBundle setLang(String language, String country){
        Locale.setDefault(new Locale(language, country));
        ResourceBundle rb = ResourceBundle.getBundle("lang/bank/language");
        this.rb = rb;
        return rb;
    }

    public String get(String key){
        return rb.getString(key);
    }


}
