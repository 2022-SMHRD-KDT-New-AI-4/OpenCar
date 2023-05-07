package com.lmj.opencar.Drive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmj.opencar.R;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoViewHolder>{
    private ArrayList<InfoVO> data;
    private Context context;

    public InfoAdapter(ArrayList<InfoVO> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View templete = LayoutInflater.from(parent.getContext()).inflate(R.layout.infotemplete,parent,false);
        InfoViewHolder holder = new InfoViewHolder(templete);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
        holder.tv_temticheck.setText(data.get(position).getDay());
        holder.tv_temhour.setText(data.get(position).getHour());
        holder.tv_temcheck.setText(data.get(position).getCheck());
        holder.tv_temfreq.setText(data.get(position).getFreq());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
