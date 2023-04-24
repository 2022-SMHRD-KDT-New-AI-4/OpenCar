package com.lmj.opencar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    Button btn_logout,btn_driving;
    TextView tv_user;
    ImageView img_setting,img_bell,iv_pattern,iv_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_user= findViewById(R.id.tv_user);
        btn_logout = findViewById(R.id.btn_logout);
//        btn_driving = findViewById(R.id.btn_driving);
        img_setting = findViewById(R.id.img_setting);
        img_bell = findViewById(R.id.img_bell);
        iv_pattern = findViewById(R.id.iv_pattern);
        iv_start = findViewById(R.id.iv_start);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String inputText = auto.getString("inputId", "");

        tv_user.setText(inputText + "님");


        Intent intent2 = getIntent();
        String nid = intent2.getStringExtra("id");
        if (nid != null) {
            tv_user.setText(nid + "님");
        }


        final String RID = nid;
        final String RID2 = inputText;

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it_setting = new Intent(MainActivity.this,SettingActivity.class);
                it_setting.putExtra("id",RID);
                if (RID == null) {
                    it_setting.putExtra("id", RID2);
                }
                launcher.launch(it_setting);
            }
        });

        // 벨버튼 클릭 이벤트
        img_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it_bell = new Intent(MainActivity.this,AlarmActivity.class);
                it_bell.putExtra("id",RID);
                if (RID == null) {
                    it_bell.putExtra("id", RID2);
                }
                launcher.launch(it_bell);
            }
        });



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
        iv_pattern.setOnClickListener(new View.OnClickListener() {
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
        iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK){
                                String id = result.getData().getStringExtra("id");
                                tv_user.setText(id+"님");
                            }
                        }
                    });


}