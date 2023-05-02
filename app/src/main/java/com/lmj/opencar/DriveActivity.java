package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

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

public class DriveActivity extends AppCompatActivity {

    Button btn_fin;
    WebView wv_camera;
    RequestQueue queue;
    PortClass port;
    int count;
//.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        btn_fin = findViewById(R.id.btn_fin);

        wv_camera = findViewById(R.id.wv_camera);

        wv_camera.setWebViewClient(new WebViewClient());
        wv_camera.getSettings().setJavaScriptEnabled(true);

        wv_camera.getSettings().setUseWideViewPort(true); // wide viewport를 유연하게 설정하고
        wv_camera.getSettings().setLoadWithOverviewMode(true); // 컨텐츠가 웹뷰 범위에 벗어날 경우  크기에 맞게 조절


        wv_camera.loadUrl("http://220.80.88.51:5000/stream");

        queue = Volley.newRequestQueue(getApplicationContext());

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String inputText2 = auto.getString("inputId2","");
        SharedPreferences.Editor commonlogin = auto.edit();
        commonlogin.putString("inputId2",inputText2);
        commonlogin.commit();

        Intent it_get = getIntent();
        String data = it_get.getStringExtra("dr_seq");
        int dr_seq = it_get.getIntExtra("data",0);




        btn_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = port.port+"drive_update/"+dr_seq+"/";
                StringRequest request_driver = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("응답", response);
                        SharedPreferences auto = getSharedPreferences("auto",Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autologin = auto.edit();
                        autologin.commit();
                        Intent intent = new Intent(DriveActivity.this, MainActivity.class);
                        count = 3;
                        intent.putExtra("count",count);
                        startActivity(intent);
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

                        param.put("dr_seq", data);
                        return param;
                    }
                };
                queue.add(request_driver);
            }
        });
    }// onCreate 끝
    // 캐시 삭제하는 기능
//    @Override
//    protected void onDestroy() {
//        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
//        String inputText2 = auto.getString("inputId2","");
//        if (inputText2 != null) {
//            super.onDestroy();
//            Log.d("Click", "onDestroy");
//            clearApplicationDate(getApplicationContext());
//        }
//    }
//    public static void clearApplicationDate(Context context){
//        File cache = context.getCacheDir();
//        File appDir = new File(cache.getParent());
//        if (appDir.exists()){
//            String[] children = appDir.list();
//            for (String list : children){
//                Log.d("캐시삭제",list);
//                if (list.equals("cache")) continue;
//                deleteDir(new File(appDir, list));
//            }
//        }
//    }
//    private static boolean deleteDir(File dir){
//        if (dir != null && dir.isDirectory()){
//            String[] children = dir.list();
//            for (int i = 0; i<children.length; i++){
//                boolean success = deleteDir(new File(dir,children[i]));
//                if (!success){
//                    return false;
//                }
//            }
//        }
//        return dir.delete();
//    }
}// class 끝
