package com.simahero.datamosh.Fragments.CREATE;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import static com.simahero.datamosh.Moshing.DATA.ConstNames.TS_EXTENSION;
import static com.simahero.datamosh.Moshing.DATA.ConstNames.V1_BEG;
import static com.simahero.datamosh.Moshing.DATA.ConstNames.V1_CONV;
import static com.simahero.datamosh.Moshing.DATA.ConstNames.V1_END;
import static com.simahero.datamosh.Moshing.DATA.ConstNames.V1_PFRAME;

public class ProgressWBEFragment extends Fragment {

    private TextView progressMessage, progressCount;
    private Button back, check;
    private ProgressBar progressBar;
    private UIViewModel model;

    public ProgressWBEFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progressoie, container, false);
        initViews(view);
        convert(URIS.wb, V1_CONV);
        model.setpFrameTask(0);
        model.setSectionEnd(0);
        model.setSection(0);
        return view;
    }

    private void convert(Uri uri, String name) {
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.convert(model.getPreset().getValue(), VideoUtils.getRealPathFromURI(getContext(), uri), name, AVI_EXTENSION), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                cutBegining(V1_CONV, V1_BEG);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), "Failed :(", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(ProgressWBEFragment.this).navigate(R.id.homeFragment);
            }

            @Override
            public void onStart() {
                progressCount.setText("Converting video...");
            }
        });
        task.execute();
    }

    private void cutBegining(String uri, String name) {
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.cutFromTo(FilesManager.getPathFromDest(uri, AVI_EXTENSION), "00:00", getTime(model.getFramelist().getValue().get(0).timestamp), name, TS_EXTENSION), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                getPFrames(V1_CONV, "V" + 0 + "_PFRAME", model.getFramelist().getValue().get(0).getTimestamp(), 0);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), "Failed :(", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(ProgressWBEFragment.this).navigate(R.id.homeFragment);
            }

            @Override
            public void onStart() {
                progressCount.setText("Slicing up... 1");
            }
        });
        task.execute();
    }

    private void getPFrames(String uri, String name, int timestamp, final int i) {
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.create1PFrameVideo(FilesManager.getPathFromDest(uri, AVI_EXTENSION), getTime(timestamp), name, TS_EXTENSION), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                int j = model.getpFrameTask().getValue();
                j++;
                model.setpFrameTask(i);
                if (j < model.getFramelist().getValue().size()) {
                    getPFrames(V1_CONV, "V" + j + "_PFRAME", model.getFramelist().getValue().get(j).getTimestamp(), j);
                } else {
                    createSectionEnd(V1_CONV, "V" + 0 + "_SECTION", 0);
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), "Failed :(", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(ProgressWBEFragment.this).navigate(R.id.homeFragment);
            }

            @Override
            public void onStart() {
                progressCount.setText("Creating P Frames... " + i + "/" + model.getFramelist().getValue().size());
            }
        });
        task.execute();
    }

    private void createSectionEnd(String uri, String name, final int i) {
        String time;
        if (i < model.getFramelist().getValue().size()-1){
            time = getTime(model.getFramelist().getValue().get(i + 1).getTimestamp());
        } else {
            time = getTime(SelectFrameFragment.duration);
        }
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.cutFromTo(FilesManager.getPathFromDest(uri, AVI_EXTENSION), getTime(model.getFramelist().getValue().get(i).timestamp + 33), time, "V" + i + "_SCE", TS_EXTENSION), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                int j = model.getSectionEnd().getValue();
                j++;
                model.setSectionEnd(j);
                if (j < model.getFramelist().getValue().size()) {
                    createSectionEnd(V1_CONV, "V" + j + "_SECTION", j);
                } else {
                    createSections(Commands.getSectionConcat("V" + 0 + "_PFRAME", model.getFramelist().getValue().get(0).getCount(), FilesManager.getPathFromDest("V" + 0 + "_SCE", TS_EXTENSION)), "V" + 0 + "_CS", 0);
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), "Failed :(", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(ProgressWBEFragment.this).navigate(R.id.homeFragment);
            }

            @Override
            public void onStart() {
                progressCount.setText("Creating intersections... " + i + "/" + model.getFramelist().getValue().size());
            }
        });
        task.execute();
    }

    private void createSections(String concat, String name, final int i) {
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.createSection(concat, name, TS_EXTENSION), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                int j = model.getSection().getValue();
                j++;
                model.setSection(j);
                if (j < model.getFramelist().getValue().size()){
                    createSections(Commands.getSectionConcat("V" + j + "_PFRAME", model.getFramelist().getValue().get(j).getCount(), FilesManager.getPathFromDest("V" + j + "_SCE", TS_EXTENSION)), "V" + j + "_CS", j);
                } else {
                    concat(Commands.getFinalC(FilesManager.getPathFromDest(V1_BEG, TS_EXTENSION), model.getFramelist().getValue().size()), "moshed");
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), "Failed :(", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(ProgressWBEFragment.this).navigate(R.id.homeFragment);
            }

            @Override
            public void onStart() {
                progressCount.setText("Creating sections... " + i + "/" + model.getFramelist().getValue().size());
            }
        });
        task.execute();
    }

    private void concat(String concat, String name) {
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.concatTs(concat, name, AVI_EXTENSION), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                convertback();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), "Failed :(", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(ProgressWBEFragment.this).navigate(R.id.homeFragment);
            }

            @Override
            public void onStart() {
                progressCount.setText("Concentrating parts...");
            }
        });
        task.execute();
    }

    public void convertback() {
        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.finalconvert(model.getPreset().getValue(), FilesManager.getPathFromDest("moshed", AVI_EXTENSION), model.getMoshedVideoName().getValue(), MP4_EXTENSION), new FFmpegExecuteResponseHandler() {
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
                Toast.makeText(getActivity(), "Failed :(", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(ProgressWBEFragment.this).navigate(R.id.homeFragment);
            }

            @Override
            public void onStart() {
                progressCount.setText("Finishing up...");
            }
        });
        task.execute();
    }

    private void initViews(View view) {
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
                NavHostFragment.findNavController(ProgressWBEFragment.this).navigate(R.id.homeFragment);
            }
        });
        check = view.findViewById(R.id.chackbutton);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ProgressWBEFragment.this).navigate(R.id.checkItOutFragment);
            }
        });

    }

    //MILIS TO FFMPEG FORMAT 00:00:00.000
    private String getTime(int i) {
        int hr = i / 3600000;
        int rem = i % 3600000;
        int mn = rem / 60000;
        int rem2 = rem % 60000;
        int sec = rem2 / 1000;
        int ms = i % 1000;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec) + "." + ms;
    }
}
