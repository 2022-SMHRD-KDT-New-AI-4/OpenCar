package com.lmj.opencar;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.lmj.opencar.Drive.InfoAdapter;
import com.lmj.opencar.Drive.InfoVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyinfoActivity extends AppCompatActivity {
    ArrayList<InfoVO> data = new ArrayList<>();
    RecyclerView recyclerView;
    RequestQueue queue;
    StringRequest request_driveinfo;
    InfoAdapter adapter;
    PortClass port;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        recyclerView = findViewById(R.id.rv_drive);

        // 통로는 한개만 있어두 됨
        queue = Volley.newRequestQueue(getApplicationContext());


        // volley 주행 날짜 & 주행시간 & 주행시작 후 첫 번째 졸음감지 & 최근 주행 졸음 빈도수
        String url = port.port+"my_drive_info/"+"test1"; // 아이디 수정필요

        request_driveinfo = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(response);

                    // json 가져오기 -----> ★★★★
                    int driveTime = Integer.parseInt(jo.getString("drive_time"));
                    int slTime = Integer.parseInt(jo.getString("sl_time"));
                    int freq = Integer.parseInt(jo.getString("freq"));

                    // 여기서 정보 가져오기
//                    for (int i = 0; i<jo.length();i++){
//
//                        data.add(new AlarmVO(jo.getJSONObject(Integer.toString(i)).getString("MSG"),jo.getJSONObject(Integer.toString(i)).getString("send_Time")));
//                    }

//                    tv_ticheck.setText(jo.getString("day")); // 최근날짜
//                    tv_drhour.setText("◾   주행시간 "+driveTime/60+"시간 "+driveTime%60+"분");
//                    tv_slcheck.setText("◾   주행시작 "+slTime/60+"시간 "+slTime%60+"분 후 졸음감지");
//                    tv_freq.setText("◾   총 "+freq+"회 졸음감지");


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Err onResponseJson: " + e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("herehere","Err onResponseJson: " + e.toString());
                }

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyinfoActivity.this, "Err ErrorListener: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(request_driveinfo);


                data.add(new InfoVO("test","◾   주행시간 00시간 00분 00초","◾   주행시작 60시간 60분 후 졸음감지","◾   총 0회 졸음감지"));
                data.add(new InfoVO("test","◾   주행시간 00시간 00분 00초","◾   주행시작 60시간 60분 후 졸음감지","◾   총 0회 졸음감지"));
//        data.add(new AlarmVO("졸음운전이 감지되었습니다.","2020.03.21 10:40:00"));
//        data.add(new AlarmVO("졸음운전 주의시간 입니다.","2020.03.22 11:40:00"));



        adapter = new InfoAdapter(data, getApplicationContext());
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));







    }
}