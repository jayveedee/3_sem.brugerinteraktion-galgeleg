package s185095.hangman.logic;

public class Result {

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

    public void setGuesses(int guesses) {
        this.guesses = guesses;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isQuitter() {
        return quitter;
    }

    public void setQuitter(boolean quitter) {
        this.quitter = quitter;
    }
}
