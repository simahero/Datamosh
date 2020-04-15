package com.simahero.datamosh.Moshing;

import android.os.AsyncTask;
import com.arthenica.mobileffmpeg.FFmpeg;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class FFmpegExecuteAsyncTask extends AsyncTask<Void, Void, Integer> {

    private final String command;
    FFmpegExecuteResponseHandler ffmpegExecuteResponseHandler;

    public FFmpegExecuteAsyncTask(final String command, final FFmpegExecuteResponseHandler ffmpegExecuteResponseHandler) {
        this.command = command;
        this.ffmpegExecuteResponseHandler = ffmpegExecuteResponseHandler;
    }


    protected Integer doInBackground(Void... voids) {
        return FFmpeg.execute(command);
    }

    @Override
    protected void onPreExecute() {
        if (ffmpegExecuteResponseHandler != null) {
            ffmpegExecuteResponseHandler.onStart();
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (integer == RETURN_CODE_SUCCESS){
            ffmpegExecuteResponseHandler.onSuccess(null);
        } else {
            ffmpegExecuteResponseHandler.onFailure(null);
        }
    }
}
