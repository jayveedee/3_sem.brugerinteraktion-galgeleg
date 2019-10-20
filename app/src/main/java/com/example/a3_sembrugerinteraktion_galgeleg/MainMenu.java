package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainMenu extends AppCompatActivity {

    Button bStart;
    ImageView iHjaelp;
    Galgelogik gLogik = Galgelogik.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bStart = findViewById(R.id.bStart); bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, PlayMenu.class));
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
                startActivity(new Intent(MainMenu.this, StatisticsMenu.class));
            }
        });


        new Regneark().execute();
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
