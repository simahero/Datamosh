package com.simahero.datamosh.Fragments.CREATE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.simahero.datamosh.Dialogs.OIESettings;
import com.simahero.datamosh.Moshing.DATA.UIViewModel;
import com.simahero.datamosh.Moshing.DATA.URIS;
import com.simahero.datamosh.R;
import com.simahero.datamosh.UTILS.VideoUtils;

import static android.app.Activity.RESULT_OK;

public class StartMoshFragment extends Fragment {

    private FloatingActionButton fob;
    private Button cut1, cut2, replace1, replace2;
    private CardView cv1, cv2;
    private static final int SELECT_VIDEO = 1;
    private static final int REPLACE_VIDEO_1 = 2;
    private static final int REPLACE_VIDEO_2 = 3;
    private NavController controller;
    private UIViewModel model;
    ImageView tn1, tn2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_mosh, container, false);
        controller = NavHostFragment.findNavController(this);
        model = ViewModelProviders.of(getActivity()).get(UIViewModel.class);
        tn1 = view.findViewById(R.id.vthumb1);
        tn2 = view.findViewById(R.id.vthumb2);
        if (model.getVideo1().getValue() != null){
            tn1.setImageBitmap(model.getVideo1().getValue());
        }
        if (model.getVideo2().getValue() != null){
            tn2.setImageBitmap(model.getVideo2().getValue());
        }
        cv1 = view.findViewById(R.id.cardView1);
        cv2 = view.findViewById(R.id.cardView2);
        model.getVideo1().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                tn1.setImageBitmap(bitmap);
            }
        });
        model.getVideo2().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                tn2.setImageBitmap(bitmap);
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
        cut1 = view.findViewById(R.id.cutv1);
        cut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setCutVid(URIS.v1);
                controller.navigate(R.id.cutFragment);
            }
        });
        cut2 = view.findViewById(R.id.cutv2);
        cut2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setCutVid(URIS.v2);
                controller.navigate(R.id.cutFragment);
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
        replace2 = view.findViewById(R.id.replacev2);
        replace2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                i.setType("video/*");
                startActivityForResult(i, REPLACE_VIDEO_2);
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
                if (URIS.v1 == null) {
                    URIS.v1 = data.getData();
                    cv1.setVisibility(View.VISIBLE);
                    try {
                        VideoUtils.getBitmapFromUri(getContext(), URIS.v1, model.getVideo1());
                    } catch (Throwable throwable) {
                    }

                } else if (URIS.v2 == null) {
                    URIS.v2 = data.getData();
                    cv2.setVisibility(View.VISIBLE);
                    try {
                        VideoUtils.getBitmapFromUri(getContext(), URIS.v2, model.getVideo2());
                    } catch (Throwable throwable) {
                    }
                    fob.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_fast_forward_black_24dp));
                    fob.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OIESettings settings = new OIESettings();
                            settings.show(getParentFragmentManager(), "settings");
                        }
                    });
                }
            } else if (resultCode == RESULT_OK && requestCode == REPLACE_VIDEO_1) {
                if (data != null) {
                    URIS.v1 = data.getData();
                    try {
                        VideoUtils.getBitmapFromUri(getContext(), URIS.v1, model.getVideo1());
                    } catch (Throwable throwable) {
                    }
                }
            } else if (resultCode == RESULT_OK && requestCode == REPLACE_VIDEO_2) {
                if (data != null) {
                    URIS.v2 = data.getData();
                    try {
                        VideoUtils.getBitmapFromUri(getContext(), URIS.v2, model.getVideo2());
                    } catch (Throwable throwable) {
                    }
                }
            }
        }
    }
}

