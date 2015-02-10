package com.big.firb;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static String DEFAULT_REMOTE_DIR="\\\\137.204.74.121\\c$\\BrandwatchZIP\\ENGJSONPROCESSED";
    private static String DEFAULT_DIR="C:\\Users\\matteo.francia3\\Desktop\\TestDir";

    public static void main(String[] args) {
        Scanner user_input = new Scanner( System.in );
        System.out.print("Enter root dir to search \n\t(type \"1\" for default or \"2\" for default on 137.204.74.121): ");

        FS_Utils fs_util;

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
        }
        //System.out.println(fs_util.getRootDir());

    }
}
