package com.simahero.datamosh.UTILS;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;

public class FilesManager {

    public static String DEST;
    public static File dest;
    public static String RESULTS;
    public static File results;

    //creating filesystem for the app
    public static void makeAppDirectory(Context context) {

        //destination folder to work with
        dest = new File(Environment.getExternalStorageDirectory() + File.separator + "MoshIt");
        boolean success = true;
        if (!dest.exists()) {
            success = dest.mkdir();
        }
        if (success) {
        } else {
            Toast.makeText(context, "Failed to create folder! :(", Toast.LENGTH_SHORT).show();
        }

        //result folder to keep result videos
        results = new File(Environment.getExternalStorageDirectory() + File.separator + "MoshIt" + File.separator + "MoshedVideos");
        boolean success2 = true;
        if (!results.exists()) {
            success2 = results.mkdir();
        }
        if (success2) {
        } else {
            Toast.makeText(context, "Failed to create folder! :(", Toast.LENGTH_SHORT).show();
        }

        //folders path to work with
        DEST = dest.getAbsolutePath();
        RESULTS = results.getAbsolutePath();
    }

    //get files path
    public static String getPathFromDest(String filename, String extension){
        return DEST + File.separator + filename + extension;
    }

    //get files path
    public static String getPathFromResult(String filename, String extension){
        return RESULTS + File.separator + filename + extension;
    }

    //remove unnesecary files
    public static void cleanUp(){
        for (File f : dest.listFiles()){
            if (f == results){
                continue;
            } else {
                f.delete();
            }
        }
    }
}
