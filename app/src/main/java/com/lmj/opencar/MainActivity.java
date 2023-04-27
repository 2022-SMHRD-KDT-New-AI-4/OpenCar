package com.lmj.opencar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.kakao.sdk.user.UserApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RequestQueue queue;
    PortClass port;
    Button btn_logout;
    TextView tv_user;
    ImageView img_logout,img_bell,iv_pattern,iv_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_user= findViewById(R.id.tv_user);
        img_logout = findViewById(R.id.img_logout);
        img_bell = findViewById(R.id.img_bell);
        iv_pattern = findViewById(R.id.iv_pattern);
        iv_start = findViewById(R.id.iv_start);

        queue = Volley.newRequestQueue(getApplicationContext());

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


        // 로그 아웃 버튼 이벤트
        img_logout.setOnClickListener(new View.OnClickListener() {
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
                String url = port.port + "drive_insert/"+inputText;
                StringRequest request_start = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            Log.d("응답",response);
                            Intent it_start = new Intent(MainActivity.this, DriveActivity.class);
                            it_start.putExtra("id",inputText);
                            it_start.putExtra("dr_seq",response);
                            startActivity(it_start);
                            finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error in response", "Error :"+ error.toString());
                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> param = new HashMap<>();

                        param.put("User_id",inputText);
                        return param;
                    }
                };
                    request_start.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(request_start);

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