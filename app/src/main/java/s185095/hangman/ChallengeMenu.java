package s185095.hangman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import s185095.hangman.logic.ChallengeAdapter;
import s185095.hangman.logic.Hangman;

public class ChallengeMenu extends AppCompatActivity implements ChallengeAdapter.OnWordListener {

    private RecyclerView rView;
    private List<String> wordList;
    private Hangman logic;
    private ChallengeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challengemenu);

        initializeActivity();
    }

    private void initializeActivity() {

        logic = Hangman.getInstance();
        wordList = logic.getListOfWords();

        rView = findViewById(R.id.recycler_view);
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        rView.setLayoutManager(lManager);
        adapter = new ChallengeAdapter(wordList,this, logic);
        rView.setAdapter(adapter);
    }

    @Override
    public void onWordClick(int postion) {
        startActivity(new Intent(ChallengeMenu.this, PlayMenu.class));
        logic.setCurrWord(logic.getListOfWords().get(postion));
        ChallengeMenu.this.finish();
    }
}
