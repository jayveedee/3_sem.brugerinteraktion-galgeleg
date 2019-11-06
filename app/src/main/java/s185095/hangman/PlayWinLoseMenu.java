package s185095.hangman;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.firezenk.bubbleemitter.BubbleEmitterView;

import s185095.hangman.logic.Hangman;

public class PlayWinLoseMenu extends AppCompatActivity implements View.OnClickListener {

    private Hangman logic;
    private BubbleEmitterView bubbleEmitter;
    private Button bBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playwinlosemenu);
        bBubble = findViewById(R.id.bBubble); bBubble.setOnClickListener(this);
        bubbleEmitter = findViewById(R.id.bubbleEmitter);

    }

    @Override
    public void onClick(View v) {
        if (v == bBubble){
            bubbleEmitter.emitBubble(100);
        }
    }
}
