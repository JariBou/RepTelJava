package fr.jari.RepTelJava.java;

import java.util.Locale;
import java.util.ResourceBundle;

public class LangFunctions {
    private ResourceBundle rb;
    public String language;
    public String country;

    public String getLanguage(){
        return this.language;
    }
    public void setLanguage(String language){
        this.language = language;
    }

    public String getCountry(){
        return this.country;
    }
    public void setCountry(String country){
        this.country = country;
    }


    public void setLang(String language, String country){
        Locale.setDefault(new Locale(language, country));
        this.rb = ResourceBundle.getBundle("lang/bank/language"); setLanguage(language);
    }

    public String get(String key){
        return rb.getString(key);
    }


}
