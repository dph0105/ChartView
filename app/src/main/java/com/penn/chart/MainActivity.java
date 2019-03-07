package com.penn.chart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.penn.chartview.LineChartView;
import com.penn.chartview.Point;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LineChartView lcv = findViewById(R.id.lcv);
        List<Point> points = new ArrayList<>();
        points.add(new Point(1,5));
        points.add(new Point(2,10.51f));
        points.add(new Point(3,5.21f));
        points.add(new Point(4,0));
        points.add(new Point(5,0));
        points.add(new Point(6,0));
        points.add(new Point(7,0));
        points.add(new Point(8,0));
        points.add(new Point(9,0));
        points.add(new Point(10,0));
        points.add(new Point(11,0));
        points.add(new Point(12,0));
        lcv.setPoints(points);
        lcv.setSinglePointSize(10f);
        lcv.setYAxisUnit("次");
        lcv.setYValueFormatter(new LineChartView.YValueFormatter() {
            @Override
            public String getFormatterValue(float value) {
                return String.valueOf((int)value);
            }
        });
    }
}
