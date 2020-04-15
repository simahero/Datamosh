package com.simahero.datamosh.Moshing;

import com.simahero.datamosh.Moshing.Exceptions.FFmpegCommandAlreadyRunningException;

import java.util.Map;

public interface FFmpegInterface {

    void execute(String cmd, FFmpegExecuteResponseHandler ffmpegExecuteResponseHandler) throws FFmpegCommandAlreadyRunningException;

}