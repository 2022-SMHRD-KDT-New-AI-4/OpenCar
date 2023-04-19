package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn_logout,btn_patten,btn_driving;
    TextView tv_user;
    ImageView img_setting,img_bell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_user= findViewById(R.id.tv_user);
        btn_logout = findViewById(R.id.btn_logout);
        btn_patten = findViewById(R.id.btn_patten);
        btn_driving = findViewById(R.id.btn_driving);
        img_setting = findViewById(R.id.img_setting);
        img_bell = findViewById(R.id.img_bell);

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it_setting = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(it_setting);
            }
        });

        img_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it_bell = new Intent(MainActivity.this,BellActivity.class);
                startActivity(it_bell);
            }
        });


        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String inputText = auto.getString("inputId", "");

        tv_user.setText(inputText + "님");


        Intent intent2 = getIntent();
        String nid = intent2.getStringExtra("id");
        if (nid != null) {
            tv_user.setText(nid + "님");
        }

        // 로그 아웃 버튼 이벤트
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(MainActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // 패턴 분석 버튼 이벤트
        btn_patten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it_next2 = new Intent(MainActivity.this,PattenActivity.class);
                it_next2.putExtra("id",inputText);
                if (nid != null) {
                    it_next2.putExtra("id", nid);
                }
                startActivity(it_next2);
            }
        });

        // 주행 시작 버튼 이벤트
        btn_driving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}