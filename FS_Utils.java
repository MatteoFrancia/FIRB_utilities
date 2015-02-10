package com.big.firb;

import java.io.File;

public class FS_Utils {
    private static File ROOT_DIR;

    /**
     * Instantiate a new FS_Utils object to recursively scan fs dirs.
     * @param root_dir the host file system full path to the directory to scan
     */
    public FS_Utils(String root_dir) {
        try{
            ROOT_DIR=new File(root_dir);
        }
        catch(NullPointerException npe){
            System.out.println("ATTENZIONE!\n\nLa directory inserita non è valida!");
        }
    }

    /**
     * Getter for the ROOT_DIR name
     *
     * @return the path of the actual ROOT_DIR
     */
    public String getRootDir()
    {
        return ROOT_DIR.getName();
    }

//    File[] dirElements=currentDir.listFiles();
//    for(int i=0;i<dirElements.length;i++){
//        System.out.println("["+i+"]"+dirElements[i]);
//        System.out.println("\t\t[IsDir] ==>"+dirElements[i].isDirectory());
//        System.out.println("\t\t[IsFile] ==>" + dirElements[i].isFile());
//        System.out.println("\t\t[IsZip] ==>" + (dirElements[i].getName().endsWith(".zip") || dirElements[i].getName().endsWith(".rar")));
//    }
}
