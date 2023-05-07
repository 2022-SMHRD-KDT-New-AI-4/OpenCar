package com.lmj.opencar.Drive;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmj.myopen0419.R;

public class InfoViewHolder extends RecyclerView.ViewHolder {
    TextView tv_temhour,tv_temfreq,tv_temcheck,tv_temticheck;

    public InfoViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_temhour = itemView.findViewById(R.id.tv_temhour);
        tv_temfreq = itemView.findViewById(R.id.tv_temfreq);
        tv_temcheck = itemView.findViewById(R.id.tv_temcheck);
        tv_temticheck = itemView.findViewById(R.id.tv_temticheck);
    }
}
