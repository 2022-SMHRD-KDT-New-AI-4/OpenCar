package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.lmj.opencar.Chart.BarChartClass;
import com.lmj.opencar.Chart.HoriBarChartClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class PatternActivity extends AppCompatActivity {
    LineChart linechart;
    RequestQueue queue;
    StringRequest request_sleep_my_hour_count,request_sleep_my_month_avg_count;
    StringRequest request_sleep_my_month_count,request_sleep_my_all,request_sleep_time_rank;
    StringRequest request_myinfo,request_my_drive_info;
    PortClass port;
    TextView tv_monavg,tv_month,tv_model,tv_sltime,tv_ticheck,tv_slcheck;
    TextView tv_drhour, tv_freq, tv_modelcheck;
    String loginId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);

        BarChartClass bc = new BarChartClass();
        HoriBarChartClass hc = new HoriBarChartClass();

        bc.barchart2 = findViewById(R.id.barchart);
        hc.barChart = findViewById(R.id.chart2);

        linechart = findViewById(R.id.chart);
        tv_monavg = findViewById(R.id.tv_monavg);
        tv_month = findViewById(R.id.tv_month);
        tv_model = findViewById(R.id.tv_model);
        tv_sltime = findViewById(R.id.tv_sltime);
        tv_ticheck = findViewById(R.id.tv_ticheck);
        tv_slcheck = findViewById(R.id.tv_slcheck);
        tv_drhour = findViewById(R.id.tv_drhour);
        tv_freq = findViewById(R.id.tv_freq);
        tv_modelcheck = findViewById(R.id.tv_modelcheck);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId",null); // 자동 로그인 캐시
        loginId = auto.getString("inputId",null); // 일반 로그인 캐시


        // 통로는 한개만 있어두 됨
        queue = Volley.newRequestQueue(getApplicationContext());



        // volley로 나이대와 차종 받기 및 그래프 그리기

        String url = port.port+"my_info/"+loginId; // 아이디 수정필요

        request_myinfo = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(response);
                    String[] myinfo = new String[2];

                    // json 가져오기 -----> ★★★★
                    myinfo[0] = jo.getString("user_age"); // 나이대
                    myinfo[1] = jo.getString("user_model");// 차종

                    // 차종 정보
                    tv_modelcheck.setText(myinfo[1]);


                    // HorizontalBarChart 전체 졸음빈도수
                    hc.configureChartAppearance(myinfo);
                    volleySleepcount();


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Err onResponseJson: " + e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("herehere","Err onResponseJson: " + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatternActivity.this, "Err ErrorListener: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override //response를 UTF8로 변경해주는 소스코드
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }
        };

        queue.add(request_myinfo);









        // LineChart 졸음시간대(6개월)
        configureChartAppearance(linechart,4);
        //volley에서 값 찾아서 return
        volleyGet();

        // BarChart 월별 졸음빈도수
        bc.makeBarChart();
        volleyMonth();




        // volley 주행 날짜 & 주행시간 & 주행시작 후 첫 번째 졸음감지 & 최근 주행 졸음 빈도수
        url = port.port+"my_drive_info/"+loginId; // 아이디 수정필요

        request_my_drive_info = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(response);

                    if(jo.length() !=0){
                        // json 가져오기 -----> ★★★★
                        int driveTime = Integer.parseInt(jo.getString("drive_time"));
                        int slTime = Integer.parseInt(jo.getString("sl_time"));
                        int freq = Integer.parseInt(jo.getString("freq"));


                        tv_ticheck.setText(jo.getString("day")); // 최근날짜
                        tv_drhour.setText("◾   주행시간 "+driveTime/60+"시간 "+driveTime%60+"분");
                        tv_slcheck.setText("◾   주행시작 "+slTime/60+"시간 "+slTime%60+"분 후 졸음감지");
                        tv_freq.setText("◾   총 "+freq+"회 졸음감지");
                    } else {
                        tv_ticheck.setText("");
                        tv_drhour.setText("◾   주행시간 정보가 없습니다.");
                        tv_slcheck.setText("◾   주행시작 정보가 없습니다.");
                        tv_freq.setText("◾   졸음감지 정보가 없습니다.");
                    }









                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Err onResponseJson: " + e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("herehere","Err onResponseJson: " + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatternActivity.this, "Err ErrorListener: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(request_my_drive_info);



        // 월별 평균 졸음빈도수
        url = port.port+"sleep_my_month_avg_count/"+loginId; // 아이디 수정필요

        request_sleep_my_month_avg_count = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(response);

                    // json 가져오기 -----> ★★★★
                    String monthavg = jo.getJSONObject("0").getString("monthavg");

                    if(!(monthavg.equals("None"))){
                        float cnt = Float.parseFloat(monthavg); // 평균
                        tv_monavg.setText(cnt+"회");
                    }



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Err onResponseJson: " + e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("herehere","Err onResponseJson: " + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatternActivity.this, "Err ErrorListener: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(request_sleep_my_month_avg_count);




        // 졸음시간대 top2
        url = port.port+"sleep_time_rank/"+loginId; // 아이디 수정필요

        request_sleep_time_rank = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(response);

                    int[] times = new int[2];

                    // json 가져오기 -----> ★★★★ null에러 뜨는 지 확인하기
                    for(int i=0;i<jo.length();i++){
                        times[i] = Integer.parseInt(jo.getJSONObject(Integer.toString(i)).getString("time")); // 시간1
                    }
