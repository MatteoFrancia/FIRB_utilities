package com.big.firb;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FS_Utils {
    /**
     * Hold the path of the directory to be scanned
     */
    private static File ROOT_DIR;
    /**
     * The log file
     */
    private static String LOG_FILE="C:\\Users\\matteo.francia3\\Desktop\\testlog.txt";

    /**
     * Instantiate a new FS_Utils object to recursively scan fs dirs.
     * @param root_dir the host file system full path to the directory to scan
     */
    public FS_Utils(String root_dir){
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
        return ROOT_DIR.getAbsolutePath();
    }

    /**
     * Recursively scan the ROOT_DIR directory
     * @throws IOException if the element is not found
     */
    public void scan() throws IOException {
        File[] dirElements=ROOT_DIR.listFiles();

        for(int i=0;i<dirElements.length;i++){
            if((dirElements[i].getName().endsWith(".zip") || dirElements[i].getName().endsWith(".rar"))){
                this.scanZip(dirElements[i]);
            }
            else if(dirElements[i].isDirectory()){
                this.scan(dirElements[i].getAbsolutePath());
            }else if(dirElements[i].isFile()){
                //do nothing
            }
        }
    }

    /**
     * Recursively scan the directory passed as a parameter to the method itself
     * @param dir the folder to scan
     * @throws IOException
     */
    public void scan(String dir) throws IOException {
        File actualdir = ROOT_DIR=new File(dir);
        File[] actualDirFiles = actualdir.listFiles();

        for(int i=0;i<actualDirFiles.length;i++){
            if((actualDirFiles[i].getName().endsWith(".zip") || actualDirFiles[i].getName().endsWith(".rar"))){
                this.scanZip(actualDirFiles[i]);
            }
            else if(actualDirFiles[i].isDirectory()){
                this.scan(actualDirFiles[i].getAbsolutePath());
            }else if(actualDirFiles[i].isFile()){
                //do nothing
            }
        }
    }

    /**
     * Recursivey scan a zipped file
     * @param file The zipped file
     * @throws IOException if the file is not found
     */
    private void scanZip(File file) throws IOException{
        ZipFile tmpZipFile = new ZipFile(this.getRootDir()+"\\"+file.getName());
        Enumeration zipEntries = tmpZipFile.entries();

        while (zipEntries.hasMoreElements()) {
            this.logElement(((ZipEntry) zipEntries.nextElement()).getName());
        }
    }

    /**
     * Log to file element retrieved by the scanning routines
     * @param element the element retrieved  (the one to log)
     * @throws IOException if the log fil is not found
     */
    private void logElement(String element) throws IOException{
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE, true)));
        writer.println(element);
        writer.close();
    }
}