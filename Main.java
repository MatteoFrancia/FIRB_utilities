package com.big.firb;

import java.io.File;

public class Main {
    static String FILE_DIR="C:\\Users\\matteo.francia3\\Desktop\\TestDir";

    public static void main(String[] args) {
        File currentDir=new File(FILE_DIR);

        File[] dirElements=currentDir.listFiles();
        for(int i=0;i<dirElements.length;i++){
            System.out.println("["+i+"]"+dirElements[i]);
            System.out.println("\t\t[IsDir] ==>"+dirElements[i].isDirectory());
            System.out.println("\t\t[IsFile] ==>" + dirElements[i].isFile());
            System.out.println("\t\t[IsZip] ==>" + (dirElements[i].getName().endsWith(".zip") || dirElements[i].getName().endsWith(".rar")));
        }



    }
}
