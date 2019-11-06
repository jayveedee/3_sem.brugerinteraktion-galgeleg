package s185095.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import s185095.hangman.logic.Hangman;

public class StatsMenu extends AppCompatActivity implements View.OnClickListener {

    private Hangman logic;
    private String sPKey;
    private Button bChangeView, bResetToDefault;
    private GraphView gLineGraph;
    private BarChart gBarChart;
    private SharedPreferences sPref;
    private SharedPreferences.Editor sEdit;

    private List<Integer> winLossesList;
    private List<Integer> highscoresList;
    private int gamesPlayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statsmenu);
        initializeActivity();
    }

    private void initializeLineGraph() {

        gLineGraph = findViewById(R.id.gLineGraph);
        gLineGraph.setTitle("Win / Lose Graph");
        gLineGraph.setTitleTextSize(75);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        double x = 0, y = 0;
        for (int i = 0; i <= gamesPlayed; i++) {
            if (winLossesList.size() - 1 >= i){
                y = winLossesList.get(i);
                x = i;
            }

            series.appendData(new DataPoint(x,y),true,70);

            gLineGraph.addSeries(series);
            series.setColor(Color.GREEN);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setThickness(8);

            gLineGraph.getViewport().setMinX(0);
            gLineGraph.getViewport().setXAxisBoundsManual(true);

            GridLabelRenderer gridLabel = gLineGraph.getGridLabelRenderer();
            gridLabel.setHorizontalAxisTitle("Games Played");
            gridLabel.setVerticalAxisTitle("Games Won / Lost");
        }
    }

    private void initializeBarChart() {

        gBarChart = findViewById(R.id.gBarChart);
        gBarChart.getDescription().setEnabled(false);

        ArrayList<BarEntry> entriesForY = new ArrayList<>();

        Collections.sort(highscoresList);
        Collections.reverse(highscoresList);

        for (int i = 0; i < 10; i++) {
            if (highscoresList.size() - 1 >= i){
                BarEntry newEntry = new BarEntry(i + 1, highscoresList.get(i));
                entriesForY.add(newEntry);
                Log.d("stats", "barEntry: " + highscoresList.get(i));

            }
        }
        Log.d("stats", "test " + highscoresList.size());

        BarDataSet set = new BarDataSet(entriesForY, "Highscores");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);
        set.setValueTextColor(Color.parseColor("#ffffff"));
        set.setValueTextSize(16);
        set.setBarBorderWidth(10f);

        BarData data = new BarData(set);
        gBarChart.getAxisLeft().setTextColor(Color.parseColor("#ffffff"));
        gBarChart.getAxisLeft().setTextSize(16);
        gBarChart.getXAxis().setTextColor(Color.parseColor("#ffffff"));
        gBarChart.getXAxis().setTextSize(16);

        gBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        gBarChart.getXAxis().setCenterAxisLabels(true);
        gBarChart.getXAxis().setGranularity(1f);
        gBarChart.getXAxis().setDrawGridLines(false);

        gBarChart.getAxisLeft().setDrawGridLines(false);
        gBarChart.getAxisLeft().setGranularity(2);
        gBarChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);


        gBarChart.setData(data);
        gBarChart.getLegend().setEnabled(false);
        gBarChart.getAxisRight().setEnabled(false);
        gBarChart.setDrawValueAboveBar(true);
        gBarChart.setFitBars(false);
        gBarChart.invalidate();
        gBarChart.animate();
    }

    private void initializeActivity() {
        logic = Hangman.getInstance();
        winLossesList = logic.getListOfWinsLosses();
        highscoresList = logic.getListOfHighscores();
        gamesPlayed = logic.getGamesPlayed();

        bChangeView = findViewById(R.id.bChangeView); bChangeView.setOnClickListener(this);
        bResetToDefault = findViewById(R.id.bResetToDefaults); bResetToDefault.setOnClickListener(this);

        initializeLineGraph();
        initializeBarChart();

        sPKey = "hangman.data";
        sPref = getSharedPreferences(sPKey,MODE_PRIVATE);
        sEdit = sPref.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeLineGraph();
        initializeBarChart();
        debugMessages();
    }

    private void debugMessages() {
        Log.d("stats", "highscoresList: " + highscoresList);
        Log.d("stats", "winLossesList: " + winLossesList);
        Log.d("stats", "gamesPlayed: " + gamesPlayed);
    }

    @Override
    public void onClick(View v) {
        if (v == bChangeView){
            if (bChangeView.getText().equals("Highscores")){
                gBarChart.setVisibility(View.VISIBLE);
                bChangeView.setText("Win / Lose");
            }
            else {
                gBarChart.setVisibility(View.INVISIBLE);
                bChangeView.setText("Highscores");
            }
        }
        if (v == bResetToDefault){
            logic.resetGame();
            sEdit.clear();
            sEdit.commit();
        }
    }
}
