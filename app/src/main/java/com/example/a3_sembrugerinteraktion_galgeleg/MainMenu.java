package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    // Variabler

    Button bStart;
    ImageView iHjaelp;
    Galgelogik gLogik = Galgelogik.getInstance();
    public SharedPreferences mPref;
    public SharedPreferences.Editor mEdit;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPref = getSharedPreferences("galgeleg.data",MODE_PRIVATE);
        mEdit = mPref.edit();

        // Thjekker hvilken knap der bliver brugt

        bStart = findViewById(R.id.bStart); bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, PlayMenu.class));
                gLogik.nulstil();
                gLogik.setCurrentScore(0);
            }
        });

        iHjaelp = findViewById(R.id.iHelp); iHjaelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, HelpMenu.class));
            }
        });

        ImageView iStats = findViewById(R.id.iStats);
        iStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocalData1("highscore");
                getLocalData2("spilvundettabt");
                getLocalData3("spilspillet");
                startActivity(new Intent(MainMenu.this, StatisticsMenu.class));
            }
        });

        // For at håndtere en fejl, bliver AsyncTask brugt til at hente regnearket fra nettet

        new Regneark().execute();
    }

    private void getLocalData1(String key) {
        Gson gson = new Gson();
        String json = mPref.getString(key,null);
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        ArrayList<Integer> savedData = gson.fromJson(json,type);
        gLogik.setHighscores(savedData);
        if (savedData == null){
            savedData = new ArrayList<>();
            gLogik.setHighscores(savedData);
        }
    }
    private void getLocalData2(String key) {
        Gson gson = new Gson();
        String json = mPref.getString(key,null);
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        ArrayList<Integer> savedData = gson.fromJson(json,type);
        gLogik.setAntalSpilVundetTabt(savedData);
        if (savedData == null){
            savedData = new ArrayList<>();
            gLogik.setAntalSpilVundetTabt(savedData);
        }
    }
    private void getLocalData3(String key) {
        String json = mPref.getString(key,"0");
        int savedDate = Integer.parseInt(json);
        gLogik.setAntalSpilSpillet(savedDate);
    }

    private class Regneark extends AsyncTask<Void, Void, Void> {
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params){
            try {
                gLogik.hentOrdFraRegneark("1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
