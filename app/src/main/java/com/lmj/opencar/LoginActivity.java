package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText edt_id,edt_pw;
    Button btn_login;
    CheckBox ch_auto;
    String loginId,loginPw;
    TextView tv_join;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_id =  findViewById(R.id.edt_id);
        edt_pw =  findViewById(R.id.edt_pw);
        btn_login = findViewById(R.id.btn_join);
        ch_auto =  findViewById(R.id.ch_auto);
        tv_join = findViewById(R.id.tv_join);


        tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it_join = new Intent(LoginActivity.this,JoinActivity.class);
                startActivity(it_join);
            }
        });


        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        loginId = auto.getString("inputId",null);
        loginPw = auto.getString("inputPw", null);

        if (loginId != null && loginPw != null){
            if (loginId.equals("hi") && loginPw.equals("123") ){
                Toast.makeText(LoginActivity.this, loginId+"님 자동 로그인", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        else if (loginId == null && loginPw == null) {
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edt_id.getText().toString().equals("hi") && edt_pw.getText().toString().equals("123") && ch_auto.isChecked()==true){
                        SharedPreferences auto = getSharedPreferences("auto",Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autologin = auto.edit();
                        loginId = auto.getString("inputId",edt_id.getText().toString());
                        autologin.putString("inputId",edt_id.getText().toString());
                        autologin.putString("inputPw",edt_pw.getText().toString());
                        autologin.commit();
                        Toast.makeText(LoginActivity.this, loginId+"님 환영합니다", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (edt_id.getText().toString().equals("hi") && edt_pw.getText().toString().equals("123")) {
                        loginId = auto.getString("inputId",edt_id.getText().toString());
                        Toast.makeText(LoginActivity.this, loginId+"님 환영합니다", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("id",edt_id.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }



    }
}