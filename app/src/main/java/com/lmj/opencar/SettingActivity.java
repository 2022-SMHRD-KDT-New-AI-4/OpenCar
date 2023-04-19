package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    Button btn_ok;
    SeekBar bellbar,speedbar;
    TextView tv_val1,tv_val2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btn_ok = findViewById(R.id.btn_ok);
        bellbar = findViewById(R.id.bellbar);
        speedbar = findViewById(R.id.speedbar);
        tv_val1 = findViewById(R.id.tv_val1);
        tv_val2 = findViewById(R.id.tv_val2);

        bellbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // 시크바를 조작하고 있는 중
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 시크바를 처음 터치했을 때
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 시크바 터치가 끝났을 때
                tv_val1.setText(String.format("%d",bellbar.getProgress()));
            }
        });



        speedbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_val2.setText(String.format("%d",speedbar.getProgress()));
            }
        });



        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it_set = new Intent(SettingActivity.this,MainActivity.class);
                // it_set.putExtra() 세팅한 값 가지고 올때 사용하기
                startActivity(it_set);
            }
        });





    }
}