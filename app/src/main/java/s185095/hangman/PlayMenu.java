package s185095.hangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import s185095.hangman.logic.Hangman;
import s185095.hangman.logic.Result;

public class PlayMenu extends AppCompatActivity implements View.OnClickListener {

    /** THE MAIN CLASS WHERE THE GAME IS PLAYED ON. THIS CLASS CONTROLS WHAT HAPPENS WHEN EACH BUTTON IS PRESSED
     * AND WHAT GETS UPDATED IN THE LOGIC CLASS BECAUSE OF THE BUTTONS PRESSED.
     * ALSO CONTAINS SOME ANIMATIONS; SOUND EFFECTS AND DATA SAVING FUNCTIONALITY LIKE SHAREDPREFERENCES*/

    private static Hangman logic;
    private TextView tWord, tScore;
    private ImageView iGameOver;
    private Button bRestart, bResults, bChallenge;
    private List<Button> keyboard;
    private ConstraintLayout conLayout;
    private Animation fadeIn, fadeOut;
    private SharedPreferences sPref;
    private SharedPreferences.Editor sEdit;
    private String sPKey, sPKeyHS, sPKeyWL, sPKeyG, sPKeyRS;
    private int gamesPlayed, winLose;
    private boolean resultActivityPlaying;
    private MediaPlayer loseSound, winSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmenu);
        initializeActivity();
    }

    /** INITIALIZES EVERYTHING ON THE ACTIVITY */
    private void initializeActivity() {

        //Get hangman instance
        logic = Hangman.getInstance();

        //Initialize textviews
        tWord = findViewById(R.id.tCurrentWord); tWord.setText(logic.getVisibleWord());
        tScore = findViewById(R.id.tScore); tScore.setText(Integer.toString(logic.getCurrScore()));

        //Initialize imageview
        iGameOver = findViewById(R.id.iGameOver);

        //Initialize restart button and Results button
        bRestart = findViewById(R.id.bRestart); bRestart.setVisibility(View.INVISIBLE);
        bRestart.setOnClickListener(this);
        bResults = findViewById(R.id.bResults); bResults.setVisibility(View.INVISIBLE);
        bResults.setOnClickListener(this);
        bChallenge = findViewById(R.id.bChallenge); bChallenge.setVisibility(View.VISIBLE);
        bChallenge.setOnClickListener(this);


        //Initialize keyboard buttons
        keyboard = new ArrayList<>();
        int buttons = 27;
        for (int i = 1; i < buttons; i++) {
            String bID = "b" + i;
            int resID = getResources().getIdentifier(bID, "id", getPackageName());

            Button b = findViewById(resID);
            keyboard.add(b);

            b.setOnClickListener(this);
        }

        //Initialize background layout
        conLayout = findViewById(R.id.playMenu);
        conLayout.setBackground(getDrawable(R.drawable.gframe00));

        //Initialize animations
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        //Initialize sharedpreference
        sPKey = "hangman.data"; sPKeyHS = "highscore"; sPKeyWL = "winlose"; sPKeyG = "games"; sPKeyRS = "results";
        sPref = getSharedPreferences(sPKey,MODE_PRIVATE);
        sEdit = sPref.edit();

        //Initialize variables from logic
        initializeLogicVariables();

        //Fjerner status baren på toppen af skærmen
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /** SPECIFICALLY INITIALIZES THE LOGIC VARIABLES, GETS USED TO UPDATE STATS REPEATEDLY */
    private void initializeLogicVariables() {
        gamesPlayed = logic.getGamesPlayed();
        if (logic.getListOfWinsLosses().size() > 0) {
            winLose = logic.getListOfWinsLosses().get(logic.getListOfWinsLosses().size() - 1);
        }
        else {
            winLose = 0;
        }
    }

    /** WHEN CLICKING THE A BUTTON SOMETHING HAPPENS! */
    @Override
    public void onClick(View v) {
        //Opdaterer listen over wins og losses for hver gang man har spillet et spil
        initializeLogicVariables();
        if (!logic.isTheGameFinished()){
            //Går igennem alle knapper og finder udaf hvilken knap det var
            checkWhichButtonPressed(v);
        }
        else {
            if (v == bRestart){
                //Nulstiller spillet, opdaterer skærmen såsom knapperne kommer tilbage osv.
                restartGame();
            }
            if (v == bResults){
                //Sender brugeren over til ResultMenu aktiviteten
                startActivity(new Intent(PlayMenu.this, ResultsMenu.class));
                resultActivityPlaying = true;
            }
        }
        if (logic.isGameOver() && !resultActivityPlaying){
            if (logic.getCurrScore() != 0){
                //Tjekker om spillet var vundet eller tabt
                checkIfGameIsWonOrLost();
            }
        }
        if (v == bChallenge){
            //Sender brugeren ind i ChallengeMenu hvor man kan vælge ord fra en liste
            startActivity(new Intent(PlayMenu.this,ChallengeMenu.class));
            //Slutter playmenu aktiviteten
            PlayMenu.this.finish();
        }
        debugMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        debugMessages();
        resultActivityPlaying = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Stopper lyden når man går ud af aktiveteten
        if (winSound != null){
            winSound.release();
        }
        if (loseSound != null){
            loseSound.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Hvis spilleren går ud af aktiviteten, tjekker spillet om spillet var færdigt, hvis det var, adder disse if statements til highscore listen
        if (logic.getCurrScore() != 0){
            if (!logic.isTheGameFinished()){
                logic.getListOfHighscores().add(logic.getCurrScore());
                saveSharedData(logic.getListOfHighscores(), sPKeyHS);

                Result result = new Result(logic.getWrongGuesses(),logic.getCurrWord(), true);
                logic.getListOfResults().add(result);
            }
            //Resetter singleton objektet til næste gang man kommer ind igen til aktiviteten
            logic.restartGame();
            logic.setCurrScore(0);
        }
    }

    private void debugMessages() {
        int logMessage =+ 1;
        Log.d("play","-----------------###--" + logMessage +"--###----------------");
        Log.d("play", "gameOver = " + logic.isGameOver() +
                " - currScore: " + logic.getCurrScore() +
                " - currWord: " + logic.getCurrWord());
        Log.d("play","-------------------------------------");
        Log.d("play", "lastGuess: " + logic.isLastGuessWasRight() +
                " - lastLetter: " + logic.getCurrLetter() +
                " - wrongGuesses: " + logic.getWrongGuesses());
        Log.d("play","-------------------------------------");
        Log.d("play", "highscores: " + logic.getListOfHighscores().size() +
                " - WinsLossesList: " + logic.getListOfWinsLosses().size() +
                " - winLoseStreak: " + logic.getGamesPlayed());
        Log.d("play", "                wordList: " + logic.getListOfWords().size());
        Log.d("play","-----------------$$$--" + logMessage + "--$$$----------------");

    }

    /** CHECKS WHICH BUTTON WAS PRESSED AND TALKS TO LOGIC CLASS TO SEE IF IT WAS CORRECT */
    private void checkWhichButtonPressed(View v) {
        for (int i = 0; i < keyboard.size(); i++) {
            if (v == keyboard.get(i)){
                //laver knappen usynlig
                keyboard.get(i).setVisibility(View.INVISIBLE);

                //Gætter en bokstav til den knap man trykkede på
                logic.guessACharacter(keyboard.get(i).getText().toString());
                tWord.setText(logic.getVisibleWord());

                //Opdaterer pointene
                logic.updateScore();
                tScore.setText(Integer.toString(logic.getCurrScore()));

                //Hvis sidste gæt var forkert, ændres billedet i baggrunden
                if (!logic.isLastGuessWasRight()){
                    changeFrame();
                }
            }
        }
    }

    /** CHANGES THE CURRENT FRAME IF IT WAS A WRONG GUESS */
    private void changeFrame() {
        String frame = "gframe0" + logic.getWrongGuesses();
        int resID = getResources().getIdentifier(frame, "drawable", getPackageName());
        conLayout.setBackground(getDrawable(resID));
    }

    /** RESTARTS THE GAME AND RESETS ALL VIEWS USED AND SCORES ON THE LOGIC END*/
    private void restartGame() {
        //Resetter baggrunden
        conLayout.setBackground(getDrawable(R.drawable.gframe00));

        //Resetter singleton objektet der styrer logikken
        logic.restartGame();
        tScore.setText(Integer.toString(logic.getCurrScore()));

        //Resetter ordet der skal gættes på skærmen
        tWord.setText(logic.getVisibleWord());
        tWord.setTextColor(Color.parseColor("#FF0000"));

        //Laver alle knapper synlige igen og clickable
        for (int i = 0; i < keyboard.size(); i++) {
            keyboard.get(i).setVisibility(View.VISIBLE);
            keyboard.get(i).setClickable(true);
        }

        //Gemmer restart og result knappen fordi spillet er nu igen i gang
        bRestart.setVisibility(View.INVISIBLE);
        bResults.setVisibility(View.INVISIBLE);
        iGameOver.setVisibility(View.INVISIBLE);
    }

    /** CHANGES THE VIEW DEPENDING ON IF IT WAS A WIN OR LOSE. ALSO UPDATES STATS ON THE LOGIC END */
    private void checkIfGameIsWonOrLost() {
        tWord.setText(logic.getCurrWord());

        //Alle knapper fade'er ud og bliver unclickable
        for (int i = 0; i < keyboard.size(); i++) {
            keyboard.get(i).startAnimation(fadeOut);
            keyboard.get(i).setVisibility(View.INVISIBLE);
            keyboard.get(i).setClickable(false);
        }

        //Restart og Results knapperne fade'er ind
        bRestart.startAnimation(fadeIn);
        bRestart.setVisibility(View.VISIBLE);
        bResults.startAnimation(fadeIn);
        bResults.setVisibility(View.VISIBLE);

        //Tjekker om spillet er tabt
        if (logic.getWrongGuesses() == 6){
            //Laver ordet der skulle gættes til at være grønt og animerer lose billedet
            tWord.setTextColor(Color.parseColor("#FF0000"));
            startAnimation(getDrawable(R.drawable.ic_lose));

            //Opdaterer singleton objektet med den nye score og gemmer den også i sharedpreferences
            logic.getListOfHighscores().add(logic.getCurrScore());
            saveSharedData(logic.getListOfHighscores(), sPKeyHS);

            //Man har tabt, derfor minuses win/lose counteren og den bliver addet til arraylisten hos logikken
            winLose--;
            logic.getListOfWinsLosses().add(winLose);

            //adder til hvor mange spil man har spillet
            gamesPlayed++;
            logic.setGamesPlayed(gamesPlayed);

            //Laver et result objekt som bruges i ResultMenu til at se hvilket ord der var gættet og hvor mange gæt det tog
            Result result = new Result(logic.getWrongGuesses(),logic.getCurrWord(), false);
            logic.getListOfResults().add(result);

            //Tænder for lose lyden
            loseSound = MediaPlayer.create(getApplicationContext(),R.raw.lose);
            loseSound.start();
        }
        //Ellers er spillet vundet
        else {
            //Laver ordet der skulle gættes til at være grønt og animerer win billedet
            tWord.setTextColor(Color.parseColor("#8BC34A"));
            startAnimation(getDrawable(R.drawable.ic_win));

            //Skifter baggrunden til et sjovt sejr billede
            conLayout.setBackground(getDrawable(R.drawable.gframe000));

            //Man har vundet, derfor plusser win/lose counteren og den bliver addet til arraylisten hos logikken
            winLose++;
            logic.getListOfWinsLosses().add(winLose);

            //adder til hvor mange spil man har spillet
            gamesPlayed++;
            logic.setGamesPlayed(gamesPlayed);

            //Laver et result objekt som bruges i ResultMenu til at se hvilket ord der var gættet og hvor mange gæt det tog
            Result result = new Result(logic.getWrongGuesses(),logic.getCurrWord(), false);
            logic.getListOfResults().add(result);

            //Tænder for win lyden
            winSound = MediaPlayer.create(getApplicationContext(),R.raw.win);
            winSound.start();
        }

        //Gemmer alle date i sharedpreferences, såsom det bliver opdateret
        saveSharedData(logic.getListOfResults(),sPKeyRS);
        saveSharedData(logic.getListOfWinsLosses(), sPKeyWL);
        saveSharedData(logic.getGamesPlayed(), sPKeyG);
    }

    /** SAVES DATA THROUGH SHAREDPREFERENCE */
    private void saveSharedData(List arrList, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(arrList);
        sEdit.putString(key,json);
        sEdit.apply();
    }

    /** SAVES DATA THROUGH SHAREDPREFERENCE */
    private void saveSharedData(int number, String key) {
        String json = Integer.toString(number);
        sEdit.putString(key, json);
        sEdit.apply();
    }

    /** CALLS AN ANIMATION TO BE PERFORMED ON BUTTONS AND IMAGEVIEWS */
    private void startAnimation(Drawable drawID){
        iGameOver.startAnimation(fadeIn);
        iGameOver.setVisibility(View.VISIBLE);
        iGameOver.setBackground(drawID);
    }
}
