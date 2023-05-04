package com.lmj.opencar.Chart;

import android.graphics.Color;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.lmj.opencar.PortClass;

import java.util.ArrayList;

public class HoriBarChartClass {
    public HorizontalBarChart barChart;

    // *** HorizontalBarChart --> 전체 졸음빈도수
    // BarChart의 기본적인 것들을 세팅
    public void configureChartAppearance(String[] myinfo) {

        barChart.getDescription().setEnabled(false); // chart 밑에 description 표시 유무
        barChart.setTouchEnabled(false); // 터치 유무
        barChart.getLegend().setEnabled(false); // Legend는 차트의 범례
        barChart.setExtraOffsets(10f, 0f, 40f, 0f);

        // XAxis (수평 막대 기준 왼쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(15f);
        xAxis.setGridLineWidth(5f);
        xAxis.setGridColor(Color.parseColor("#FFFFFF"));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X 축 데이터 표시 위치



        // YAxis(Left) (수평 막대 기준 아래쪽) - 선 유무, 데이터 최솟값/최댓값, label 유무
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setAxisMinimum(0f); // 최솟값
        axisLeft.setAxisMaximum(60f); // 최댓값
        axisLeft.setGranularity(1f); // 값만큼 라인선 설정
        axisLeft.setDrawLabels(false); // label 삭제

        // YAxis(Right) (수평 막대 기준 위쪽) - 사이즈, 선 유무
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setTextSize(10f);
        axisRight.setDrawLabels(false); // label 삭제
        axisRight.setDrawGridLines(false);
        axisRight.setDrawAxisLine(false);

        // myinfo[0]과 myinfo[1] 로 고치기!!!!!!!!!!
        // name[1]:나이대, name[2]:차종
        String[] name = {"전체",myinfo[0],myinfo[1],"나"};

        // XAxis에 원하는 String 설정하기 (앱 이름)
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return name[(int) value];
            }
        });

    }





    // BarChart에 표시될 데이터를 생성
    public BarData createChartData(float[] counts) {

        // 1. [BarEntry] BarChart에 표시될 데이터 값 생성
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < 4; i++) { // 데이터 개수
            float x = i;
            float y = counts[i];
            values.add(new BarEntry(x, y));
        }

        // 2. [BarDataSet] 단순 데이터를 막대 모양으로 표시, BarChart의 막대 커스텀
        BarDataSet set2 = new BarDataSet(values, "values");
        set2.setDrawIcons(false);
        set2.setDrawValues(true);
        set2.setColors(Color.parseColor("#D0DFFC"),Color.parseColor("#D0DFFC"),Color.parseColor("#D0DFFC"),Color.parseColor("#4D95F7")); // 색상 설정



        // 데이터 값 원하는 String 포맷으로 설정하기 (ex. ~회)
        set2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (String.valueOf(value)) + "회";
            }
        });

        // 3. [BarData] 보여질 데이터 구성
        BarData data = new BarData(set2);
        data.setBarWidth(0.6f);
        data.setValueTextSize(13);
        data.setValueTextColor(Color.rgb(163, 163, 163));


        /// ★★★★★★★★★★★★여기 폰트 check염~!~!~!~★★★★★★★★★★★★★
//        AssetManager am = getResources().getAssets();
//        data.setValueTypeface(Typeface.createFromAsset(,"font/scdream3.otf"));
//        data.setValueTypeface(Typeface.createFromFile("app/src/main/res/drawable/font/scdream3.otf"));



        //        Typeface face = Typeface.createFromAsset();
//        data.setValueTypeface(face)
//        val tf = Typeface.createFromAsset(assetManager, "font/montserrat_regular.ttf")
//        data.setValueTypeface(tf)

//        Typeface tf = Typeface.createFromAsset();

        return data;
    }


    // 위의 생성된 BarData를 실제 BarChart 객체에 전달하고 BarChart를 갱신해 데이터를 표시한다.
    public void prepareChartData(BarData data) {
        barChart.setData(data); // BarData 전달
        barChart.invalidate(); // BarChart 갱신해 데이터 표시
        barChart.animateY(2000);
    }
}