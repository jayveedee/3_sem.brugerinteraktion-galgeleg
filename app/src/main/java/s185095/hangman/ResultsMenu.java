package s185095.hangman;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import s185095.hangman.logic.Hangman;
import s185095.hangman.logic.WordListAdapter;

public class ResultsMenu extends AppCompatActivity {

    /**
     * There's not much inside of this class, as most of the things needed for it are made inside of WordListAdapter and other than that
     * Just getting data from the singleton Hangman, to update the textfield with new data
     */

    private Hangman logic;
    private ListView listView;
    private TextView tCurrWord, tCurrScore;
    private WordListAdapter adapter;
    private MediaPlayer toBeContinuedSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultsmenu);
        initializeActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        toBeContinuedSound.release();
    }

    /** INITIALIZES THE VIEW AND CALLS THE ADAPTER TO GET THE LISTVIEW UP AND RUNNING */
    private void initializeActivity() {
        //initialiserer logikken og listview
        logic = Hangman.getInstance();
        listView = findViewById(R.id.listView);

        //Initialiserer textviewene
        tCurrWord = findViewById(R.id.tCurrentWord); tCurrWord.setText(logic.getCurrWord());
        tCurrScore = findViewById(R.id.tCurrentScore); tCurrScore.setText(Integer.toString(logic.getCurrScore()));

        //Kalder adapter objektet og giver den alle nødvendige ting til at udføre opsættelsen af listviewet
        adapter = new WordListAdapter(this, R.layout.listview_layout, logic.getListOfResults());
        listView.setAdapter(adapter);

        //Initialiserer lyd effect
        toBeContinuedSound = MediaPlayer.create(getApplicationContext(),R.raw.tobecontinued);
        toBeContinuedSound.start();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}

