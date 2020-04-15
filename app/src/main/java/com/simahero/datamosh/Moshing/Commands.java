package com.simahero.datamosh.Moshing;

import com.simahero.datamosh.UTILS.FilesManager;

import java.io.File;
import static com.simahero.datamosh.UTILS.FilesManager.DEST;
import static com.simahero.datamosh.UTILS.FilesManager.RESULTS;

public class Commands {

    public static String convert(String preset, String uri, String filename, String extension){
        return "-i " + uri + " -vcodec libx264 -preset " + preset +" -x264opts keyint=99999999:min-keyint=99999999:no-scenecut:bframes=0 -filter:v fps=fps=60 -an -y " +  DEST + File.separator + filename + extension; //"veryfast", "-x264opts", "keyint=2","-filter:v", "fps=fps=30", "-pix_fmt", "yuv420p", "-acodec", "copy", "-y",  DEST + File.separator + filename + extension
    }

    public static String prepareVideo(String uri, String dest){
        return "-i " + uri + " -c:v libx264 -preset ultrafast -x264opts keyint=2 -filter:v fps=fps=60 -pix_fmt yuv420p -acodec copy -y " + dest;
    }

    public static String finalconvert(String preset, String uri, String filename, String extension){
        return "-i " + uri + " -c:v libx264 -preset " + preset + " -x264opts keyint=2 -filter:v fps=fps=60 -pix_fmt yuv420p -acodec copy -y " + RESULTS + File.separator + filename + extension;
    }

    public static String createTs(String uri, String name) {
        return "-i " + uri + " -c copy -bsf:v h264_mp4toannexb -f mpegts -fflags +genpts -y " + DEST + File.separator + name + ".ts";
    }

    public static String createTsNoIFrame(String uri, String name) {
        return "-i " + uri + " -ss 00:00:00.020 -c copy -bsf:v h264_mp4toannexb -f mpegts -copyinkf -fflags +genpts -y " + DEST + File.separator + name + ".ts"; //"-vsync", "drop",
    }

    public static String concatTs(String concat, String name, String extension){
        return "-i " + concat + " -bsf:a aac_adtstoasc -c copy -y " + DEST + File.separator + name + extension; //"-bsf:a", "aac_adtstoasc",
    }

    public static String cutTo(String uri, String to, String name, String extension){
        return "-i " + uri + " -c copy -ss 00:00 -t " + to + " -y " + DEST + File.separator + name + extension; //"-bsf:a", "aac_adtstoasc",
    }

    public static String cutFrom(String uri, String from, String name, String extension){
        return "-i " + uri + " -c copy -ss " + from + " -copyinkf -y " + DEST + File.separator + name + extension; //"-bsf:a", "aac_adtstoasc",
    }

    public static String cutFromTo(String uri, String from, String to, String name, String extension){
        return "-i " + uri + " -c copy -ss " + from + " -t " + to + " -copyinkf -y " + DEST + File.separator + name + extension;
    }

    public static String createSection (String concat, String name, String extension){
        return "-i " + concat + " -bsf:a aac_adtstoasc -c copy -copyinkf -y " + DEST + File.separator + name + extension;
    }

    public static String create1PFrameVideo(String uri, String from, String filename, String extension){
        return "-i " + uri + " -ss " + from + " -c copy -frames:v 2 -copyinkf -y " + DEST + File.separator + filename + extension;
    }

    public static String getMultiplePFrames(String start, String prframe, int count, String end){
        StringBuilder builder = new StringBuilder();
        builder.append("concat:");
        builder.append(start);
        for (int i = 0; i < count; i++){
            builder.append("|");
            builder.append(prframe);
        }
        if (end != null) {
            builder.append("|");
            builder.append(end);
        }
        return builder.toString();
    }

    public static String getSectionConcat(String prframe, int count, String end){
        StringBuilder builder = new StringBuilder();
        builder.append("concat:");
        for (int i = 0; i < count; i++){
            if (i > 0) {
                builder.append("|");
                builder.append(FilesManager.DEST + File.separator + prframe + ".ts");
            } else {
                builder.append(FilesManager.DEST + File.separator + prframe + ".ts");
            }
        }
        if (end != null) {
            builder.append("|");
            builder.append(end);
        }
        return builder.toString();
    }

    public static String getFinalC(String start, int i){
        StringBuilder builder = new StringBuilder();
        builder.append("concat:");
        builder.append(start);
        for (int j = 0; j < i; j++){
            builder.append("|");
            builder.append(DEST + File.separator + "V" + j + "_CS.ts");
        }
        return builder.toString();
    }
}
