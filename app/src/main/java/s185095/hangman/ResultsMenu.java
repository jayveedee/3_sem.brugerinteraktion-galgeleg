package s185095.hangman;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import s185095.hangman.logic.Hangman;

public class ResultsMenu extends AppCompatActivity {

    private Hangman logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultsmenu);

        ListView listView = findViewById(R.id.listView);



    }
}

