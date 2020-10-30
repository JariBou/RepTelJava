package fr.jari.RepTelJava.java;


public class LanguageStore {

    mainGUI f = new mainGUI();


    public String getLangText(String what){

        switch (what){

            case "OkB":
                if (f.getLanguage().equals("fr")){
                    return "Cliquez sur le boutton 'Ok' pour retourner au menu";
                } if (f.getLanguage().equals("en")){
                    return "Click on the 'Ok' button to exit to Menu";
                }





        }
        return "ok";
    }

}
