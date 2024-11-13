package com.example.sriganesh.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import com.example.sriganesh.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Chart_Activity2 extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart2);

       PieChart pieChart = findViewById(R.id.barchart2);

        ArrayList<PieEntry> records=new ArrayList<>();
        records.add(new PieEntry(1000,"2021"));
        records.add(new PieEntry(750,"2022"));
        records.add(new PieEntry(500,"2023"));
        records.add(new PieEntry(250,"2024"));
        records.add(new PieEntry(150,"2024"));

        PieDataSet dataSet=new PieDataSet(records,"Pie Chart Report");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(16f);

        PieData pieData=new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(true);
        pieChart.setCenterText("YEARLY\n\nREPORTS");
        pieChart.animate();
    }
}