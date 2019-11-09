package s185095.hangman;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import s185095.hangman.logic.Hangman;
import s185095.hangman.logic.WordListAdapter;

public class ResultsMenu extends AppCompatActivity {

    private Hangman logic;
    private ListView listView;
    private TextView tCurrWord, tCurrScore;
    private WordListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultsmenu);
        initializeActivity();
    }

    private void initializeActivity() {
        logic = Hangman.getInstance();
        listView = findViewById(R.id.listView);

        tCurrWord = findViewById(R.id.tCurrentWord); tCurrWord.setText(logic.getCurrWord());
        tCurrScore = findViewById(R.id.tCurrentScore); tCurrScore.setText(Integer.toString(logic.getCurrScore()));

        adapter = new WordListAdapter(this, R.layout.listview_layout, logic.getListOfResults());
        listView.setAdapter(adapter);
    }
}

