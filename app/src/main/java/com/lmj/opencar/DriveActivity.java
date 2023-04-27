package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DriveActivity extends AppCompatActivity {
    Button btn_fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        btn_fin = findViewById(R.id.btn_fin);

        btn_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriveActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}