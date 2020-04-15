package com.simahero.datamosh.Adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simahero.datamosh.Fragments.CREATE.PFrame;
import com.simahero.datamosh.R;

import java.util.ArrayList;

public class PFrameAdapter extends RecyclerView.Adapter<PFrameAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PFrame> pframes;
    private OnGridListener mOnGridListener;

    public void setPframes(ArrayList<PFrame> list){
        pframes = list;
        notifyDataSetChanged();
    }

    public PFrameAdapter(Context context, ArrayList<PFrame> pframes, OnGridListener mOnGridListener){
        this.context = context;
        this.pframes = pframes;
        this.mOnGridListener = mOnGridListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wbe_element, parent, false);
        return new ViewHolder(itemView, mOnGridListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PFrame p = pframes.get(position);
        holder.thumb.setImageBitmap(p.getThumbnail());
        holder.count.setText(String.valueOf(p.getCount()));
    }

    @Override
    public int getItemCount() {
        return pframes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView count;
        ImageView thumb;
        View holderview;
        OnGridListener listener;

        public ViewHolder(@NonNull View itemView, OnGridListener listener) {
            super(itemView);
            count = itemView.findViewById(R.id.counter);
            thumb = itemView.findViewById(R.id.thumb);
            holderview = itemView.findViewById(R.id.holder);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            mOnGridListener.onItemClick(itemView, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            mOnGridListener.onItemLongClick(itemView, getAdapterPosition());
            return true;
        }

    }

    public interface OnGridListener {
        void onItemClick(View view, int pos);

        void onItemLongClick(View view, int pos);
    }

}