package com.simahero.datamosh.Moshing;

import android.content.Context;
import android.os.AsyncTask;
import android.os.FileUtils;
import com.simahero.datamosh.Moshing.Exceptions.FFmpegCommandAlreadyRunningException;

import java.util.Map;

public class FFMPEG implements FFmpegInterface {

    private final Context context;
    private FFmpegExecuteAsyncTask ffmpegExecuteAsyncTask;

    private static final long MINIMUM_TIMEOUT = 10 * 1000;
    private long timeout = Long.MAX_VALUE;

    private static FFMPEG instance = null;

    private FFMPEG(Context context) {
        this.context = context.getApplicationContext();
    }

    public static FFMPEG getInstance(Context context) {
        if (instance == null) {
            instance = new FFMPEG(context);
        }
        return instance;
    }

    @Override
    public void execute(String cmd, FFmpegExecuteResponseHandler ffmpegExecuteResponseHandler) throws FFmpegCommandAlreadyRunningException {
        if (cmd.length() != 0) {
            ffmpegExecuteAsyncTask = new FFmpegExecuteAsyncTask(cmd , ffmpegExecuteResponseHandler);
            ffmpegExecuteAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            throw new IllegalArgumentException("shell command cannot be empty");
        }
    }
}
