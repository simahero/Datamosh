package com.simahero.datamosh.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.simahero.datamosh.Adapters.PFrameAdapter;
import com.simahero.datamosh.Fragments.CREATE.PFrame;
import com.simahero.datamosh.Moshing.DATA.UIViewModel;
import com.simahero.datamosh.Moshing.DATA.URIS;
import com.simahero.datamosh.R;

import static com.simahero.datamosh.UTILS.VideoUtils.retriveVideoFrameFromVideo;

public class CountDialog extends AppCompatDialogFragment {

    private UIViewModel model;
    private int selectedFrame;
    private int progressTime;
    private PFrameAdapter adapter;

    public CountDialog(int selectedFrame, int progressTime, PFrameAdapter adapter) {
        this.selectedFrame = selectedFrame;
        this.progressTime = progressTime;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        model = ViewModelProviders.of(getActivity()).get(UIViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_count, null);
        final EditText editText = view.findViewById(R.id.framec);
        builder.setTitle("Mosh settings")
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bitmap bitmap = null;
                        try {
                            bitmap = retriveVideoFrameFromVideo(getContext(), URIS.wb, progressTime * 1000);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                        if (bitmap != null) {
                            bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);
                        }
                        PFrame temp = new PFrame(Integer.parseInt(editText.getText().toString()), selectedFrame, bitmap);
                        model.getFramelist().getValue().add(temp);
                        adapter.notifyDataSetChanged();
                    }
                });

        return builder.create();
    }


}
