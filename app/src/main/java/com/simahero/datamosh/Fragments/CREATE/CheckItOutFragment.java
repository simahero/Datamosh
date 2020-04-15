package com.simahero.datamosh.Fragments.CREATE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.simahero.datamosh.Moshing.DATA.UIViewModel;
import com.simahero.datamosh.Moshing.DATA.URIS;
import com.simahero.datamosh.R;

public class CheckItOutFragment extends Fragment {

    private Button exit;
    private UIViewModel model;
    private VideoView videoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_it_out, container, false);
        model = ViewModelProviders.of(getActivity()).get(UIViewModel.class);
        videoView = view.findViewById(R.id.checkoutvid);
        videoView.setVideoPath(model.getMoshedVideoPath().getValue());
        videoView.start();
        exit = view.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.cleanUp();
                URIS.cleanUp();
                NavHostFragment.findNavController(CheckItOutFragment.this).navigate(R.id.homeFragment);

            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    model.cleanUp();
                    URIS.cleanUp();
                    NavHostFragment.findNavController(CheckItOutFragment.this).navigate(R.id.moshTypeFragment);
                    return true;
                }
                return false;
            }
        } );
        return view;
    }
}
