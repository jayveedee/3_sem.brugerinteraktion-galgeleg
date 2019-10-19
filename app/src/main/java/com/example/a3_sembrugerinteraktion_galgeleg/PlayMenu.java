package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayMenu extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Button> keyboard = new ArrayList<>();
    private Galgelogik gLogik = new Galgelogik();
    private TextView textField;
    private ConstraintLayout conLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_menu);
        conLayout = findViewById(R.id.playMenu);
        createStatisticView();

        initializeGame();
    }

    @Override
    public void onClick(View v) {
        boolean gameOver = gLogik.erSpilletSlut();
        Log.d("PlayMenu", "gameOver = " + gameOver);

        if (!gameOver){
            // Checks which button is being pressed and saves that button
            String bokstav = "";
            for (int i = 0; i < keyboard.size(); i++) {
                if (v.getId() == keyboard.get(i).getId()){
                    keyboard.get(i).setVisibility(View.INVISIBLE);
                    bokstav = keyboard.get(i).getText().toString();

                    Log.d("PlayMenu","bokstav : " + bokstav);
                }
            }

            // Calls the logic class and passes the saved letter. If it's correct, updates textView
            gLogik.gætBogstav(bokstav);
            if (gLogik.erSidsteBogstavKorrekt()){
                textField.setText(gLogik.getSynligtOrd());

            }
            else {
                int frameNR = gLogik.getAntalForkerteBogstaver();
                String frameSTR = "frame0" + frameNR;
                int resID = getResources().getIdentifier(frameSTR,"drawable",getPackageName());
                conLayout.setBackground(getDrawable(resID));

                Log.d("PlayMenu","frame = " + frameSTR);
                Log.d("PlayMenu","frameID = " + resID);
            }
        }
    }

    private void initializeGame() {
        // Initialize buttons
        int buttons = 27;
        for (int i = 1; i < buttons; i++) {
            String bID = "b" + i;
            int resID = getResources().getIdentifier(bID,"id",getPackageName());

            Button b = findViewById(resID);
            keyboard.add(b);

            b.setOnClickListener(this);
        }

        // Initialize textfield
        textField = findViewById(R.id.tBokstaver);
        textField.setText(gLogik.getSynligtOrd());


        // Logcat
        Log.d("PlayMenu","textField = " + gLogik.getSynligtOrd());
        Log.d("PlayMenu","textField = " + gLogik.getOrdet());
        for (int i = 0; i < keyboard.size(); i++) {
            int b = i + 1;
            Log.d("PlayMenu", "b" + b + " = " + keyboard.get(i));
        }
    }

    private void createStatisticView() {
        ImageView iStats = findViewById(R.id.iStats);
        iStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayMenu.this, StatisticsMenu.class));
            }
        });
    }
}
