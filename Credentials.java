package com.big.firb;

public class Credentials {
    private static String db_sid_ita="";
    private static String db_sid_eng="";
    private static String db_sid_deu="";
    private static String db_username="";
    private static String db_password="";

    public static String[] getCredentials(int lang)
    {
        String[] credentials = new String[]{db_username,db_password,""};

        switch (lang) {
            case 1:  lang = 1; //ITA
                credentials[2]=db_sid_ita;
                break;
            case 2:  lang = 2; //ENG
                credentials[2]=db_sid_eng;
                break;
            case 3:  lang = 3; //DEU
                credentials[2]=db_sid_deu;
                break;
            default:
                break;
        }

        return credentials;
    }
}
