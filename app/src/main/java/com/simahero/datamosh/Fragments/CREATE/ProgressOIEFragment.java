package com.simahero.datamosh.Fragments.CREATE;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.simahero.datamosh.Activities.MainActivity;
import com.simahero.datamosh.Moshing.DATA.UIViewModel;
import com.simahero.datamosh.Moshing.DATA.URIS;
import com.simahero.datamosh.Moshing.Commands;
import com.simahero.datamosh.Moshing.FFmpegExecuteAsyncTask;
import com.simahero.datamosh.Moshing.FFmpegExecuteResponseHandler;
import com.simahero.datamosh.R;
import com.simahero.datamosh.UTILS.FilesManager;
import com.simahero.datamosh.UTILS.VideoUtils;
import static com.simahero.datamosh.Moshing.DATA.ConstNames.AVI_EXTENSION;
import static com.simahero.datamosh.Moshing.DATA.ConstNames.MP4_EXTENSION;
import static com.simahero.datamosh.Moshing.DATA.ConstNames.V1_CONV;
import static com.simahero.datamosh.Moshing.DATA.ConstNames.V1_CUT;
import static com.simahero.datamosh.Moshing.DATA.ConstNames.V2_CONV;
import static com.simahero.datamosh.Moshing.DATA.ConstNames.V2_CUT;

public class ProgressOIEFragment extends Fragment {

    public ProgressOIEFragment(){

    }

    private TextView progressMessage, progressCount;
    private Button back, check;
    private ProgressBar progressBar;
    private UIViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progressoie, container, false);
        MainActivity.mInterstitialAd.show();
        model = ViewModelProviders.of(getActivity()).get(UIViewModel.class);
        progressMessage = view.findViewById(R.id.progressmessage);
        progressCount = view.findViewById(R.id.progresscount);
        progressBar = view.findViewById(R.id.progressBar);
        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.cleanUp();
                URIS.cleanUp();
                NavHostFragment.findNavController(ProgressOIEFragment.this).navigate(R.id.homeFragment);
            }
        });
        check = view.findViewById(R.id.chackbutton);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ProgressOIEFragment.this).navigate(R.id.checkItOutFragment);
            }
        });
        Config.enableStatisticsCallback(new StatisticsCallback() {
            @Override
            public void apply(Statistics statistics) {
                String s = "Frame number: " + statistics.getVideoFrameNumber() + "\n"
                        + "FPS: " + statistics.getVideoFps() + "\n"
                        + "Quality: " + statistics.getVideoQuality() + "\n"
                        + "Size: " + statistics.getSize() + "\n"
                        + "Time: " + statistics.getTime() + "\n"
                        + "Bitrate: " + statistics.getBitrate() + "\n"
                        + "Speed: " + statistics.getSpeed();
                progressMessage.setText(s);
            }
        });
        startConvert(VideoUtils.getRealPathFromURI(getContext(), URIS.v1), V1_CUT);
        return view;
    }

    private void startConvert(final String uri, final String name){
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.convert(model.getPreset().getValue(), uri , name, AVI_EXTENSION), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                convertSecond(FilesManager.getPathFromDest(V2_CUT, VideoUtils.getRealPathFromURI(getContext(), URIS.v2).substring(VideoUtils.getRealPathFromURI(getContext(), URIS.v2).lastIndexOf("."))), V2_CONV);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "FAILED! :(" + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStart() {
                progressCount.setText("Converting: 1/2");
            }
        });
        task.execute();
    }

    private void convertSecond(String uri, final String name){
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.convert(model.getPreset().getValue(), uri, name, AVI_EXTENSION), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                createTs1();
            }
            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "FAILED! :(" + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStart() {
                progressCount.setText("Converting: 2/2");
            }
        });
        task.execute();
    }

    private void createTs1(){
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.createTs(FilesManager.getPathFromDest(V1_CONV, AVI_EXTENSION), "vid1"), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                createTs2();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "FAILED! :(", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStart() {
                progressCount.setText("Creating TS 1/2");
            }
        });
        task.execute();
    }

    public void createTs2(){
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.createTsNoIFrame(FilesManager.getPathFromDest(V2_CONV, AVI_EXTENSION), "vid2"), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                concat();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "FAILED! :(", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStart() {
                progressCount.setText("Creating TS 2/2");
            }
        });
        task.execute();
    }

    private void concat(){
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.concatTs(Commands.getMultiplePFrames(FilesManager.getPathFromDest("vid1", ".ts"), FilesManager.getPathFromDest("vid2", ".ts"), 1, null), "moshed", MP4_EXTENSION), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                convertToMp4();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "FAILED! :(", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStart() {
                progressCount.setText("Concentrating videos");
            }

        });
        task.execute();
    }

    private void convertToMp4(){
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask("-i " + FilesManager.getPathFromDest("moshed", MP4_EXTENSION) +" -c copy " + FilesManager.getPathFromResult(model.getMoshedVideoName().getValue(), MP4_EXTENSION), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                progressBar.setVisibility(View.INVISIBLE);
                back.setVisibility(View.VISIBLE);
                check.setVisibility(View.VISIBLE);
                String resultVideo = FilesManager.getPathFromResult(model.getMoshedVideoName().getValue(), MP4_EXTENSION);
                Toast.makeText(getActivity(), "Finished sucelsfully!", Toast.LENGTH_SHORT).show();
                VideoUtils.insertToMediaStore(getActivity(), resultVideo);
                model.setMoshedVideoPath(resultVideo);
                FilesManager.cleanUp();
                progressMessage.setText("Finished!");
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "FAILED! :(" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStart() {
                progressCount.setText("Finishing up");
            }
        });
        task.execute();
    }
}
