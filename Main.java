package com.big.firb;

import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    /**
     * Where all the JSON already written on db are stored and compressed
     */
    private static String DEFAULT_REMOTE_DIR="\\\\137.204.74.121\\c$\\BrandwatchZIP\\ENGJSONPROCESSED";

    private static String JSON_REMOTE_DIR="\\\\137.204.74.121\\c$\\BrandwatchZIP\\ENG_STAGING";

    private static int STANDARD_MODE=1;

    private static int COUNT_MODE=2;

    /**
     * Test directory
     */
    private static String TEST_DIR="C:\\Users\\matteo.francia3\\Desktop\\TestDir";

    private static String[] TEST_ARRAY = new String[]{"191354","123456","321654","098876","865543"};


    /**
     * Entry point: launch iteration according to the user input
     *
     * @param args not handled yet
     */
    public static void main(String[] args) {
        Scanner user_input = new Scanner( System.in );
        // System.out.print("Enter root dir to search \n\t(type \"1\" for default or \"2\" for default on 137.204.74.121): ");
        System.out.print("Would you like to:\n[1] search not analyzed clip in JSON stored file\n[2] extract clip ID from JSON file name?\n[3] count stored JSON files\n");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        FS_Utils fs_util=null;

        String[] credentials = null;
        String db_sid, db_username, db_password;
        Connection conn = null;

        switch (user_input.next().toLowerCase()){
            case "1":
                System.out.print("Which language would you like to search? \n\t\t- \"1\" for ITA\n\t\t- \"2\" for ENG\n\t\t- \"3\" for DEU\n ");
                // Instantiate credentials according to the language chosen by the user
                credentials = Credentials.getCredentials(Integer.parseInt(user_input.next()));
                db_sid=credentials[2];
                db_username=credentials[0];
                db_password=credentials[1];

                // Instantiate a connection to Oracle
                conn = DbUtils.connect(db_sid,db_username,db_password);

                // Launch search and log times on console
                System.out.println("LAUNCH QUERY ==> "+dateFormat.format(new Date()));

                String[] clipID = DbOperations.getClipID(conn);
                //String[] clipID = TEST_ARRAY;

                System.out.println("QUERY EXECUTED ==> "+dateFormat.format(new Date()));
                System.out.println("CLIP ID RETRIEVED ==> "+clipID.length);

                fs_util=new FS_Utils(DEFAULT_REMOTE_DIR,clipID);

                System.out.println("LAUNCH SEARCH ==> "+dateFormat.format(new Date()));
                try {
                    fs_util.scan(STANDARD_MODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("SEARCH EXECUTED ==> "+dateFormat.format(new Date()));

                break;
            case "2":
                fs_util=new FS_Utils(JSON_REMOTE_DIR);
                ArrayList<String> clipIDs;

                System.out.println("LAUNCH EXTRACTION ==> "+dateFormat.format(new Date()));
                clipIDs = fs_util.extractIDFromFileName();
                System.out.println("EXTRACTION COMPLETED ("+clipIDs.size()+")elements extracted) ==> "+dateFormat.format(new Date()));

                // Instantiate credentials according to the language chosen by the user
                int ita=1;
                int eng=2;
                int deu=3;
                credentials = Credentials.getCredentials(eng);
                db_username=credentials[0];
                db_password=credentials[1];
                db_sid=credentials[2];

                // Instantiate a connection to Oracle
                conn = DbUtils.connect(db_sid,db_username,db_password);

                System.out.println("LAUNCH CLIP CHECK ==> "+dateFormat.format(new Date()));
                fs_util.checkClipStatus(conn,clipIDs);
                System.out.println("CLIP CHECK COMPLETED ==> "+dateFormat.format(new Date()));

                break;
            case "3":
                fs_util=new FS_Utils(DEFAULT_REMOTE_DIR);

                System.out.println("LAUNCH COUNT ==> "+dateFormat.format(new Date()));
                try {
                    fs_util.scan(COUNT_MODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("COUNT EXECUTED ==> "+dateFormat.format(new Date()));


            default:
                System.out.println("[Errore leggendo l'input dell'utente]");
        }
}
}
