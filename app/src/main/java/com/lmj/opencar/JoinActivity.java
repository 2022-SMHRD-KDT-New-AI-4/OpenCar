package com.lmj.opencar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {
    Button btn_join;
    EditText id,pw,birth,name;
    RequestQueue queue;
    StringRequest request_join;
    PortClass port;
    Spinner sp_list;
    String[] items = {"차종","승용차","승합차","화물차","특수차"};

    String model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        btn_join = findViewById(R.id.btn_join);
        id = findViewById(R.id.edt_id);
        pw = findViewById(R.id.edt_pw);
        birth = findViewById(R.id.edt_bir);
        name = findViewById(R.id.edt_name);
        sp_list = findViewById(R.id.sp_list);

        // 어댑터 생성
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,items);
        // ui와 연결
        sp_list.setAdapter(adapter);


        sp_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override // position 으로 몇번째 것이 선택되었는지 값을 넘겨준다.
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 1:
                        if (position == 1){
                            model = "승용차";
                            Log.d("응답1",model);
                        }
                        break;
                    case 2:
                        if (position == 2){
                            model = "승합차";
                            Log.d("응답2",model);
                        }
                        break;
                    case 3:
                        if (position == 3){
                            model = "화물차";
                            Log.d("응답3",model);
                        }
                        break;
                    case 4:
                        if (position == 4){
                            model = "특수차";
                            Log.d("응답4",model);
                        }
                        break;
                }
            }





            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







        queue = Volley.newRequestQueue(getApplicationContext());

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = port.port +"join/";
                request_join = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("응답", response.toString());

                        Intent it_login = new Intent(JoinActivity.this, LoginActivity.class);
                        startActivity(it_login);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("오류",error.toString());
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError{

                        Map<String, String> param = new HashMap<>();

                        param.put("User_id",id.getText().toString());
                        param.put("User_pw",pw.getText().toString());
                        param.put("User_name",name.getText().toString());
                        param.put("User_birthdate",birth.getText().toString());
                        param.put("User_model", model);
                        return param;
                    }
                };
                queue.add(request_join);

            }
        });





    }// onCreate

}// class