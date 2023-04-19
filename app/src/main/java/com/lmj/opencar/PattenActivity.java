package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PattenActivity extends AppCompatActivity {
    TextView tv_title;
    HorizontalBarChart horizontalbarchart;
    LineChart linechart;
    BarChart barchart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patten);

        tv_title = findViewById(R.id.tv_title);
        horizontalbarchart = findViewById(R.id.horizontalbarChart);
        linechart = findViewById(R.id.linechart);
        barchart = findViewById(R.id.barchart);

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");

        tv_title.setText(id + "님의 졸음운전 패턴");

        // 1. 수평 막대 그래프
        horizontalbarchart.setDrawBarShadow(false);
        horizontalbarchart.setDrawValueAboveBar(true);
        horizontalbarchart.getDescription().setEnabled(false);
        horizontalbarchart.setPinchZoom(false);
        horizontalbarchart.setDrawGridBackground(true);

        XAxis xl = horizontalbarchart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);

        YAxis yl = horizontalbarchart.getAxisLeft();
        yl.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yl.setDrawGridLines(false);
        yl.setEnabled(false);
        yl.setAxisMinimum(0f);

        YAxis yr = horizontalbarchart.getAxisRight();
        yr.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);

        // 막대 값
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < 6; i++) {
            yVals1.add(new BarEntry(i, (i+1)*10));
        }

        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "DataSet 1");
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        set1.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(.9f);
        horizontalbarchart.setData(data);
        horizontalbarchart.getLegend().setEnabled(false);


        // 2. 꺾은선형 그래프
        ArrayList<Entry> entry_chart1 = new ArrayList<>(); //데이터를 담을 Arraylist
        ArrayList<Entry> entry_chart2 = new ArrayList<>();

        LineData chartData = new LineData(); // 차트에 담길 데이터

        entry_chart1.add(new Entry(1,1));
        entry_chart1.add(new Entry(2,2));
        entry_chart1.add(new Entry(3,3));
        entry_chart1.add(new Entry(4,4));
        entry_chart1.add(new Entry(5,2));
        entry_chart1.add(new Entry(6,8));

        entry_chart2.add(new Entry(1,2));
        entry_chart2.add(new Entry(2,3));
        entry_chart2.add(new Entry(3,1));
        entry_chart2.add(new Entry(4,4));
        entry_chart2.add(new Entry(5,5));
        entry_chart2.add(new Entry(6,7));

        LineDataSet lineDataSet1 = new LineDataSet(entry_chart1,"LineGraph1");
        LineDataSet lineDataSet2 = new LineDataSet(entry_chart2,"LineGraph2");

        lineDataSet1.setColors(Color.RED);
        lineDataSet2.setColors(Color.BLACK);

        chartData.addDataSet(lineDataSet1);
        chartData.addDataSet(lineDataSet2);

        linechart.setData(chartData);

        linechart.invalidate(); // 차트 업데이트
        linechart.setTouchEnabled(false); // 차트 터치 disable


        // 3.막대 그래프 샘플 데이터
        ArrayList<BarEntry> visitors = new ArrayList<>();
        visitors.add(new BarEntry(1, 420));
        visitors.add(new BarEntry(2, 450));
        visitors.add(new BarEntry(3, 520));
        visitors.add(new BarEntry(4, 620));
        visitors.add(new BarEntry(5, 540));
        visitors.add(new BarEntry(6, 720));
        visitors.add(new BarEntry(7, 920));
        visitors.add(new BarEntry(8, 540));
        visitors.add(new BarEntry(9, 720));
        visitors.add(new BarEntry(10, 920));

        BarDataSet barDataSet = new BarDataSet(visitors, "Visitors");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);


        barchart.setFitBars(true);
        barchart.setData(barData);
        barchart.getDescription().setText("Bar Chart Example");
        barchart.animateY(2000);


    }
}