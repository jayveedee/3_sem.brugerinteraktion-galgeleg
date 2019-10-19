package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelpMenu extends AppCompatActivity implements View.OnClickListener {

    Button bGithub;
    TextView tGithub, tDate;
    ImageView iosrsMeme1, iosrsMeme0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        iosrsMeme0 = findViewById(R.id.iosrsMeme0); iosrsMeme0.setOnClickListener(this);
        iosrsMeme1 = findViewById(R.id.iosrsMeme1); iosrsMeme1.setOnClickListener(this);
        bGithub = findViewById(R.id.bGithub); bGithub.setOnClickListener(this);
        tDate = findViewById(R.id.tDate); tDate.setText(getDate());

    }

    @Override
    public void onClick(View v) {

        if (v == bGithub) {
            Log.d("help",tGithub.getText().toString());
            Uri uri = Uri.parse(tGithub.getText().toString());
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
    }

    private String getDate() {
        long currentMillis = System.currentTimeMillis();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
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
