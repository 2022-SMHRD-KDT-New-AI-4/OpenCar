package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {
    ArrayList<AlarmVO> data = new ArrayList<>();
    RecyclerView recyclerView;
    RequestQueue queue;
    StringRequest request_alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        recyclerView = findViewById(R.id.recyclerView);

        // 통로는 한개만 있어두 됨
//        queue = Volley.newRequestQueue(getApplicationContext());
//
//        String url = "http://218.157.24.41:5000/alarm_select/test1";
//
//
//        request_alarm = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
//                try {
//                    JSONObject jo = new JSONObject(response);
//
//
//
////                    data.add(new AlarmVO(jo.getJSONObject("0").getString("MSG"),jo.getJSONObject("0").getString("send_Time")));
////                    data.add(new AlarmVO(jo.getString("MSG"),jo.getString("send_Time")));
//                    Log.d("herehere",response);
//
//
//
//
//
//                } catch (JSONException e) {
//                    Toast.makeText(getApplicationContext(), "Err onResponseJson: " + e.toString(), Toast.LENGTH_LONG).show();
//                    Log.d("herehere","Err onResponseJson: " + e.toString());
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(AlarmActivity.this, "Err ErrorListener: " + error.toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        }){
//            @Override //response를 UTF8로 변경해주는 소스코드
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                try {
//                    String utf8String = new String(response.data, "UTF-8");
//                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
//                } catch (UnsupportedEncodingException e) {
//                    // log error
//                    return Response.error(new ParseError(e));
//                } catch (Exception e) {
//                    // log error
//                    return Response.error(new ParseError(e));
//                }
//            }
//        };
//
//
//        queue.add(request_alarm);

        data.add(new AlarmVO("졸음운전 주의시간 입니다.","2020.03.22 11:40:00"));
        data.add(new AlarmVO("졸음운전이 감지되었습니다.","2020.03.21 10:40:00"));
        data.add(new AlarmVO("졸음운전 주의시간 입니다.","2020.03.22 11:40:00"));



        AlarmAdapter adapter = new AlarmAdapter(data, getApplicationContext());
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));




    }
}