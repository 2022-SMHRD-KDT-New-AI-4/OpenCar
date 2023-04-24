package com.lmj.opencar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    TextView tv_msg,tv_time;
    ImageView img;

    public AlarmViewHolder(@NonNull View itemView) {
        super(itemView);
        img = itemView.findViewById(R.id.img_handle);
        tv_msg = itemView.findViewById(R.id.tv_msg);
        tv_time = itemView.findViewById(R.id.tv_time);
    }


}
