package s185095.hangman.logic;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import s185095.hangman.R;

public class WordListAdapter extends ArrayAdapter<Result> {

    /**
     * This class is used to make the ListView function and by using a custom layout, there was needed to make a new class that extended ArrayAdapter with the Result as a parameter
     */

    private Context context;
    private int resources;


    public WordListAdapter(@NonNull Context context, int resource, @NonNull List<Result> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resources = resource;
    }

    /** METHOD THAT UPDATES THE LISTVIEW, WITH THE CUSTOM LAYOUT AND DOES SOME FUNCTIONALITY AS WELL */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Kalder getItem for at få objektets data
        String word = getItem(position).getWord();
        int guesses = getItem(position).getGuesses();
        boolean quitter = getItem(position).isQuitter();

        Result result = new Result(guesses,word,quitter);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resources,parent,false);

        //Initialiserer TextViewene
        TextView tWord = convertView.findViewById(R.id.tWord);
        TextView tGuess = convertView.findViewById(R.id.tGuesses);

        //Sætter variablerne fra objektet på viewet
        tWord.setText(word);
        tGuess.setText(Integer.toString(guesses));

        Log.d("result", "Word: " + word);
        Log.d("result", "guesses: " + guesses);

        //Ændrer på farverne alt efter hvor mange forsøg det tog at gærre rigtigt
        if (guesses >= 6){
            tWord.setTextColor(Color.parseColor("#FF0000"));
            tGuess.setTextColor(Color.parseColor("#FF0000"));
        }
        if (guesses >= 4 && guesses <= 5){
            tWord.setTextColor(Color.parseColor("#FF9800"));
            tGuess.setTextColor(Color.parseColor("#FF9800"));
        }
        if (guesses <= 3 && guesses > 0){
            tWord.setTextColor(Color.parseColor("#8BC34A"));
            tGuess.setTextColor(Color.parseColor("#8BC34A"));
        }
        if (guesses == 0){
            tWord.setTextColor(Color.parseColor("#00BCD4"));
            tGuess.setTextColor(Color.parseColor("#00BCD4"));
        }
        if (quitter){
            tWord.setTextColor(Color.parseColor("#909090"));
            tGuess.setTextColor(Color.parseColor("#909090"));
        }
        return convertView;
    }
}
