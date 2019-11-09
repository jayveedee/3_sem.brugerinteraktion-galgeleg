package s185095.hangman.logic;

public class Result {

    private int guesses;
    private String word;

    public Result(int guesses, String word) {
        this.guesses = guesses;
        this.word = word;
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
}
