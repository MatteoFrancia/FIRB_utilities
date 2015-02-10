package com.big.firb;

import java.util.Scanner;

public class Main {
    private static String DEFAULT_DIR="C:\\Users\\matteo.francia3\\Desktop\\TestDir";

    public static void main(String[] args) {
        Scanner user_input = new Scanner( System.in );
        System.out.print("[Enter root dir to search:] ");

        FS_Utils fs_util;

        if(user_input.next().equals("")){
            fs_util=new FS_Utils(DEFAULT_DIR);
        }
        else{
            fs_util=new FS_Utils(user_input.next());
        }






    }
}
