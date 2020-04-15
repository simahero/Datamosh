package com.simahero.datamosh.Fragments.CREATE;

import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.simahero.datamosh.Moshing.DATA.UIViewModel;
import com.simahero.datamosh.Moshing.DATA.URIS;
import com.simahero.datamosh.R;
import com.simahero.datamosh.UTILS.VideoUtils;
import java.io.File;
import idv.luchafang.videotrimmer.VideoTrimmerView;

public class CutFragment extends Fragment {

    private RangeSeekBar rangeSeekBar;
    private VideoView videoView;
    private UIViewModel model;
    private Button done;
    private int fps = 30;
    private int duration;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cut, container, false);
        model = ViewModelProviders.of(getActivity()).get(UIViewModel.class);
        rangeSeekBar = view.findViewById(R.id.sb);
        videoView = view.findViewById(R.id.videoView);
        done = view.findViewById(R.id.cutbutton);
        videoView.setVideoURI(model.getCutVid().getValue());
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {

                    }
                });
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int currtime = videoView.getCurrentPosition();
                        if (currtime >= (int)rangeSeekBar.getRightSeekBar().getProgress()){
                            videoView.seekTo((int)rangeSeekBar.getLeftSeekBar().getProgress());
                            videoView.start();
                        }
                    }
                }, 1 );
                videoView.start();
                duration = mp.getDuration();
                rangeSeekBar.setSteps(duration);
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
        videoView.start();
        rangeSeekBar.setProgressColor(getResources().getColor(R.color.colorAccent));
        rangeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                videoView.seekTo((int) leftValue);
                videoView.start();

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }
        });
        return view;
    }

}