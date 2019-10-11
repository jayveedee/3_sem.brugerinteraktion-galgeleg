package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HjaelpMenu extends AppCompatActivity implements View.OnClickListener {

    Button bGithub;
    TextView tGithub, tDate;
    ImageView iAboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hjaelp);

        /*
        image = findViewById(R.id.imageView);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
        image.startAnimation(aniFade);
         */

        bGithub = findViewById(R.id.bGithub); bGithub.setOnClickListener(this);
        tGithub = findViewById(R.id.tGithub);

        tDate = findViewById(R.id.tDate); tDate.setText(getDate());

        iAboutMe = findViewById(R.id.iAboutMe); iAboutMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == bGithub) {
            Log.d("hjaelp",tGithub.getText().toString());
            Uri uri = Uri.parse(tGithub.getText().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (v == iAboutMe) {
            Log.d("hjaelp","aboutMe Image");
            Uri uri = Uri.parse("https://pics.me.me/hey-baby-whats-the-droprate-of-that-skirt-27652567.png");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private String getDate() {
        long currentMillis = System.currentTimeMillis();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        Date date = new Date(currentMillis);

        Log.d("hjaelp",  format.format(date));

        String token = format.format(date);
        String[] tokens = token.split(" ");

        StringBuilder splitString = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == null) {
                break;
            }
            splitString.append(tokens[i]).append("\n");
        }

        Log.d("hjaelp",splitString.toString());
        return splitString.toString();
    }


}
