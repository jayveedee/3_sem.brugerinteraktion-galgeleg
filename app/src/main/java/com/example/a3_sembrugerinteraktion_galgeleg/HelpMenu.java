package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelpMenu extends AppCompatActivity implements View.OnClickListener {

    // Variabler

    private Button bGithub, bInside;
    private TextView tDate;
    private ImageView iosrsMeme1, iosrsMeme0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Initialiserer det som er på viewet

        iosrsMeme0 = findViewById(R.id.iosrsMeme0); iosrsMeme0.setOnClickListener(this);
        iosrsMeme1 = findViewById(R.id.iosrsMeme1); iosrsMeme1.setOnClickListener(this);
        bGithub = findViewById(R.id.bGithub); bGithub.setOnClickListener(this);
        bInside = findViewById(R.id.bInside); bInside.setOnClickListener(this);
        tDate = findViewById(R.id.tDate);

        // Laver et Runnable objekt til at håndtere klokken
    }

    @Override
    public void onResume() {
        super.onResume();
        final Runnable updateClock = new Runnable() {
            @Override
            public void run() {
                tDate.setText(getDate());
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(updateClock,1);
    }
    @Override
    public void onClick(View v) {

        // Tjekker hvilken knap der bliver brugt

        if (v == bGithub) {
            String url = "https://github.com/jayveedee/3_sem.brugerinteraktion-galgeleg";
            Log.d("help","github link = " + url);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (v == iosrsMeme0){
            String url = "https://i.imgur.com/JFxg2m1.jpg";
            Log.d("help","osrs meme 00 = " + url);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (v == iosrsMeme1) {
            String url = "https://imgur.com/72qSWYQ.jpg";
            Log.d("help","osrs meme 01 = " + url);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (v == bInside){
            String url = "https://www.inside.dtu.dk/en/dtuinside/generelt/telefonbog/person?id=137487&tab=0";
            Log.d("help", "inside link = " + url);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    // Finder datoen ud fra nuværende millisekunder, og laver et data objekt ud af det.

    private String getDate() {
        long currentMillis = System.currentTimeMillis();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        Date date = new Date(currentMillis);

        Log.d("help",  "date format = " + format.format(date));

        String token = format.format(date);
        String[] tokens = token.split(" ");

        StringBuilder splitString = new StringBuilder();
        for (String s : tokens) {
            if (s == null) {
                break;
            }
            splitString.append(s).append("\n");
        }

        Log.d("help", "date string = " + splitString.toString());
        return splitString.toString();
    }


}
