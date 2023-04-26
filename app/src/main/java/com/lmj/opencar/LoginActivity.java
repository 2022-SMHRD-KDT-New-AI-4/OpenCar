package com.lmj.opencar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText edt_id,edt_pw;
    Button btn_login;
    CheckBox ch_auto;
    String loginId,loginPw;
    TextView tv_join;
    RequestQueue queue;
    PortClass port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_id =  findViewById(R.id.edt_id);
        edt_pw =  findViewById(R.id.edt_pw);
        btn_login = findViewById(R.id.btn_join);
        ch_auto =  findViewById(R.id.ch_auto);
        tv_join = findViewById(R.id.tv_join);

        // 통로는 한개만 있어두 됨
        queue = Volley.newRequestQueue(getApplicationContext());

        // 회원가입 페이지로 이동
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
            Toast.makeText(LoginActivity.this, loginId+"님 자동 로그인", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (loginId == null && loginPw == null) {
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = port.port + "login/";/// 3
                    StringRequest request_login = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {  /// 4
                        @Override
                        public void onResponse(String response) {
                            Log.d("응답", response);

                            if (response != null && ch_auto.isChecked()==true){
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

                            }else if (response != null && ch_auto.isChecked()==false){
                                loginId = auto.getString("inputId",edt_id.getText().toString());
                                Toast.makeText(LoginActivity.this, loginId+"님 환영합니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("id",edt_id.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error in response","Error :"+error.toString());
                        }
                    }){
                        protected Map<String, String> getParams() throws AuthFailureError{  ///6
                            Map<String, String> param = new HashMap<>();

                            param.put("User_id",edt_id.getText().toString());
                            param.put("User_pw",edt_pw.getText().toString());
                            return param;
                        }
                    };
                    request_login.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                            20000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(request_login);

                }
            });
        }


    }
}