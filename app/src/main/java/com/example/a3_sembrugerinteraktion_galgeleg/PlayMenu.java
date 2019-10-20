package com.example.a3_sembrugerinteraktion_galgeleg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayMenu extends AppCompatActivity implements View.OnClickListener {

    private Galgelogik gLogik = Galgelogik.getInstance();
    private ArrayList<Button> keyboard = new ArrayList<>();
    private String frameSTR = "";
    private TextView textField;
    private ImageView iGameOver;
    private Button bReset;
    private ConstraintLayout conLayout;
    private int antalSpilVundetTabt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_menu);
        initializeGame();
    }

    @Override
    public void onClick(View v) {

        // Tjekker om spillet er færdigt eller ej, om det er, vil man ikke kunne trykke på de sædvanlige knapper

        boolean gameOver = gLogik.erSpilletSlut();
        int antalSpilSpillet = gLogik.getAntalSpilSpillet();
        if (gLogik.getAntalSpilVundetTabt().size() > 0) {
            antalSpilVundetTabt = gLogik.getAntalSpilVundetTabt().get(gLogik.getAntalSpilVundetTabt().size() - 1);
        }
        else {
            antalSpilVundetTabt = 0;
        }

        String bokstav = "";

        if (!gameOver){

            // Tjekker hver knap, for at se hvilken knap der er blevet trykket

            for (int i = 0; i < keyboard.size(); i++) {

                // Om knappen er fundet, vil man gå ind i denne if statement for at se hvilken bokstav det er

                if (v.getId() == keyboard.get(i).getId()){

                    // Finder bokstaven

                    keyboard.get(i).setVisibility(View.INVISIBLE);
                    bokstav = keyboard.get(i).getText().toString();

                    // Bruger galgelogikken for at gætte om bokstaven er korrekt, derefter bliver textviewet opdateret

                    gLogik.gætBogstav(bokstav);
                    textField.setText(gLogik.getSynligtOrd());

                    // Om bokstaven ikke matchede ordet, vil man gå ind i denne if statement for at ændre baggrunden

                    if (!gLogik.erSidsteBogstavKorrekt()){

                        // Finder id'et af baggrunden og ændrer constraint layoutets baggrund

                        int frameNR = gLogik.getAntalForkerteBogstaver();
                        frameSTR = "frame0" + frameNR;
                        int resID = getResources().getIdentifier(frameSTR,"drawable",getPackageName());
                        conLayout.setBackground(getDrawable(resID));
                    }
                }
            }
        }
        // Om spillet er slut, er der kun en knap der kan trykkest og det er reset knappen

        else {

            // Om knappen bliver trykket, vil spillet blive resettet og alle variabler bliver nulstillet

            if (v == bReset){
                conLayout.setBackground(getDrawable(R.drawable.frame00));
                gLogik.nulstil();
                textField.setText(gLogik.getSynligtOrd());
                textField.setTextColor(Color.parseColor("#FF0000"));
                for (int i = 0; i < keyboard.size(); i++) {
                    keyboard.get(i).setVisibility(View.VISIBLE);
                }
                bReset.setVisibility(View.INVISIBLE);
                iGameOver.setVisibility(View.INVISIBLE);
                textField.setTextColor(Color.parseColor("#FF9800"));
                for (int i = 0; i < keyboard.size(); i++) {
                    keyboard.get(i).setClickable(true);
                }
            }
        }
        checkIfGameOver(antalSpilVundetTabt, antalSpilSpillet);

        // Logcat
        Log.d("play","###############################");
        Log.d("play","ButtonPressed = " + bokstav);
        Log.d("play","CurrentFrame = " + frameSTR);
        Log.d("play","GameOver = " + gameOver);
        Log.d("play","textField = " + gLogik.getSynligtOrd());
        Log.d("play","textField = " + gLogik.getOrdet());
        /*
        for (int i = 0; i < keyboard.size(); i++) {
            int b = i + 1;
            Log.d("play", "b" + b + " = " + keyboard.get(i).getId());
        }
        */
    }

    private void checkIfGameOver(int antalSpilVundetTabt, int antalSpilSpillet) {
        if (gLogik.getAntalForkerteBogstaver() >= 6 || gLogik.erSpilletVundet()){

            // Viser frem det ord der skulle gættest og laver en animation for at gemme knapperne og derefter vise frem reset knappen

            textField.setText(gLogik.getOrdet());
            Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
            for (int j = 0; j < keyboard.size(); j++) {
                if (keyboard.get(j).getVisibility() == View.VISIBLE) {
                    keyboard.get(j).startAnimation(fadeOut);
                    keyboard.get(j).setVisibility(View.INVISIBLE);
                    keyboard.get(j).setClickable(false);
                }
            }
            Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
            bReset.startAnimation(fadeIn);
            bReset.setVisibility(View.VISIBLE);

            // Tjekker om spillet er vundet eller ej og skifter farven af teksten af textviewet baseret på det

            if (gLogik.erSpilletVundet()){
                textField.setTextColor(Color.parseColor("#8BC34A"));
                iGameOver.startAnimation(fadeIn);
                iGameOver.setVisibility(View.VISIBLE);
                iGameOver.setBackground(getDrawable(R.drawable.excitedface_icon));
                antalSpilVundetTabt++;
                gLogik.getAntalSpilVundetTabt().add(antalSpilVundetTabt);
                Log.d("play", "antalSpilVundet = " + antalSpilVundetTabt);
                antalSpilSpillet++;
                gLogik.setAntalSpilSpillet(antalSpilSpillet);
            }
            else {
                textField.setTextColor(Color.parseColor("#FF0000"));
                iGameOver.startAnimation(fadeIn);
                iGameOver.setVisibility(View.VISIBLE);
                iGameOver.setBackground(getDrawable(R.drawable.deadface_icon));
                antalSpilVundetTabt--;
                gLogik.getAntalSpilVundetTabt().add(antalSpilVundetTabt);
                Log.d("play", "antalSpilVundet = " + antalSpilVundetTabt);
                antalSpilSpillet++;
                gLogik.setAntalSpilSpillet(antalSpilSpillet);
            }
        }
    }

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
    }
}
