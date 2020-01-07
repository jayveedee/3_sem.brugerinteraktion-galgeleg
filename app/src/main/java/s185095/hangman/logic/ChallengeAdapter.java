package s185095.hangman.logic;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import s185095.hangman.R;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder> {

    private static final String TAG = "ChallengeAdapter";

    private List<String> wordList;
    private OnWordListener onWordListener;
    private Hangman logic;

    public ChallengeAdapter(List<String> wordList, OnWordListener onWordListener, Hangman logic) {
        this.wordList = wordList;
        this.onWordListener = onWordListener;
        this.logic = logic;
    }

    @NonNull
    @Override
    public ChallengeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,parent,false);
        return new ViewHolder(view, onWordListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeAdapter.ViewHolder holder, int position) {
        try {
            holder.tChallenge.setText(wordList.get(position));
        }catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: \n" + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (wordList == null){
            return 0;
        }
        else {
            return wordList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnWordListener onWordListener;
        TextView tChallenge;

        public ViewHolder(@NonNull View itemView, OnWordListener onWordListener) {
            super(itemView);
            this.onWordListener = onWordListener;
            this.tChallenge = itemView.findViewById(R.id.tChallenge);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onWordListener.onWordClick(getAdapterPosition());
        }
    }

    public interface OnWordListener{
        void onWordClick(int postion);
    }
}
