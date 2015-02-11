package com.big.firb;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    /**
     * Where all the JSON already written on db are stored and compressed
     */
    private static String DEFAULT_REMOTE_DIR="\\\\137.204.74.121\\c$\\BrandwatchZIP\\ENGJSONPROCESSED";
    /**
     * Test directory
     */
    private static String DEFAULT_DIR="C:\\Users\\matteo.francia3\\Desktop\\TestDir";


    /**
     * Entry point: launch iteration according to the user input
     *
     * @param args not handled yet
     */
    public static void main(String[] args) {
        Scanner user_input = new Scanner( System.in );
        // System.out.print("Enter root dir to search \n\t(type \"1\" for default or \"2\" for default on 137.204.74.121): ");
        System.out.print("Which langage would you like to search? \n\t\t- \"1\" for ITA\n\t\t- \"2\" for ENG\n\t\t- \"3\" for DEU\n ");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Instantiate credentials according to the language chosen by the user
        String[] credentials = Credentials.getCredentials(Integer.parseInt(user_input.next()));
        String db_sid=credentials[2];
        String db_username=credentials[0];
        String db_password=credentials[1];

        // Instantiate a connection to Oracle
        Connection conn = DbUtils.connect(db_sid,db_username,db_password);

        // Launch search and log times on console
        System.out.println("LAUNCH QUERY ==> "+dateFormat.format(new Date()));

        String[] clipID = DbOperations.getClipID(conn);

        System.out.println("QUERY EXECUTED ==> "+dateFormat.format(new Date()));
        System.out.println("CLIP ID RETRIEVED ==> "+clipID.length);

        FS_Utils fs_util;




        /*FS_Utils fs_util;

        if(user_input.next().equals("1")){
            fs_util=new FS_Utils(DEFAULT_DIR);
        }
        else if(user_input.next().equals("2")){
            fs_util=new FS_Utils(DEFAULT_REMOTE_DIR);
        }
        else{
            fs_util=new FS_Utils(user_input.next());
        }

        try {
            fs_util.scan();
        } catch (IOException e) {
            e.printStackTrace();
        }*/



    }
}
