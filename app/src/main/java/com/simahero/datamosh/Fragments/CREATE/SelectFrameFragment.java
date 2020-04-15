package com.simahero.datamosh.Fragments.CREATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.simahero.datamosh.Adapters.PFrameAdapter;
import com.simahero.datamosh.Dialogs.CountDialog;
import com.simahero.datamosh.Dialogs.WBESettings;
import com.simahero.datamosh.Moshing.DATA.UIViewModel;
import com.simahero.datamosh.Moshing.DATA.URIS;
import com.simahero.datamosh.Moshing.Commands;
import com.simahero.datamosh.Moshing.FFmpegExecuteAsyncTask;
import com.simahero.datamosh.Moshing.FFmpegExecuteResponseHandler;
import com.simahero.datamosh.R;
import com.simahero.datamosh.UTILS.FilesManager;
import com.simahero.datamosh.UTILS.VideoUtils;

import java.util.ArrayList;

public class SelectFrameFragment extends Fragment implements PFrameAdapter.OnGridListener {

    //VIEWS
    private RangeSeekBar rangeSeekBar;
    private ProgressBar progressBar;
    private Button selectButton;
    private VideoView videoView;
    private ConstraintLayout holderLayout;
    private UIViewModel model;
    private TextView currpos, helptext, stat;
    private RecyclerView recyclerView;
    private PFrameAdapter adapter;
    private FloatingActionButton floatingActionButton;

    //DATA
    private int selectedFrameTimeStamp;
    private boolean selectionmade = false;
    private int fps = 60;
    public static int duration;
    private int progressTime;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_select_frame, container, false);
        initView(view);
        initListeners(view);
        setFfmpegConfig();

        FFmpegExecuteAsyncTask task = new FFmpegExecuteAsyncTask(Commands.prepareVideo(VideoUtils.getRealPathFromURI(getContext(), URIS.wb), FilesManager.getPathFromDest("prepared", ".mp4")), new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
                stat.setVisibility(View.INVISIBLE);
                selectButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                helptext.setVisibility(View.INVISIBLE);
                holderLayout.setVisibility(View.VISIBLE);
                currpos.setText("Select a frame!");
                videoView.setVideoPath(FilesManager.getPathFromDest("prepared", ".mp4"));
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        duration = mp.getDuration();
                        rangeSeekBar.setSteps(duration / fps);
                        rangeSeekBar.setRange(0, (float) (duration));
                    }
                });
                videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (videoView.isPlaying()) {
                            videoView.pause();
                        } else {
                            videoView.start();
                        }
                    }
                });
                model.getFramelist().observe(getViewLifecycleOwner(), new Observer<ArrayList<PFrame>>() {
                    @Override
                    public void onChanged(ArrayList<PFrame> pFrames) {
                        adapter.setPframes(pFrames);
                        adapter.notifyDataSetChanged();
                    }
                });
                floatingActionButton = view.findViewById(R.id.addP);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CountDialog dialog = new CountDialog(selectedFrameTimeStamp, progressTime, adapter);
                        dialog.show(getFragmentManager(), "count");
                    }
                });
                videoView.start();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "Failed to process the video... :(", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(SelectFrameFragment.this).navigate(R.id.homeFragment);
            }

            @Override
            public void onStart() {
            }

        });
        task.execute();
        return view;
    }

    private void initView(View view){

        //FOR ONBACKPRESSED
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        //INIT VIEWMODEL
        model = ViewModelProviders.of(getActivity()).get(UIViewModel.class);
        model.setFramelist(new ArrayList<PFrame>());

        //INIT RECYCLEVIEW
        recyclerView = view.findViewById(R.id.recycle);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayout);
        adapter = new PFrameAdapter(getContext(), model.getFramelist().getValue(), this);
        recyclerView.setAdapter(adapter);

        //INIT TEXTVIEWS, BUTTONS, ETC...
        stat = view.findViewById(R.id.statistics);
        currpos = view.findViewById(R.id.currpos);
        helptext = view.findViewById(R.id.helptext);
        holderLayout = view.findViewById(R.id.vidconst);
        videoView = view.findViewById(R.id.videoView);
        progressBar = view.findViewById(R.id.preparevidprogress);
        selectButton = view.findViewById(R.id.cutbutton);
        rangeSeekBar = view.findViewById(R.id.sb);

    }

    private void initListeners(View view){
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    NavHostFragment.findNavController(SelectFrameFragment.this).navigate(R.id.moshTypeFragment);
                    return true;
                }
                return false;
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getFramelist().getValue().size() == 0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                    builder.setTitle("Please select a frame");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else {
                    model.setSelectedTime(String.valueOf(progressTime / 1000));
                    model.setSelectedEnd(getTime((int)progressTime + 17));
                    WBESettings settings = new WBESettings();
                    settings.show(getParentFragmentManager(), "wbesettings");
                }
            }
        });

        rangeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                videoView.seekTo((int) leftValue);
                progressTime = (int) leftValue;
                if (progressTime % 33 != 0) {
                    progressTime = progressTime - (progressTime % 33);
                }
                rangeSeekBar.setIndicatorText(getTime(videoView.getCurrentPosition()));
                selectedFrameTimeStamp = progressTime;
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }
        });
    }

    private void setFfmpegConfig(){
        /*
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
                stat.setText(s);
            }
        });

         */
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

    @Override
    public void onItemClick(View view, int pos) {

    }

    @Override
    public void onItemLongClick(View view, int pos) {

    }
}