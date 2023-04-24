package com.lmj.opencar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    private ArrayList<AlarmVO> data;
    private Context context;

    public AlarmAdapter(ArrayList<AlarmVO> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View templete = LayoutInflater.from(parent.getContext()).inflate(R.layout.templete,parent,false);
        AlarmViewHolder holder = new AlarmViewHolder(templete);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {

        holder.tv_time.setText(data.get(position).getTime());
        holder.tv_msg.setText(data.get(position).getMsg());
        holder.img.setImageResource(R.drawable.wheel);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
