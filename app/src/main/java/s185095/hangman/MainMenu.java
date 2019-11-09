package s185095.hangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import s185095.hangman.logic.Hangman;
import s185095.hangman.logic.Result;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    private static Hangman logic;
    private String sPKey, sPKeyHS, sPKeyWL, sPKeyG, sPkeyRS;
    private Button bStart;
    private ImageView iAbout, iStats;
    private SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        initializeActivity();
    }

    /** INITIALIZE THE VIEW AND OBJCTS */
    private void initializeActivity() {
        logic = Hangman.getInstance();

        bStart = findViewById(R.id.bStart); bStart.setOnClickListener(this);
        iAbout = findViewById(R.id.iAbout); iAbout.setOnClickListener(this);
        iStats = findViewById(R.id.iStats); iStats.setOnClickListener(this);

        sPKey = "hangman.data"; sPKeyHS = "highscore"; sPKeyWL = "winlose"; sPKeyG = "games"; sPkeyRS = "results";
        sPref = getSharedPreferences(sPKey,MODE_PRIVATE);

        getData();

        new Sheet().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        debugMessages();
    }

    private void debugMessages() {
        Log.d("main", "highscoresList: " + logic.getListOfHighscores());
        Log.d("main", "winLossesList: " + logic.getListOfWinsLosses());
        Log.d("main", "gamesPlayed: " + logic.getGamesPlayed());
        Log.d("main", "Results: " + logic.getListOfResults());
    }

    /** THREE THINGS TO CLICK ON AND THREE DIFFERENT OUTCOMES */
    @Override
    public void onClick(View v) {
        if (v == bStart){
            startActivity(new Intent(MainMenu.this, PlayMenu.class));
            logic.restartGame();
            logic.setCurrScore(0);
        }
        if (v == iAbout){
            startActivity(new Intent(MainMenu.this, AboutMenu.class));
        }
        if (v == iStats){
            startActivity(new Intent(MainMenu.this, StatsMenu.class));
        }
    }

    /** GETS THE DATA FROM A PREVIOUS SESSION IF IT EXISTS */
    private void getSharedData(String key) {
        Gson gson = new Gson();

        if (key.equals(sPKeyHS) || key.equals(sPKeyWL)){
            String json = sPref.getString(key,null);
            Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
            ArrayList<Integer> sharedData = gson.fromJson(json,type);

            if (key.equals(sPKeyHS)){
                logic.setListOfHighscores(sharedData);
                if (sharedData == null){
                    ArrayList<Integer> emptyList = new ArrayList<>();
                    logic.setListOfHighscores(emptyList);
                }
            }
            if (key.equals(sPKeyWL)){
                logic.setListOfWinsLosses(sharedData);
                if (sharedData == null){
                    ArrayList<Integer> emptyList = new ArrayList<>();
                    emptyList.add(0);
                    logic.setListOfWinsLosses(emptyList);
                }
            }
        }
        if (key.equals(sPKeyG)){
            String json = sPref.getString(key,"0");
            int sharedData = Integer.parseInt(json);
            logic.setGamesPlayed(sharedData);
        }
        if (key.equals(sPkeyRS)){
            String json = sPref.getString(key,null);
            Type type = new TypeToken<ArrayList<Result>>(){}.getType();
            ArrayList<Result> sharedDate = gson.fromJson(json, type);
            logic.setListOfResults(sharedDate);
            if (sharedDate == null){
                ArrayList<Result> emptyList = new ArrayList<>();
                logic.setListOfResults(emptyList);
            }

        }
    }

    private void getData() {
        getSharedData(sPKeyHS);
        getSharedData(sPKeyWL);
        getSharedData(sPKeyG);
        getSharedData(sPkeyRS);
    }

    /** CREATES A PARALLEL THREAD TO THE MAIN ONE, TO GET THE SHEET FROM A URL */
    private static class Sheet extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                logic.getWordsFromSheets();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
