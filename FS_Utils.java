package com.big.firb;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FS_Utils {
    /**
     * Hold the path of the directory to be scanned
     */
    private static File ROOT_DIR;
    /**
     * The log file
     */
    private static String LOG_FILE="C:\\Users\\matteo.francia3\\Documents\\Developing\\Java\\SearchClipIDinJSONname\\SearchClipIDinJSONname_LOG.txt";
    /**
     * The dir where the matching file will be copied
     */
    private static String OUTPUT_DIR="C:\\Users\\matteo.francia3\\Documents\\Developing\\Java\\SearchClipIDinJSONname\\Extracted_Files";

    private static String[] ID_TO_SEARCH;

    /**
     * Instantiate a new FS_Utils object to recursively scan fs dirs.
     * @param root_dir the host file system full path to the directory to scan
     */
    public FS_Utils(String root_dir,String[] idToSearch){
        try{
            ROOT_DIR=new File(root_dir);

        }
        catch(NullPointerException npe){
            npe.printStackTrace();
        }

        ID_TO_SEARCH=idToSearch;
    }

    /**
     * Getter for the ROOT_DIR name
     *
     * @return the path of the actual ROOT_DIR
     */
    public String getRootDir() {
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
        File actualdir =new File(dir);
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
     * Recursivey scan a zipped file and copy out a single file that match
     * with a given criteria
     *
     * @param file The zipped file
     * @throws IOException if the file is not found
     */
    private void scanZip(File file) throws IOException{
        FileInputStream fin = new FileInputStream(this.getRootDir()+"\\"+file.getName());
        BufferedInputStream bin = new BufferedInputStream(fin);
        ZipInputStream zin = new ZipInputStream(bin);
        ZipEntry ze = null;

        this.logElement("Scanning "+file.getName()+"...");

        for(int i=0;i<ID_TO_SEARCH.length;i++){
            boolean found = false;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().contains(ID_TO_SEARCH[i])) {
                    OutputStream out = new FileOutputStream(OUTPUT_DIR+ze.getName().split("/")[1]);
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zin.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                    found=true;
                    this.logElement(ID_TO_SEARCH[i]+" ==> "+found);
                    break;
                }
            }
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
