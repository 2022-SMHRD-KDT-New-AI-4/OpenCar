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
import android.util.Log;
import android.view.View;

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


import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
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
        String inputName = auto.getString("inputName","");

        tv_user.setText(inputName + "님");


        Intent intent2 = getIntent();
        String nid = intent2.getStringExtra("id");
        if (nid != null) {
            tv_user.setText(nid + "님");
        }


        final String RID = nid;
        final String RID2 = inputName;


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
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                String inputText = auto.getString("inputId", "");
                //String inputText2 = auto.getString("inputId2","");

//                if (inputText != null){
//                    id = inputText;
//                }if (nid != null){
//                    id = inputText2;
//                }

                String url = port.port+"drive_insert/"+inputText+"/";

                 request_start = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            Integer data=jo.getInt("dr_seq");
                            Intent it_start = new Intent(MainActivity.this, DriveActivity.class);
                            it_start.putExtra("data",data);
                            it_start.putExtra("dr_seq", response);
                            it_start.putExtra("inputText2",inputText);
                            startActivity(it_start);
                            // finish();
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

                        param.put("User_id", inputText);

                        return param;
                    }
                };
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

    // 캐시 삭제하는 기능
    @Override
    protected void onDestroy() {
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String inputText2 = auto.getString("inputId2","");
        Intent it_count = getIntent();
        int count = it_count.getIntExtra("count",0);
        if (inputText2 ==null) {
            if (inputText2 != null && (count % 2) == 1) {
                super.onDestroy();
                Log.d("Click", "onDestroy");
                clearApplicationDate(getApplicationContext());
            }
        }
    }
    public static void clearApplicationDate(Context context){
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()){
            String[] children = appDir.list();
            for (String list : children){
                Log.d("캐시삭제",list);
                if (list.equals("cache")) continue;
                deleteDir(new File(appDir, list));
            }
        }
    }
    private static boolean deleteDir(File dir){
        if (dir != null && dir.isDirectory()){
            String[] children = dir.list();
            for (int i = 0; i<children.length; i++){
                boolean success = deleteDir(new File(dir,children[i]));
                if (!success){
                    return false;
                }
            }
        }
        return dir.delete();
    }




}