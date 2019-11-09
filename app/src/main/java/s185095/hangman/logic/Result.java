package s185095.hangman.logic;

public class Result {

    /**
     * This is the result object, which is used for the listview on ResultsMenu. It contains some variables that are used on the view.
     * Very basic class, only used to store data.
     */

    private int guesses;
    private String word;
    private boolean quitter;

    public Result(int guesses, String word, boolean quitter) {
        this.guesses = guesses;
        this.word = word;
        this.quitter = quitter;
    }

    public int getGuesses() {
        return guesses;
    }

    public String getWord() {
        return word;
    }

    public boolean isQuitter() {
        return quitter;
    }
}
