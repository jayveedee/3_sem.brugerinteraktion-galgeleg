package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;

public class StatisticsMenu extends AppCompatActivity implements View.OnClickListener {

    // Variabler

    private Galgelogik gLogik = Galgelogik.getInstance();
    private ArrayList<Integer> antalVundet = gLogik.getAntalSpilVundetTabt();
    private ArrayList<Integer> highscores = gLogik.getHighscores();
    private int antalSpil = gLogik.getAntalSpilSpillet();
    private Runnable run;
    private Button bChangeView;
    private LineGraphSeries series;
    private Handler handler = new Handler();
    private GraphView graph;
    private BarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_menu);

        // Laver en graf ud fra et importeret bibliotek

        graph = findViewById(R.id.graph);
        graph.setTitle("Win / Lose Graph");
        graph.setTitleTextSize(75);

        bChangeView = findViewById(R.id.bChangeView);
        bChangeView.setText("Highscore");
        bChangeView.setOnClickListener(this);

        barChart = findViewById(R.id.barChart);
        barChart.getDescription().setEnabled(false);


        // Logcat

        if (antalVundet.size() > 0) {
            Log.d("stats","antalVundet = " + (gLogik.getAntalSpilVundetTabt().get(antalVundet.size() - 1)));
        }
        Log.d("stats","antalSpil = " + antalSpil);
    }

    // For at opdatere grafen bruges der et Runnable objekt, såsom dataet bliver opdateret efter hvert spil
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

                // Indsætter dataet på viewet som en graf og definerer farver osv.

                graph.addSeries(series);
                series.setColor(Color.GREEN);
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(10);
                series.setThickness(8);

                graph.getViewport().setMinX(0);
                graph.getViewport().setXAxisBoundsManual(true);

                setData(10);
                barChart.setFitBars(false);

                handler.postDelayed(this,200);
            }

            private void setData(int highscoresCount) {
                ArrayList<BarEntry> entryY = new ArrayList<>();

                Collections.sort(highscores);
                Collections.reverse(highscores);

                for (int i = 0; i < highscoresCount; i++) {
                    if (highscores.size() - 1 >= i) {
                        BarEntry entry = new BarEntry(i + 1, highscores.get(i));
                        entryY.add(entry);
                    }
                }
                BarDataSet dataset = new BarDataSet(entryY, "Highscores");
                dataset.setColors(ColorTemplate.MATERIAL_COLORS);
                dataset.setDrawValues(true);
                dataset.setValueTextColor(Color.parseColor("#ffffff"));
                dataset.setValueTextSize(16);
                dataset.setBarBorderWidth(10f);

                BarData data = new BarData(dataset);
                barChart.getAxisLeft().setTextColor(Color.parseColor("#ffffff"));
                barChart.getAxisLeft().setTextSize(16);
                barChart.getXAxis().setTextColor(Color.parseColor("#ffffff"));
                barChart.getXAxis().setTextSize(16);

                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.getXAxis().setCenterAxisLabels(true);
                barChart.getXAxis().setGranularity(1f);
                barChart.getXAxis().setDrawGridLines(false);

                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.getAxisLeft().setGranularity(2);
                barChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);


                barChart.setData(data);
                barChart.getLegend().setEnabled(false);
                barChart.getAxisRight().setEnabled(false);
                barChart.setDrawValueAboveBar(true);
                barChart.invalidate();
                barChart.animate();
            }
        };
        handler.postDelayed(run,10);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {

        if (bChangeView.getText().equals("Highscore")){
            barChart.setVisibility(View.VISIBLE);
            bChangeView.setText("Win / Lose");
        }
        else {
            barChart.setVisibility(View.INVISIBLE);
            bChangeView.setText("Highscore");
        }
    }
}
