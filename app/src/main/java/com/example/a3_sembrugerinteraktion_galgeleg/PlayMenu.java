package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayMenu extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Button> keyboard = new ArrayList<>();
    private Galgelogik gLogik = new Galgelogik();
    private TextView textField;
    private ConstraintLayout conLayout;
    private boolean justFinished = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_menu);
        conLayout = findViewById(R.id.playMenu);

        initializeGame();
    }

    @Override
    public void onClick(View v) {
        boolean gameOver = gLogik.erSpilletSlut();
        Log.d("play", "gameOver = " + gameOver);

        if (!gameOver){
            // Checks which button is being pressed and saves that button
            String bokstav = "";
            for (int i = 0; i < keyboard.size(); i++) {
                if (v.getId() == keyboard.get(i).getId()){
                    keyboard.get(i).setVisibility(View.INVISIBLE);
                    bokstav = keyboard.get(i).getText().toString();

                    Log.d("play","bokstav : " + bokstav);
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

                Log.d("play","frame = " + frameSTR);
                Log.d("play","frameID = " + resID);
            }
        }
        else {
            if (justFinished){
                Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadeout);
                textField.setText(gLogik.getOrdet());
                for (int i = 0; i < keyboard.size(); i++) {
                    if (keyboard.get(i).getVisibility() == View.VISIBLE) {
                        keyboard.get(i).startAnimation(fadeOut);
                        keyboard.get(i).setVisibility(View.INVISIBLE);
                    }
                }
                Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
                Button bReset = findViewById(R.id.bReset);
                bReset.startAnimation(fadeIn);
                bReset.setVisibility(View.VISIBLE);

                justFinished = false;
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
        Log.d("play","textField = " + gLogik.getSynligtOrd());
        Log.d("play","textField = " + gLogik.getOrdet());
        for (int i = 0; i < keyboard.size(); i++) {
            int b = i + 1;
            Log.d("play", "b" + b + " = " + keyboard.get(i).getId());
        }
    }
}
