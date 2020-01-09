package s185095.hangman;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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

    /**
     * This class contains some statistics over the games that have been played.
     * Nothing too fancy, just some implementations of jjoe64's Graphview and mikephil's BarChart
     */

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

    /** INITIALIZES THE LINE GRAPH WHERE WIN / LOSE IS SHOWN */
    private void initializeLineGraph() {

        //Finder grafen og sætter nogle værdier ind
        gLineGraph = findViewById(R.id.gLineGraph);
        gLineGraph.setTitle("Words Guessed Graph");
        gLineGraph.setTitleTextSize(75);

        //Laver en serie, som er linjepunkterne
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        double x = 0, y = 0;
        for (int i = 0; i <= gamesPlayed; i++) {
            if (winLossesList.size() - 1 >= i){
                y = winLossesList.get(i);
                x = i;
            }

            series.appendData(new DataPoint(x,y),true,70);

            //Adder punkterne og laver den linje som kommer til at være imellem punkterne grøn
            gLineGraph.addSeries(series);
            series.setColor(Color.GREEN);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setThickness(8);

            //Nogle smådetaljer over hvor grafen skal begynde fra og tekst
            gLineGraph.getViewport().setMinX(0);
            gLineGraph.getViewport().setXAxisBoundsManual(true);

            GridLabelRenderer gridLabel = gLineGraph.getGridLabelRenderer();
            gridLabel.setHorizontalAxisTitle("Words Guessed");
            gridLabel.setVerticalAxisTitle("Wrong / Right");
        }
    }

    /** INITIALIZES THE HIGHSCORE BARCHART GRAPH */
    private void initializeBarChart() {
        //Finder barChartet
        gBarChart = findViewById(R.id.gBarChart);
        gBarChart.getDescription().setEnabled(false);

        ArrayList<BarEntry> entriesForY = new ArrayList<>();

        //Sorterer og bruger reverse listen såsom at de højeste scores er øverst
        Collections.sort(highscoresList);
        Collections.reverse(highscoresList);

        //Adder de highscores som entries, indtil man kommer op til 10, hvor highscore listen skal slutte
        for (int i = 0; i < 10; i++) {
            if (highscoresList.size() - 1 >= i){
                BarEntry newEntry = new BarEntry(i + 1, highscoresList.get(i));
                entriesForY.add(newEntry);
                Log.d("stats", "barEntry: " + highscoresList.get(i));

            }
        }
        Log.d("stats", "test " + highscoresList.size());

        //Masser af smådetaljer over hvordan grafen skal se ud. Tekst størrelse, farver osv.
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

        //Endelig indsætter dataet på grafen og man kan se det.
        gBarChart.setData(data);
        gBarChart.getLegend().setEnabled(false);
        gBarChart.getAxisRight().setEnabled(false);
        gBarChart.setDrawValueAboveBar(true);
        gBarChart.setFitBars(false);
        gBarChart.invalidate();
        gBarChart.animate();
    }

    /** INITIALIZES THE VIEW AND ALL VARIABLES NEEDED */
    private void initializeActivity() {
        //Henter singleton objektet og laver nogle lokale variabler udfra dataet den indeholder
        logic = Hangman.getInstance();
        winLossesList = logic.getListOfWinsLosses();
        highscoresList = logic.getListOfHighscores();
        gamesPlayed = logic.getGamesPlayed();

        //Initialiserer knapperne
        bChangeView = findViewById(R.id.bChangeView); bChangeView.setOnClickListener(this);
        bResetToDefault = findViewById(R.id.bResetToDefaults); bResetToDefault.setOnClickListener(this);

        //Kalder på metoderne for at initialisere graferne
        initializeLineGraph();
        initializeBarChart();

        //Sharedpreference nøglen, for at hente lokalt data
        sPKey = "hangman.data";
        sPref = getSharedPreferences(sPKey,MODE_PRIVATE);
        sEdit = sPref.edit();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
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

    /** 2 BUTTONS TO CLICK ON, ONE RESETS THE DATA, THE OTHER CHANGES GRAPH */
    @Override
    public void onClick(View v) {
        //Skifter imellem highscore og win lose grafen
        if (v == bChangeView){
            if (bChangeView.getText().equals("Highscores")){
                gBarChart.setVisibility(View.VISIBLE);
                bChangeView.setText("Words Guessed");
            }
            else {
                gBarChart.setVisibility(View.INVISIBLE);
                bChangeView.setText("Highscores");
            }
        }
        //Sletter al lokal data og også sltter dataet fra singleton objektet
        if (v == bResetToDefault){
            logic.resetGame();
            sEdit.clear();
            sEdit.commit();
        }
    }
}
