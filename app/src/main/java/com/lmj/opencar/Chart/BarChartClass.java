package com.lmj.opencar.Chart;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;

public class BarChartClass {
    public BarChart barchart2;


    // barChart 졸음빈도수
    public void makeBarChart() {

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

        String[] months = {"12월", "1월", "2월", "3월", "4월", "5월"};


        // XAxis에 원하는 String 설정하기 (날짜)
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                return months[(int) value];
            }
        });

    }



    public BarData createBarData(HashMap<Integer,Integer> map) {


        // 1. [BarEntry] BarChart에 표시될 데이터 값 생성
        ArrayList<BarEntry> values = new ArrayList<>();


        // 0->12월, 1->1, 2->2, 3->3, 4->4, 5->5
        for (int i = 0; i < 6; i++) { // 데이터 개수

            if (i != 0) {

                if (map.containsKey(i)) {
                    values.add(new BarEntry(i, map.get(i)));
                    Log.d("hereherebar", i + "," + map.get(i));
                } else {
                    values.add(new BarEntry(i, 0));
                    Log.d("hereherebar", i + "," + 0);
                }
            } else {
                if (map.containsKey(12)) {
                    values.add(new BarEntry(0, map.get(12)));
                    Log.d("hereherebar", i + "," + map.get(12));
                } else {
                    values.add(new BarEntry(0, 0));
                    Log.d("hereherebar", i + "," + 0);
                }

            }

        }


        BarDataSet barDataSet = new BarDataSet(values, "check");
        barDataSet.setDrawIcons(false);
        barDataSet.setDrawValues(false);
        barDataSet.setColors(Color.parseColor("#E1E2E7"),Color.parseColor("#E1E2E7"),Color.parseColor("#E1E2E7"),Color.parseColor("#E1E2E7"),Color.parseColor("#D0DFFC"),Color.parseColor("#4D95F7"));
//        barDataSet.setValueTextColor(Color.rgb(163, 163, 163));
//        barDataSet.setValueTextSize(10f);

        BarData barData = new BarData(barDataSet);

        barData.setBarWidth(0.6f);

        return barData;
    }

    public void prepareBar(BarData data) {

//        barchart2.setFitBars(true);
        barchart2.setData(data);
        barchart2.invalidate();
        barchart2.animateY(2000);


    }
}
