package com.lmj.opencar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static RequestQueue queue;
    PortClass port;
    String id;
    TextView tv_user;
    ImageView img_logout,img_bell,iv_pattern,iv_start;
    StringRequest request_start;
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
                Intent it_next2 = new Intent(MainActivity.this, PatternActivity.class);
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
                if (inputText != null){
                    id = inputText;
                } else if (nid != null) {
                    id = nid;
                }

                String url = port.port+"drive_insert/"+id+"/";

                 request_start = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            Integer data=jo.getInt("dr_seq");
                            Intent it_start = new Intent(MainActivity.this, DriveActivity.class);
                            it_start.putExtra("data",data);
                            it_start.putExtra("dr_seq", response);
                            startActivity(it_start);
                            finish();
                            Log.d("응답","data");
                            Log.d("응답",response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error in response", "Error :"+ error.getMessage());
                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> param = new HashMap<>();

                        param.put("Used_id",id);

                        return param;
                    }
                };
                 request_start.setShouldCache(false);
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