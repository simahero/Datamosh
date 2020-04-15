package com.simahero.datamosh.Fragments.CREATE;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.simahero.datamosh.Activities.MainActivity;
import com.simahero.datamosh.Moshing.DATA.UIViewModel;
import com.simahero.datamosh.Moshing.DATA.URIS;
import com.simahero.datamosh.R;

public class MoshTypeFragment extends Fragment {

    private Button ioe, wbe;
    private VideoView iovv, wbvv;
    private NavController controller;
    private UIViewModel model;

    public MoshTypeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mosh_type, container, false);
        controller = NavHostFragment.findNavController(this);
        URIS.cleanUp();
        model = ViewModelProviders.of(getActivity()).get(UIViewModel.class);
        model.cleanUp();
        ioe = view.findViewById(R.id.ioe);
        wbe = view.findViewById(R.id.wbe);
        Uri ioeraw = Uri.parse("android.resource://" + getActivity().getPackageName() + "/"
                + R.raw.oie);
        iovv = view.findViewById(R.id.oivv);
        iovv.setVideoURI(ioeraw);
        iovv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setVolume(0,0);
            }
        });
        iovv.start();
        Uri wbraw = Uri.parse("android.resource://" + getActivity().getPackageName() + "/"
                + R.raw.jc);
        wbvv = view.findViewById(R.id.wbvv);
        wbvv.setVideoURI(wbraw);
        wbvv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        wbvv.start();
        ioe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
                controller.navigate(R.id.startMoshFragment);
            }
        });
        wbe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());
                controller.navigate(R.id.startBloomingFragment);
            }
        });
        return view;
    }
}
