package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

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
    StringRequest request_sleep_my_month_count,request_sleep_my_all;
    PortClass port;
    TextView tv_monavg;


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


        // 통로는 한개만 있어두 됨
        queue = Volley.newRequestQueue(getApplicationContext());


        // HorizontalBarChart 전체 졸음빈도수
        hc.configureChartAppearance();
        volleySleepcount();

        // LineChart 졸음시간대(6개월)
        configureChartAppearance(linechart,4);
        //volley에서 값 찾아서 return
        volleyGet();

        // BarChart 월별 졸음빈도수
        bc.makeBarChart();
        volleyMonth();



        // 월별 평균 졸음빈도수
        String url = port.port+"sleep_my_month_avg_count/"+"test1"; // 아이디 수정필요

        request_sleep_my_month_avg_count = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(response);

                    // json 가져오기 -----> ★★★★
                    int cnt = Integer.parseInt(jo.getJSONObject("0").getString("monthavg")); // 평균
                    tv_monavg.setText(cnt+"회");

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

        String url = port.port+"sleep_my_hour_count/"+"test1"; // 아이디 수정필요

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

        // 1. 나의 총 졸음운전 빈도수 & 전체 평균 졸음운전 빈도수
        String url = port.port+"sleep_my_all/"+"test1"; // 아이디 수정필요
        request_sleep_my_all = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(response);
                    // 여기서 데이터 값 빼오기

                    // json 가져오기 -----> ★★★★★
                    int my = Integer.parseInt(jo.getJSONObject("0").getString("freq")); // 나
                    int all = Integer.parseInt(jo.getJSONObject("1").getString("freq")); // 전체

                    hc.prepareChartData(hc.createChartData(new int[]{all,my}));



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

        HashMap<Integer,Integer> map = new HashMap<>();
        String url = port.port+"sleep_my_month_count/"+"test1"; // 아이디 수정필요

        request_sleep_my_month_count = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jo = new JSONObject(response);

                    // 1~24시 추출
                    // json 가져오기 -----> ★★★★★★★★★★여기 수정해야함!!!!!!!!!!!
                    for (int j = 0; j<jo.length();j++){
                        int month = Integer.parseInt(jo.getJSONObject(Integer.toString(j)).getString("month")); // 월
                        int count = Integer.parseInt(jo.getJSONObject(Integer.toString(j)).getString("sleep_my_month_count")); // 횟수
                        map.put(month,count);
//                        Log.d("hereherein",time+","+count);
                    }

                    // BarChart 생성
                    bc.prepareBar(bc.createBarData(map));

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