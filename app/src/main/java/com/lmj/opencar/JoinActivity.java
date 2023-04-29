package com.lmj.opencar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
//    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        btn_join = findViewById(R.id.btn_join);
        id = findViewById(R.id.edt_id);
        pw = findViewById(R.id.edt_pw);
        birth = findViewById(R.id.edt_bir);
        name = findViewById(R.id.edt_name);

        //어댑터 생성
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array,R.layout.activity_join);
        //드롭다운뷰 연결
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        //UI와 연결
//        binding.homeSpinner.setAdapter(adapter);
    }

    //Spinner Listener
    public void spinnerListener() {
//        binding.homeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            //선택 시 작동기능
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 1:
//                        break;
//                    case 2:
//                        break;
//                    case 3:
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });






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

                        return param;
                    }
                };
                queue.add(request_join);

            }
        });
    }
}