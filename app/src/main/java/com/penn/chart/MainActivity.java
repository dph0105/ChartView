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
        points.add(new Point(1,10));
        points.add(new Point(2,19));
        points.add(new Point(3,2));
        points.add(new Point(4,30));
        points.add(new Point(5,15));
        points.add(new Point(6,22));
        points.add(new Point(7,25));
        points.add(new Point(8,50));
        points.add(new Point(9,8));
        points.add(new Point(10,15));
        points.add(new Point(11,32));
        points.add(new Point(12,20));
        points.add(new Point(1,1));
        points.add(new Point(2,18));
        points.add(new Point(3,39));
        lcv.setPoints(points);
    }
}
