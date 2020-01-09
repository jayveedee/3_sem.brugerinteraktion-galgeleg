package s185095.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import s185095.hangman.logic.ChallengeAdapter;
import s185095.hangman.logic.Hangman;

public class ChallengeMenu extends AppCompatActivity implements ChallengeAdapter.OnWordListener {

    /** CHALLENGEMENU CONTAINS A LIST OF WORDS THAT CAN BE CLICKED ON AND THEN THAT WORD
     * WILL BE LATER USED IN THE PLAYMENU. ABOVE YOU CAN SEE THE CLASS IMPLEMENTS AN INTERFACE CALLED OnWordListener
     * WHICH IS USED TO CALL THE ONCLICK METHOD FROM WITHIN THE ADAPTER*/

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

        //Initialiserer logikken
        logic = Hangman.getInstance();
        wordList = logic.getListOfWords();

        //Laver et recyclerview og en layoutmanager, samt en adapter der styrer hvilke ting skal indsættes hvor i viewet
        rView = findViewById(R.id.recycler_view);
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        rView.setLayoutManager(lManager);
        adapter = new ChallengeAdapter(wordList,this);
        rView.setAdapter(adapter);

        //Fjerner status baren på toppen af skærmen
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onWordClick(int postion) {
        //Når man klikker på et ord, vil man derefter blive sent tilbage til playmenu hvor det klikkede ord er det som skal gættes
        startActivity(new Intent(ChallengeMenu.this, PlayMenu.class));
        logic.setCurrWord(logic.getListOfWords().get(postion));

        //Slutter aktiviteten, såsom man ikke kan gå tilbage til denne aktivitet
        ChallengeMenu.this.finish();
    }
}
