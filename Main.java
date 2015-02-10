package com.big.firb;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static String DEFAULT_DIR="C:\\Users\\matteo.francia3\\Desktop\\TestDir";

    public static void main(String[] args) {
        Scanner user_input = new Scanner( System.in );
        System.out.print("Enter root dir to search (type \"1\" for default): ");

        FS_Utils fs_util;

        if(user_input.next().equals("1")){
            fs_util=new FS_Utils(DEFAULT_DIR);
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
