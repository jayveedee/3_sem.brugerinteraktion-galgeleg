package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class HjaelpMenu extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hjaelp);

        image = findViewById(R.id.imageView);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
        image.startAnimation(aniFade);
    }
}
