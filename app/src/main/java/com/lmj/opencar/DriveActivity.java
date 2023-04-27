package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class DriveActivity extends AppCompatActivity {
    Button btn_fin;
    RequestQueue queue;
    PortClass port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        btn_fin = findViewById(R.id.btn_fin);

        queue = Volley.newRequestQueue(getApplicationContext());

        Intent it_get = getIntent();
        String dr_seq = it_get.getStringExtra("dr_seq");


        btn_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = port.port+"driver_update/"+dr_seq;
                StringRequest request_driver = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("응답", response);
                        Intent intent = new Intent(DriveActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error in response", "Error :"+error.toString());
                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> param = new HashMap<>();

                        param.put("dr_seq", dr_seq);
                        return param;
                    }
                };
                queue.add(request_driver);
            }
        });
    }
}