package com.simahero.datamosh.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.simahero.datamosh.Moshing.DATA.UIViewModel;
import com.simahero.datamosh.Moshing.DATA.URIS;
import com.simahero.datamosh.R;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WBESettings extends AppCompatDialogFragment {

    private UIViewModel model;
    private NiceSpinner niceSpinner;
    private String item;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        model = ViewModelProviders.of(getActivity()).get(UIViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_wbe_settings, null);
        final EditText input = view.findViewById(R.id.input);
        niceSpinner = view.findViewById(R.id.spinner);
        final List<String> dataset = new LinkedList<>(Arrays.asList("ultrafast", "veryfast", "fast", "medium – default preset", "slow", "veryslow"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setSelectedIndex(0);
        niceSpinner.setArrowTintColor(R.color.colorAccent);
        niceSpinner.setTintColor(R.color.colorAccent);
        niceSpinner.setTextColor(getResources().getColor(R.color.colorAccent));
        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
            }
        });
        builder.setTitle("Mosh settings")
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        model.cleanUp();
                        URIS.v1 = null;
                        URIS.v2 = null;
                        dialog.cancel();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        model.setMoshedvidname(input.getText().toString());
                        model.setPreset(dataset.get(niceSpinner.getSelectedIndex()));
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.progressWBEFragment);
                    }
                });

        return builder.create();
    }


}