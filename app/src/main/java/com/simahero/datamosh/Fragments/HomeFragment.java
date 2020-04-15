package com.simahero.datamosh.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.simahero.datamosh.Activities.MainActivity;
import com.simahero.datamosh.R;

public class HomeFragment extends Fragment {

    private Button moshNow;
    private Button tips;
    private Button inspiration;
    private Button gallery;
    private NavController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        controller = NavHostFragment.findNavController(this);
        moshNow = view.findViewById(R.id.mosh_now);
        moshNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.action_homeFragment_to_moshTypeFragment);
            }
        });
        tips = view.findViewById(R.id.tips_n_tricks);
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.tipsFragment);
            }
        });
        inspiration = view.findViewById(R.id.inspiration);
        inspiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.inspirationFragment);
            }
        });
        gallery = view.findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.galleryFragment);
            }
        });
        return view;
    }
}
