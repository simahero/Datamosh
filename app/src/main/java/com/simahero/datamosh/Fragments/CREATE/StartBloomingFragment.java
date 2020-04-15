package com.simahero.datamosh.Fragments.CREATE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.simahero.datamosh.Moshing.DATA.UIViewModel;
import com.simahero.datamosh.Moshing.DATA.URIS;
import com.simahero.datamosh.R;
import com.simahero.datamosh.UTILS.VideoUtils;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartBloomingFragment extends Fragment {

    private static final String TAG = "startBloomingFragment";

    private FloatingActionButton fob;
    private Button cut1, replace1;
    private CardView cv1;
    private static final int SELECT_VIDEO = 1;
    private static final int REPLACE_VIDEO_1 = 2;
    private NavController controller;
    private UIViewModel model;
    private ImageView tn1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_blooming, container, false);
        controller = NavHostFragment.findNavController(this);
        model = ViewModelProviders.of(getActivity()).get(UIViewModel.class);
        tn1 = view.findViewById(R.id.vthumb);
        if (model.getVideo1().getValue() != null){
            tn1.setImageBitmap(model.getVideo1().getValue());
        }
        cv1 = view.findViewById(R.id.cardView1);
        model.getWb().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                tn1.setImageBitmap(bitmap);
            }
        });
        fob = view.findViewById(R.id.floatingActionButton);
        fob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                i.setType("video/*");
                startActivityForResult(i, SELECT_VIDEO);
            }
        });
        replace1 = view.findViewById(R.id.replacev1);
        replace1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                i.setType("video/*");
                startActivityForResult(i, REPLACE_VIDEO_1);
            }
        });
        cut1 = view.findViewById(R.id.cutv1);
        cut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setCutVid(URIS.wb);
                controller.navigate(R.id.cutFragment);
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_VIDEO) {
            if (data != null) {
                if (URIS.wb == null) {
                    URIS.wb = data.getData();
                    cv1.setVisibility(View.VISIBLE);
                    try {
                        VideoUtils.getBitmapFromUri(getContext(), URIS.wb, model.getWb());
                    } catch (Throwable throwable) {
                    }
                    fob.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_fast_forward_black_24dp));
                    fob.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            controller.navigate(R.id.selectFrameFragment);
                        }
                    });

                } else if (resultCode == RESULT_OK && requestCode == REPLACE_VIDEO_1) {
                    if (data != null) {
                        URIS.wb = data.getData();
                        try {
                            VideoUtils.getBitmapFromUri(getContext(), URIS.wb, model.getVideo1());
                        } catch (Throwable throwable) {
                        }
                    }
                }
            }
        }
    }
}
