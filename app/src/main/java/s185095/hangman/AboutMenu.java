package s185095.hangman;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AboutMenu extends AppCompatActivity implements View.OnClickListener {

    private Button bGit, bIns;
    private ImageView iOSRS1, iOSRS2;
    private TextView tCredits, tUX1, tUX2, tUX3;
    private String gitURL, osrs1URL, osrs2URL, insURL;
    private List<TextView> textViewList;
    private Animation fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutmenu);
        initializeActivity();
    }

    /** INITIALIZES THE OBJECTS AND VIEWS */
    private void initializeActivity() {

        gitURL = "https://github.com/jayveedee/3_sem.brugerinteraktion-galgeleg";
        insURL = "https://www.inside.dtu.dk/en/dtuinside/generelt/telefonbog/person?id=137487&tab=0";
        osrs1URL = "https://i.imgur.com/JFxg2m1.jpg";
        osrs2URL = "https://imgur.com/72qSWYQ.jpg";

        bGit = findViewById(R.id.bGit); bGit.setOnClickListener(this);
        bIns = findViewById(R.id.bIns); bIns.setOnClickListener(this);

        iOSRS1 = findViewById(R.id.iOSRS1); iOSRS1.setOnClickListener(this);
        iOSRS2 = findViewById(R.id.iOSRS2); iOSRS2.setOnClickListener(this);

        textViewList = new ArrayList<>();

        tCredits = findViewById(R.id.tCredits); tCredits.setOnClickListener(this);
        tUX1 = findViewById(R.id.tUX1); tUX1.setOnClickListener(this);
        tUX2 = findViewById(R.id.tUX2); tUX2.setOnClickListener(this);
        tUX3 = findViewById(R.id.tUX3); tUX3.setOnClickListener(this);

        textViewList.add(tCredits);
        textViewList.add(tUX1);
        textViewList.add(tUX2);
        textViewList.add(tUX3);

        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

    }

    /** FUNCTIONALITY ADDED TO CLICKABLE VIEWS */
    @Override
    public void onClick(View v) {

        if (v == bGit){
            Log.d("about", "Opening: " + bGit.getText());
            openWebApp(gitURL);
            bGit.startAnimation(fadeOut);
            bGit.setClickable(false);
            bGit.setVisibility(View.INVISIBLE);
        }
        if (v == bIns){
            Log.d("about", "Opening: " + bIns.getText());
            openWebApp(insURL);
            bIns.startAnimation(fadeOut);
            bIns.setClickable(false);
            bIns.setVisibility(View.INVISIBLE);
        }
        if (v == iOSRS1){
            Log.d("about", "Opening: " + iOSRS1.getContentDescription());
            openWebApp(osrs1URL);
            iOSRS1.startAnimation(fadeOut);
            iOSRS1.setClickable(false);
            iOSRS1.setVisibility(View.INVISIBLE);
        }
        if (v == iOSRS2){
            Log.d("about", "Opening: " + iOSRS2.getContentDescription());
            openWebApp(osrs2URL);
            iOSRS2.startAnimation(fadeOut);
            iOSRS2.setClickable(false);
            iOSRS2.setVisibility(View.INVISIBLE);
        }
        if (textViewList.contains(v)){
            for (int i = 0; i < textViewList.size(); i++) {
                if (v == textViewList.get(i)){
                    Log.d("about", "Clicking on: " + textViewList.get(i).getContentDescription());
                    textViewList.get(i).startAnimation(fadeOut);
                    textViewList.get(i).setVisibility(View.INVISIBLE);
                    textViewList.get(i).setClickable(false);
                }
            }
        }
    }

    /** OPENS A WEBPAGE DEPENDING ON WHICH URL HAS BEEN USED */
    private void openWebApp(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
