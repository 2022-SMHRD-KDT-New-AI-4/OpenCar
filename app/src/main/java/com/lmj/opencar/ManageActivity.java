package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ManageActivity extends AppCompatActivity {

    TextView tv_manage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        tv_manage = findViewById(R.id.tv_manage);

        tv_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageActivity.this, CapopActivity.class);
                startActivity(intent);



            }
        });
    }
}