//                    tv_monavg.setText(cnt+"회");

                    if(times[1] != 0){
                        tv_sltime.setText(times[0]+"시와 "+times[1]+"시에 많이 졸아요");
                    } else if((times[0] != 0)) {
                        tv_sltime.setText(times[0]+"시에 많이 졸아요");
                    } else {
                        tv_sltime.setText("");
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Err onResponseJson: " + e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("herehere","Err onResponseJson: " + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatternActivity.this, "Err ErrorListener: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(request_sleep_time_rank);







    }





    // *** LineChart 졸음시간대(6개월)
    // LineChart의 기본적인 것들을 세팅
    public void configureChartAppearance(LineChart lineChart, int range) {

//        lineChart.setExtraBottomOffset(15f); // 간격
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);// chart 밑에 description 표시 유무
        lineChart.setExtraOffsets(10f, 0f, 10f, 15f);


        // Legend는 차트의 범례
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);


        // XAxis (아래쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // x축 데이터 표시 위치
        xAxis.setGranularity(1f);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(Color.rgb(118, 118, 118));
        xAxis.setSpaceMin(0.5f); // Chart 맨 왼쪽 간격 띄우기
        xAxis.setSpaceMax(0.5f); // Chart 맨 오른쪽 간격 띄우기

        // YAxis(Right) (왼쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        YAxis yAxisLeft = lineChart.getAxisLeft();
//        yAxisLeft.setTextSize(14f);
        yAxisLeft.setEnabled(false);
//        yAxisLeft.setTextColor(Color.rgb(163, 163, 163));
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setAxisLineWidth(2);
        yAxisLeft.setAxisMinimum(0f); // 최솟값
//        yAxisLeft.setAxisMaximum((float) RANGE[0][range]); // 최댓값
//        yAxisLeft.setGranularity((float) RANGE[1][range]);
//        yAxisLeft.setAxisMaximum((float) 5); // 최댓값
//        yAxisLeft.setGranularity((float) 5);

        // YAxis(Left) (오른쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setDrawLabels(false); // label 삭제
        yAxis.setTextColor(Color.rgb(163, 163, 163));
        yAxis.setDrawAxisLine(false);
        yAxis.setAxisLineWidth(2);
        yAxis.setAxisMinimum(0f); // 최솟값
