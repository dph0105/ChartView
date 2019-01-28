package com.penn.chart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.penn.chartview.LineChartView;
import com.penn.chartview.Point;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LineChartView lcv = findViewById(R.id.lcv);
        List<Point> points = new ArrayList<>();
        points.add(new Point(1,0));
//        points.add(new Point(2,0));
//        points.add(new Point(3,0));
//        points.add(new Point(4,0));
//        points.add(new Point(5,0));
//        points.add(new Point(6,0));
//        points.add(new Point(7,0));
//        points.add(new Point(8,0));
//        points.add(new Point(9,0));
//        points.add(new Point(10,0));
//        points.add(new Point(11,0));
//        points.add(new Point(12,0));
        lcv.setPoints(points);
        lcv.setSinglePointSize(10f);
    }
}
