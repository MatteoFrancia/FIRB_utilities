package com.big.firb;

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FS_Utils {
    /**
     * Hold the path of the directory to be scanned
     */
    private static File ROOT_DIR;
    /**
     * The SearchClipIDinJSONname procedure log file
     */
    private static String SEARCH_LOG_FILE="C:\\Users\\matteo.francia3\\Documents\\Developing\\Java\\SearchClipIDinJSONname\\SearchClipIDinJSONname_LOG.txt";
    /**
     * The countJSON procedure log file
     */
    private static String COUNT_LOG_FILE="C:\\Users\\matteo.francia3\\Documents\\Developing\\Java\\SearchClipIDinJSONname\\CountJSON_LOG.txt";
    /**
     * The clip id extraction from JSON log file
     */
    private static String EXTRACTID_LOG_FILE="C:\\Users\\matteo.francia3\\Documents\\Developing\\Java\\SearchClipIDinJSONname\\ExtractIDs_LOG.txt";
    /**
     * The dir where the matching file will be copied
     */
    private static String OUTPUT_DIR="C:\\Users\\matteo.francia3\\Documents\\Developing\\Java\\SearchClipIDinJSONname\\Extracted_Files\\";

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
     * Instantiate a new FS_Utils object to recursively scan fs dirs.
     * @param root_dir the host file system full path to the directory to scan
     */
    public FS_Utils(String root_dir){
        try{
            ROOT_DIR=new File(root_dir);

        }
        catch(NullPointerException npe){
            npe.printStackTrace();
        }
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
    public void scan(int scanMode) throws IOException {
        File[] dirElements=ROOT_DIR.listFiles();

        for(int i=0;i<dirElements.length;i++){
            this.logElement("["+i+"] "+dirElements[i].getName(),SEARCH_LOG_FILE);
            if((dirElements[i].getName().endsWith(".zip") || dirElements[i].getName().endsWith(".rar"))){
                this.scanZip(dirElements[i],scanMode);
            }
            else if(dirElements[i].isDirectory()){
                this.scan(dirElements[i].getAbsolutePath(),scanMode);
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
    public void scan(String dir,int scanMode) throws IOException {
        File actualdir =new File(dir);
        File[] actualDirFiles = actualdir.listFiles();

        for(int i=0;i<actualDirFiles.length;i++){
            if((actualDirFiles[i].getName().endsWith(".zip") || actualDirFiles[i].getName().endsWith(".rar"))){
                this.scanZip(actualDirFiles[i],scanMode);
            }
            else if(actualDirFiles[i].isDirectory()){
                this.scan(actualDirFiles[i].getAbsolutePath(),scanMode);
            }else if(actualDirFiles[i].isFile()){
                //do nothing
            }
        }
    }

    /**
     * Recursively scan a zipped file and copy out a single file that match
     * with a given criteria
     *
     * @param file The zipped file
     * @throws IOException if the file is not found
     */
    private void scanZip(File file,int scanMode) throws IOException{
        FileInputStream fin = new FileInputStream(this.getRootDir()+"\\"+file.getName());
        BufferedInputStream bin = new BufferedInputStream(fin);
        ZipInputStream zin = new ZipInputStream(bin);
        ZipEntry ze = null;

        //this.logElement("\t\tScanning "+file.getName()+"...");

        switch(scanMode){
            case 1:

                while ((ze = zin.getNextEntry()) != null) {
                    if(!ze.isDirectory()){
                        for (int i = 0; i < ID_TO_SEARCH.length; i++) {
                            //this.logElement("====================================== ["+i+"] ============================================================");
                            //this.logElement("\t\t\t\t" + ze.getName() + ".contains(_" + ID_TO_SEARCH[i] + "_) ==> " + (ze.getName().contains("_" + ID_TO_SEARCH[i] + "_")));
                            if (ze.getName().contains("_" + ID_TO_SEARCH[i] + "_")) {
                                OutputStream out = new FileOutputStream(OUTPUT_DIR + ze.getName().split("/")[1]);
                                byte[] buffer = new byte[8192];
                                int len;
                                while ((len = zin.read(buffer)) != -1) {
                                    out.write(buffer, 0, len);
                                }
                                out.close();
                                this.logElement("\t\t\t\t" + ID_TO_SEARCH[i] + " ==> FOUND",SEARCH_LOG_FILE);
                                break;
                            }
                        }
                    }
                }
                break;
            case 2:
                int jsonFound=0;
                while ((ze = zin.getNextEntry()) != null) {
                    if(!ze.isDirectory()){
                        if (ze.getName().contains(".json")) {
                            jsonFound++;
                        }
                    }
                }
                this.logElement(file.getName()+ " ==> "+jsonFound,COUNT_LOG_FILE);
                break;
            default:
                break;
        }

    }

    /**
     * Extract ID from JSON clip name
     *
     * @return an ArrayList with all the IDs extracted
     */
    public ArrayList<String> extractIDFromFileName() {
        ArrayList<String> clipIDs = new ArrayList<String>();

        File[] dirElements = ROOT_DIR.listFiles();

        for (int i = 0; i < dirElements.length; i++) {
            clipIDs.add(dirElements[i].getName().split("_")[1]);
        }
        return clipIDs;
    }

    /**
     * For each clip id in the clipIDs ArrayList check if on the ods is there some written sentence
     * and the is_processed flag value
     *
     * @param clipIDs the IDs to search
     */
    public void checkClipStatus(Connection conn,ArrayList<String> clipIDs){
        int[] tmpStatus=new int[3];

        for(int i=0;i<clipIDs.size();i++){
            tmpStatus = DbOperations.getClipStatus(conn,Integer.parseInt(clipIDs.get(i)));
            try {
                this.logElement(tmpStatus[0]+" - "+tmpStatus[1]+" - "+tmpStatus[2],EXTRACTID_LOG_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Log to file element retrieved by the scanning routines
     * @param element the element retrieved  (the one to log)
     * @throws IOException if the log fil is not found
     */
    private void logElement(String element,String logFile) throws IOException{
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
        writer.println(element);
        writer.close();
    }
}