//        yAxis.setAxisMaximum((float) RANGE[0][range]); // 최댓값
//        yAxis.setGranularity((float) RANGE[1][range]);
//        yAxis.setAxisMaximum((float) 5); // 최댓값
        yAxis.setGranularity((float) 5);


        // XAxis에 원하는 String 설정하기 (시간)
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                return Integer.toString((int) value);
            }
        });
    }


    // 졸음 시간대 데이터 가져오기
    public void volleyGet(){
        HashMap<Integer,Integer> map = new HashMap<>();

        String url = port.port+"sleep_my_hour_count/"+loginId; // 아이디 수정필요

        request_sleep_my_hour_count = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jo = new JSONObject(response);

                    // 1~24시 추출
                    // json 가져오기 -----> ★★★★★
                    for (int j = 0; j<jo.length();j++){
                        int time = Integer.parseInt(jo.getJSONObject(Integer.toString(j)).getString("sl_start_time")); // 시간대
                        int count = Integer.parseInt(jo.getJSONObject(Integer.toString(j)).getString("sleep_hour_count")); // 횟수
                        map.put(time,count);
//                        Log.d("hereherein",time+","+count);
                    }

                    // lineChart 생성
                    prepareChartData(createChartData(map),linechart);


                    Log.d("hereheremethod",response);
                    Log.d("hereherelength",jo.length()+"");



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Err onResponseJson: " + e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("herehere","Err onResponseJson: " + e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatternActivity.this, "Err ErrorListener: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(request_sleep_my_hour_count);


    }


    //HorizontalBarChart와 마찬가지로 LineChart에 표시될 데이터를 생성한다. 다른 점은 DataSet이 하나가 아닌 4개이다.
    //따라서, BarChart에 보여질 데이터에는 4의 BarDataSet이 있다.
    public LineData createChartData(HashMap<Integer,Integer> map) {
        ArrayList<Entry> entry1 = new ArrayList<>(); // 앱1
        LineData chartData = new LineData();



//        // 1~24시 추출
        for (int i = 1; i <= 24; i++) {

            if (map.containsKey(i)){
                entry1.add(new Entry(i, map.get(i)));
//                Log.d("herehereout",i+","+map.get(i));
            } else {
                entry1.add(new Entry(i, 0));
//                Log.d("herehereout",i+","+0);
            }

        }

        // 4개 앱의 DataSet 추가 및 선 커스텀

        // 앱1
        LineDataSet lineDataSet1 = new LineDataSet(entry1, "hello");
        chartData.addDataSet(lineDataSet1);

        lineDataSet1.setLineWidth(3);
//        lineDataSet1.setCircleRadius(6);
        lineDataSet1.setDrawValues(false);
        lineDataSet1.setDrawCircleHole(false);
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setDrawHorizontalHighlightIndicator(false);
        lineDataSet1.setDrawHighlightIndicators(false);
        lineDataSet1.setColor(Color.rgb(31, 120, 180));
        lineDataSet1.setCircleColor(Color.rgb(31, 120, 180));


        chartData.setValueTextSize(15);
        return chartData;
    }

    // 생성된 BarData를 실제 BarChart 객체에 전달하고 BarChart를 갱신해 데이터를 표시
    public void prepareChartData(LineData data, LineChart lineChart) {
        lineChart.setData(data); // LineData 전달
        lineChart.invalidate(); // LineChart 갱신해 데이터 표시
//        lineChart.animateY(3000);
        lineChart.animateX(3000);
    }



    // hori그래프 데이터
    public void volleySleepcount(){
        HoriBarChartClass hc = new HoriBarChartClass();
        hc.barChart = findViewById(R.id.chart2);
        tv_model = findViewById(R.id.tv_model);


        // 1. 나의 총 졸음운전 빈도수 & 전체 평균 졸음운전 빈도수
        String url = port.port+"sleep_my_all/"+loginId; // 아이디 수정필요
        request_sleep_my_all = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    float ref = 0;
                    String fe = "더";

                    JSONObject jo = new JSONObject(response);
                    // 여기서 데이터 값 빼오기

                    // json 가져오기 -----> ★★★★★
                    float my = Float.parseFloat(jo.getJSONObject("3").getString("freq")); // 나
                    float all = Float.parseFloat(jo.getJSONObject("1").getString("freq")); // 전체
                    float age = Float.parseFloat(jo.getJSONObject("0").getString("freq")); // 나이대
                    float model = Float.parseFloat(jo.getJSONObject("2").getString("freq")); // 차종

//                    Log.d("hereherehori","my:"+my);
//                    Log.d("hereherehori","all:"+all);
//                    Log.d("hereherehori","age:"+age);
//                    Log.d("hereherehori","model:"+model);

                    if(my>=model){
                        ref = my-model;
                    } else if(my<model) {
                        ref = model-my;
                        fe="덜";
                    }

                    // ★★★★★★★★이부분 수정해야함!★★★★★★★★★
                    tv_model.setText(" 평균보다 "+ref+"회 "+fe+" 졸아요");
                    hc.prepareChartData(hc.createChartData(new float[]{all,age,model,my}));





                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Err onResponseJson: " + e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("herehere","Err onResponseJson: " + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatternActivity.this, "Err ErrorListener: " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("herehere","Err ErrorListener: " + error.toString());

            }
        }){
            @Override //response를 UTF8로 변경해주는 소스코드
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }
        };

        queue.add(request_sleep_my_all);

    }


    // 나의 월별 졸음운전 빈도수 출력(6개월)
    public void volleyMonth(){
        BarChartClass bc = new BarChartClass();
        bc.barchart2 = findViewById(R.id.barchart);
        tv_month = findViewById(R.id.tv_month);

        HashMap<Integer,Integer> map = new HashMap<>();
        String url = port.port+"sleep_my_month_count/"+loginId; // 아이디 수정필요

        request_sleep_my_month_count = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    String re = "낮아요";
                    JSONObject jo = new JSONObject(response);



                    // 1~24시 추출
                    // json 가져오기 -----> ★★★★★★★★★★여기 수정해야함!!!!!!!!!!!
                    for (int j = 0; j<jo.length();j++){
                        int month = Integer.parseInt(jo.getJSONObject(Integer.toString(j)).getString("month")); // 월
                        int count = Integer.parseInt(jo.getJSONObject(Integer.toString(j)).getString("sleep_my_month_count")); // 횟수
                        map.put(month,count);
//                        Log.d("hereherein",time+","+count);
                    }


                    BarData data = bc.createBarData(map);

                    if(jo.length()!=0){
                        String dat = data.getDataSets().toString();
                        String[] arr = dat.split(",");


                        int idx = arr[8].indexOf("y");
                        int idx_1 = arr[8].indexOf("]");
                        Log.d("hereherearr","arr8:"+arr[8].substring(idx+3,idx_1-1));
                        int idx2 = arr[7].indexOf("y");
                        int idx2_1 = arr[7].indexOf("E");
                        Log.d("hereherearr","arr7:"+arr[7].substring(idx2+3,idx2_1-1));

                        float m5 = Float.parseFloat(arr[8].substring(idx+3,idx_1-1));
                        float m4 = Float.parseFloat(arr[7].substring(idx2+3,idx2_1-1));

                        if(m4<m5){
                            re = "높아요";
                        } else if (m4==m5){
                            re = "같아요";
                        }



                        tv_month.setText("지난 달에 비해 졸음빈도가 "+re);
                    } else {
                        tv_month.setText("");
                    }




                    // BarChart 생성
                    bc.prepareBar(data);








                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Err onResponseJson: " + e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("herehere","Err onResponseJson: " + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatternActivity.this, "Err ErrorListener: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(request_sleep_my_month_count);

    }


}