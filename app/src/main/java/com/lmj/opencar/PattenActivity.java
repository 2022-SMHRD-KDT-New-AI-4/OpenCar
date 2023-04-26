package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

public class PattenActivity extends AppCompatActivity {
    HorizontalBarChart barChart;
    LineChart linechart;
    BarChart barchart2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patten);
        barChart = findViewById(R.id.chart2);
        linechart = findViewById(R.id.chart);
        barchart2 = findViewById(R.id.barchart);


        // HorizontalBarChart 전체 졸음빈도수
        configureChartAppearance();
        prepareChartData(createChartData());

        // LineChart 졸음시간대(6개월)
        configureChartAppearance(linechart,4);
        prepareChartData(createChartData(4),linechart);

        // BarChart 월별 졸음빈도수
        makeBarChart();



    }


    // *** HorizontalBarChart --> 전체 졸음빈도수
    // BarChart의 기본적인 것들을 세팅
    private void configureChartAppearance() {

        barChart.getDescription().setEnabled(false); // chart 밑에 description 표시 유무
        barChart.setTouchEnabled(false); // 터치 유무
        barChart.getLegend().setEnabled(false); // Legend는 차트의 범례
        barChart.setExtraOffsets(10f, 0f, 40f, 0f);

        // XAxis (수평 막대 기준 왼쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(15f);
        xAxis.setGridLineWidth(25f);
        xAxis.setGridColor(Color.parseColor("#FFFFFF"));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X 축 데이터 표시 위치


        // YAxis(Left) (수평 막대 기준 아래쪽) - 선 유무, 데이터 최솟값/최댓값, label 유무
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setAxisMinimum(0f); // 최솟값
        axisLeft.setAxisMaximum(50f); // 최댓값
        axisLeft.setGranularity(1f); // 값만큼 라인선 설정
        axisLeft.setDrawLabels(false); // label 삭제

        // YAxis(Right) (수평 막대 기준 위쪽) - 사이즈, 선 유무
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setTextSize(15f);
        axisRight.setDrawLabels(false); // label 삭제
        axisRight.setDrawGridLines(false);
        axisRight.setDrawAxisLine(false);

        String[] name = {"전체","나"};

        // XAxis에 원하는 String 설정하기 (앱 이름)
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return name[(int) value];
            }
        });

    }


    // BarChart에 표시될 데이터를 생성
    private BarData createChartData() {

        // 1. [BarEntry] BarChart에 표시될 데이터 값 생성
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < 2; i++) { // 데이터 개수
            float x = i;
            float y = 10+i;
            values.add(new BarEntry(x, y));
        }

        // 2. [BarDataSet] 단순 데이터를 막대 모양으로 표시, BarChart의 막대 커스텀
        BarDataSet set2 = new BarDataSet(values, "values");
        set2.setDrawIcons(false);
        set2.setDrawValues(true);
        set2.setColors(Color.parseColor("#D0DFFC"),Color.parseColor("#4D95F7")); // 색상 설정



        // 데이터 값 원하는 String 포맷으로 설정하기 (ex. ~회)
        set2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (String.valueOf((int) value)) + "번";
            }
        });

        // 3. [BarData] 보여질 데이터 구성
        BarData data = new BarData(set2);
        data.setBarWidth(0.5f);
        data.setValueTextSize(15);

        return data;
    }


    // 위의 생성된 BarData를 실제 BarChart 객체에 전달하고 BarChart를 갱신해 데이터를 표시한다.
    private void prepareChartData(BarData data) {
        barChart.setData(data); // BarData 전달
        barChart.invalidate(); // BarChart 갱신해 데이터 표시
        barChart.animateY(2000);
    }


    // *** LineChart 졸음시간대(6개월)
    // LineChart의 기본적인 것들을 세팅
    private void configureChartAppearance(LineChart lineChart, int range) {

//        lineChart.setExtraBottomOffset(15f); // 간격
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);// chart 밑에 description 표시 유무
        lineChart.setExtraOffsets(10f, 0f, 10f, 15f);


        // Legend는 차트의 범례
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setFormSize(10);
//        legend.setTextSize(10);
//        legend.setTextColor(Color.parseColor("#A3A3A3"));
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        legend.setDrawInside(false);
//        legend.setYEntrySpace(5);
//        legend.setWordWrapEnabled(false);
//        legend.setXOffset(80f);
//        legend.setYOffset(20f);
//        legend.getCalculatedLineSizes();

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


        // XAxis에 원하는 String 설정하기 (날짜)
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                return Integer.toString((int) value+1);
            }
        });
    }



    //HorizontalBarChart와 마찬가지로 LineChart에 표시될 데이터를 생성한다. 다른 점은 DataSet이 하나가 아닌 4개이다.
    //따라서, BarChart에 보여질 데이터에는 4의 BarDataSet이 있다.
    private LineData createChartData(int range) {
        ArrayList<Entry> entry1 = new ArrayList<>(); // 앱1


        LineData chartData = new LineData();
        Random rd = new Random();

        // 랜덤 데이터 추출
        for (int i = 1; i < 24; i++) {

            int val1 = rd.nextInt(4); // 졸음 빈도수 값
            entry1.add(new Entry(i, val1));

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
    private void prepareChartData(LineData data, LineChart lineChart) {
        lineChart.setData(data); // LineData 전달
        lineChart.invalidate(); // LineChart 갱신해 데이터 표시
//        lineChart.animateY(3000);
        lineChart.animateX(3000);
    }



    // barChart 졸음빈도수
    private void makeBarChart(){
        barchart2.getDescription().setEnabled(false); // chart 밑에 description 표시 유무
        barchart2.setTouchEnabled(false); // 터치 유무
        barchart2.getLegend().setEnabled(false); // Legend는 차트의 범례
        barchart2.setExtraOffsets(25f, 0f, 40f, 20f);

        // XAxis (수평 막대 기준 왼쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis = barchart2.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(15f);
        xAxis.setGridLineWidth(25f);
        xAxis.setGridColor(Color.parseColor("#FFFFFF"));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X 축 데이터 표시 위치

        // YAxis(Left) (수평 막대 기준 아래쪽) - 선 유무, 데이터 최솟값/최댓값, label 유무
        YAxis axisLeft = barchart2.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setAxisMinimum(0f); // 최솟값
//        axisLeft.setAxisMaximum(10f); // 최댓값
        axisLeft.setGranularity(1f); // 값만큼 라인선 설정
        axisLeft.setDrawLabels(false); // label 삭제

        // YAxis(Right) (수평 막대 기준 위쪽) - 사이즈, 선 유무
        YAxis axisRight = barchart2.getAxisRight();
        axisRight.setTextSize(15f);
        axisRight.setDrawLabels(false); // label 삭제
        axisRight.setDrawGridLines(false);
        axisRight.setDrawAxisLine(false);

        String[] months = {"12월","1월","2월","3월","4월","5월"};
        // XAxis에 원하는 String 설정하기 (날짜)
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                return months[(int)value];
            }
        });

        Random rd = new Random();
        // 1. [BarEntry] BarChart에 표시될 데이터 값 생성
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i =0; i < 6; i++) { // 데이터 개수
            int x = i;
            int y = rd.nextInt(5);

            values.add(new BarEntry(x, y));
        }


        BarDataSet barDataSet = new BarDataSet(values, "check");
        barDataSet.setDrawIcons(false);
        barDataSet.setDrawValues(false);
        barDataSet.setColor(Color.parseColor("#4D95F7"));
//        barDataSet.setValueTextColor(Color.rgb(163, 163, 163));
//        barDataSet.setValueTextSize(10f);


        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.6f);

//        barchart2.setFitBars(true);
        barchart2.setData(barData);
        barchart2.invalidate();
//        barchart2.getDescription().setText("Bar Chart Example");
        barchart2.animateY(2000);


    }
}