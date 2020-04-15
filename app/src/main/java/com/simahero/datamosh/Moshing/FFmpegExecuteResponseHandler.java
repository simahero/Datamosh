package com.simahero.datamosh.Moshing;

import com.arthenica.mobileffmpeg.Statistics;

public interface FFmpegExecuteResponseHandler {

    void onSuccess(String message);

    void onFailure(String message);

    void onStart();

}
