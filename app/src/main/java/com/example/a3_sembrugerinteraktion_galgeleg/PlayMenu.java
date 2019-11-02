package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class PlayMenu extends AppCompatActivity implements View.OnClickListener {

    // Variabler

    private Galgelogik gLogik = Galgelogik.getInstance();
    private ArrayList<Button> keyboard = new ArrayList<>();
    private String frameSTR = "";
    private String bokstav = "";
    private TextView textField, tScore;
    private ImageView iGameOver;
    private Button bReset;
    private ConstraintLayout conLayout;
    private boolean gameOver;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_menu);

        // Initialiserer alle knapper, textviews osv.

        initializeGame();



        // Logcat
        Log.d("play","###############################");
        Log.d("play","ButtonPressed = " + bokstav);
        Log.d("play","CurrentFrame = " + frameSTR);
        Log.d("play","GameOver = " + gameOver);
        Log.d("play","textField = " + gLogik.getSynligtOrd());
        Log.d("play","textField = " + gLogik.getOrdet());
        Log.d("play", "score = " + gLogik.getCurrentScore());
        /*
        for (int i = 0; i < keyboard.size(); i++) {
            int b = i + 1;
            Log.d("play", "b" + b + " = " + keyboard.get(i).getId());
        }
        */
    }

    @Override
    public void onClick(View v) {

        // Tjekker om spillet er slut, hvor mange gange spillet har været i gang og hvor mange gange spilleren vandt

        gameOver = gLogik.erSpilletSlut();
        int antalSpilSpillet = gLogik.getAntalSpilSpillet();
        int antalSpilVundetTabt;
        if (gLogik.getAntalSpilVundetTabt() != null && gLogik.getAntalSpilVundetTabt().size() > 0) {
            antalSpilVundetTabt = gLogik.getAntalSpilVundetTabt().get(gLogik.getAntalSpilVundetTabt().size() - 1);
        }
        else {
            antalSpilVundetTabt = 0;
        }

        // Kører i denne if statement mens spillet er i gang

        if (!gameOver){
            for (int i = 0; i < keyboard.size(); i++) {

                // Når forloopen finder bokstaven som blev trykket, vil den derefter lave den usynlig og tjekke om den bokstav passer i det gættede ord

                if (v.getId() == keyboard.get(i).getId()){

                    keyboard.get(i).setVisibility(View.INVISIBLE);
                    bokstav = keyboard.get(i).getText().toString();

                    gLogik.gætBogstav(bokstav);
                    textField.setText(gLogik.getSynligtOrd());

                    gLogik.updateScore();
                    tScore.setText(Integer.toString(gLogik.getCurrentScore()));

                    // Hvis det var den forkerte bokstav, opdateres baggrunden

                    if (!gLogik.erSidsteBogstavKorrekt()){

                        int frameNR = gLogik.getAntalForkerteBogstaver();
                        frameSTR = "frame0" + frameNR;
                        int resID = getResources().getIdentifier(frameSTR,"drawable",getPackageName());
                        conLayout.setBackground(getDrawable(resID));
                    }
                }
            }
        }
        else {

            // Hvis spillet er slut og reset knappen bliver trykket ændres baggrunden og alle knapper, text og galgelogik bliver nulstillet

            if (v == bReset){

                conLayout.setBackground(getDrawable(R.drawable.frame00));
                if (gLogik.erSpilletTabt()){
                    if (gLogik.getHighscores() == null){
                        ArrayList<Integer> highscores = new ArrayList<>();
                        gLogik.setHighscores(highscores);
                    }
                    gLogik.getHighscores().add(gLogik.getCurrentScore());
                    saveDataLocally(gLogik.getHighscores(),"highscore");
                    gLogik.setCurrentScore(0);
                }
                gLogik.nulstil();
                tScore.setText(Integer.toString(gLogik.getCurrentScore()));

                textField.setText(gLogik.getSynligtOrd());
                textField.setTextColor(Color.parseColor("#FF0000"));

                for (int i = 0; i < keyboard.size(); i++) {
                    keyboard.get(i).setVisibility(View.VISIBLE);
                    keyboard.get(i).setClickable(true);
                }

                bReset.setVisibility(View.INVISIBLE);
                iGameOver.setVisibility(View.INVISIBLE);

                textField.setTextColor(Color.parseColor("#FF9800"));
            }
        }

        // Når spillet slutter for første gang, vil der ske en animation og nogle ændringer der bliver lavet i metoden nedenfor

        if (gLogik.getAntalForkerteBogstaver() >= 6 || gLogik.erSpilletVundet()) checkIfGameOver(antalSpilVundetTabt, antalSpilSpillet);
    }

    private void saveDataLocally(ArrayList<Integer> arrList, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(arrList);
        mEdit.putString(key, json);
        mEdit.apply();
    }
    private void saveDataLocally(int number, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(number);
        mEdit.putString(key, json);
        mEdit.apply();
    }

    private void checkIfGameOver(int antalSpilVundetTabt, int antalSpilSpillet) {

        //Definerer animationen

        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

        textField.setText(gLogik.getOrdet());

        // Laver knapperne usynlige og unclickable

        for (int j = 0; j < keyboard.size(); j++) {
            if (keyboard.get(j).getVisibility() == View.VISIBLE) {
                    keyboard.get(j).startAnimation(fadeOut);
                    keyboard.get(j).setVisibility(View.INVISIBLE);
                    keyboard.get(j).setClickable(false);
            }
        }

        bReset.startAnimation(fadeIn);
        bReset.setVisibility(View.VISIBLE);

        // Alt efter om spilleren vandt eller tabte, vil farven og baggrunden ændres i de her to if else statements

        if (gLogik.erSpilletVundet()){

            textField.setTextColor(Color.parseColor("#8BC34A"));

            iGameOver.startAnimation(fadeIn);
            iGameOver.setVisibility(View.VISIBLE);
            iGameOver.setBackground(getDrawable(R.drawable.excitedface_icon));

            // Variablerne bliver opdateret, såsom man kan senere hen bruge gem i statistik aktiviteten

            antalSpilVundetTabt++;
            gLogik.getAntalSpilVundetTabt().add(antalSpilVundetTabt);
            saveDataLocally(gLogik.getAntalSpilVundetTabt(),"spilvundettabt");

            antalSpilSpillet++;
            gLogik.setAntalSpilSpillet(antalSpilSpillet);
            saveDataLocally(gLogik.getAntalSpilSpillet(),"spilspillet");
        }
        else {

            textField.setTextColor(Color.parseColor("#FF0000"));

            iGameOver.startAnimation(fadeIn);
            iGameOver.setVisibility(View.VISIBLE);
            iGameOver.setBackground(getDrawable(R.drawable.deadface_icon));

            // Variablerne bliver opdateret, såsom man kan senere hen bruge gem i statistik aktiviteten

            antalSpilVundetTabt--;
            gLogik.getAntalSpilVundetTabt().add(antalSpilVundetTabt);
            saveDataLocally(gLogik.getAntalSpilVundetTabt(),"spilvundettabt");

            antalSpilSpillet++;
            gLogik.setAntalSpilSpillet(antalSpilSpillet);
            saveDataLocally(gLogik.getAntalSpilSpillet(),"spilspillet");
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void initializeGame() {

        // Initialiserer knapper
        int buttons = 27;
        for (int i = 1; i < buttons; i++) {
            String bID = "b" + i;
            int resID = getResources().getIdentifier(bID,"id",getPackageName());

            Button b = findViewById(resID);
            keyboard.add(b);

            b.setOnClickListener(this);
        }

        // Initialiserer textviewet
        textField = findViewById(R.id.tBokstaver);
        textField.setText(gLogik.getSynligtOrd());

        // Initialiserer reset knappen
        bReset = findViewById(R.id.bReset);
        bReset.setVisibility(View.INVISIBLE);
        bReset.setOnClickListener(this);

        // Initialiserer constraint layoutet
        conLayout = findViewById(R.id.playMenu);

        // Initialiserer gameover billedet
        iGameOver = findViewById(R.id.iGameOver);

        // Initialiserer highscore updater
        tScore = findViewById(R.id.tScore);
        tScore.setText(Integer.toString(gLogik.getCurrentScore()));

        // Initialiserer sharedpreference, som gemmer data
        mPref = getSharedPreferences("galgeleg.data",MODE_PRIVATE);
        mEdit = mPref.edit();


        if (gLogik.getAntalSpilVundetTabt() == null){
            ArrayList spilvundet = new ArrayList();
            gLogik.setAntalSpilVundetTabt(spilvundet);
        }
    }
}
