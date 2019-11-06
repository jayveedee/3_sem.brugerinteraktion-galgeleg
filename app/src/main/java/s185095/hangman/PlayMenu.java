package s185095.hangman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import java.util.List;

import s185095.hangman.logic.Hangman;

public class PlayMenu extends AppCompatActivity implements View.OnClickListener {

    private static Hangman logic;
    private TextView tWord, tScore;
    private ImageView iGameOver;
    private Button bRestart;
    private List<Button> keyboard;
    private ConstraintLayout conLayout;
    private Animation fadeIn, fadeOut;
    private SharedPreferences sPref;
    private SharedPreferences.Editor sEdit;
    private String sPKey, sPKeyHS, sPKeyWL, sPKeyG;
    private int gamesPlayed, winLose;


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
        tWord = findViewById(R.id.tWord); tWord.setText(logic.getVisibleWord());
        tScore = findViewById(R.id.tScore); tScore.setText(Integer.toString(logic.getCurrScore()));

        //Initialize imageview
        iGameOver = findViewById(R.id.iGameOver);

        //Initialize restart button
        bRestart = findViewById(R.id.bRestart); bRestart.setVisibility(View.INVISIBLE);
        bRestart.setOnClickListener(this);

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
        sPKey = "hangman.data"; sPKeyHS = "highscore"; sPKeyWL = "winlose"; sPKeyG = "games";
        sPref = getSharedPreferences(sPKey,MODE_PRIVATE);
        sEdit = sPref.edit();

        //Initialize variables from logic
        initializeLogicVariables();
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
        initializeLogicVariables();
        if (!logic.isTheGameFinished()){
            checkWhichButtonPressed(v);
        }
        else {
            if (v == bRestart){
                restartGame();
            }
        }
        if (logic.isGameOver()){
            checkIfGameIsWonOrLost();
        }
        debugMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        debugMessages();
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
                keyboard.get(i).setVisibility(View.INVISIBLE);

                logic.guessACharacter(keyboard.get(i).getText().toString());
                tWord.setText(logic.getVisibleWord());

                logic.updateScore();
                tScore.setText(Integer.toString(logic.getCurrScore()));

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
        conLayout.setBackground(getDrawable(R.drawable.gframe00));

        if (logic.getWrongGuesses() == 6){
            logic.getListOfHighscores().add(logic.getCurrScore());
            saveSharedData(logic.getListOfHighscores(), sPKeyHS);
            logic.setCurrScore(0);
        }
        logic.restartGame();
        tScore.setText(Integer.toString(logic.getCurrScore()));

        tWord.setText(logic.getVisibleWord());
        tWord.setTextColor(Color.parseColor("#FF0000"));

        for (int i = 0; i < keyboard.size(); i++) {
            keyboard.get(i).setVisibility(View.VISIBLE);
            keyboard.get(i).setClickable(true);
        }

        bRestart.setVisibility(View.INVISIBLE);
        iGameOver.setVisibility(View.INVISIBLE);
    }

    /** SAVES DATA THROUGH SHAREDPREFERENCE */
    private void saveSharedData(List<Integer> arrList, String key) {
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

    /** CHANGES THE VIEW DEPENDING ON IF IT WAS A WIN OR LOSE. ALSO UPDATES STATS ON THE LOGIC END */
    private void checkIfGameIsWonOrLost() {
        tWord.setText(logic.getCurrWord());

        for (int i = 0; i < keyboard.size(); i++) {
            keyboard.get(i).startAnimation(fadeOut);
            keyboard.get(i).setVisibility(View.INVISIBLE);
            keyboard.get(i).setClickable(false);
        }

        bRestart.startAnimation(fadeIn);
        bRestart.setVisibility(View.VISIBLE);

        if (logic.getWrongGuesses() == 6){
            tWord.setTextColor(Color.parseColor("#FF0000"));
            startAnimation(getDrawable(R.drawable.ic_lose));

            winLose--;
            logic.getListOfWinsLosses().add(winLose);

            gamesPlayed++;
            logic.setGamesPlayed(gamesPlayed);
        }
        else {
            tWord.setTextColor(Color.parseColor("#8BC34A"));
            startAnimation(getDrawable(R.drawable.ic_win));

            conLayout.setBackground(getDrawable(R.drawable.gframe000));

            winLose++;
            logic.getListOfWinsLosses().add(winLose);

            gamesPlayed++;
            logic.setGamesPlayed(gamesPlayed);
        }

        saveSharedData(logic.getListOfWinsLosses(), sPKeyWL);
        saveSharedData(logic.getGamesPlayed(), sPKeyG);
    }

    /** CALLS AN ANIMATION TO BE PERFORMED ON BUTTONS AND IMAGEVIEWS */
    private void startAnimation(Drawable drawID){
        iGameOver.startAnimation(fadeIn);
        iGameOver.setVisibility(View.VISIBLE);
        iGameOver.setBackground(drawID);
    }
}
