package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class StatisticsMenu extends AppCompatActivity {

    private Galgelogik gLogik = Galgelogik.getInstance();
    private ArrayList<Integer> antalVundet = gLogik.getAntalSpilVundetTabt();
    private int antalSpil = gLogik.getAntalSpilSpillet();
    private Runnable run;
    private LineGraphSeries series;
    private Handler handler = new Handler();
    private GraphView graph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_menu);


        graph = findViewById(R.id.graph);
        graph.setTitle("Win / Lose Graph");
        graph.setTitleTextSize(75);



        // Logcat

        if (antalVundet.size() > 0) {
            Log.d("stats","antalVundet = " + (gLogik.getAntalSpilVundetTabt().get(antalVundet.size() - 1)));
        }
        Log.d("stats","antalSpil = " + antalSpil);
    }

    @Override
    public void onResume(){
        super.onResume();
        run = new Runnable() {
            @Override
            public void run() {
                series = new LineGraphSeries();
                double x, y;
                for (int i = 0; i <= antalSpil; i++) {

                    y = antalVundet.get(i);
                    x = i;

                    series.appendData(new DataPoint(x,y),true,40);
                }
                graph.addSeries(series);
                series.setTitle("Random Curve 1");
                series.setColor(Color.GREEN);
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(10);
                series.setThickness(8);

                graph.getViewport().setMinX(0);
                graph.getViewport().setXAxisBoundsManual(true);

                handler.postDelayed(this,200);
            }
        };
        handler.postDelayed(run,10);
    }
}
