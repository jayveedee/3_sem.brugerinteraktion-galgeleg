package s185095.hangman.logic;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hangman {

    /**
     * This class handles most of the logic, when you guess a character, update a word, update score and so on.
     * It also keeps track on all the variables necessary for the statistics implemented in the activities.
     */

    private static final Hangman ourInstance = new Hangman();
    private List<String> listOfWords;
    private List<String> listOfLettersInWord;
    private List<Integer> listOfHighscores;
    private List<Integer> listOfWinsLosses;
    private List<Result> listOfResults;
    private String currWord;
    private String visibleWord;
    private String currLetter;
    private boolean gameOver;
    private boolean lastGuessWasRight;
    private boolean wordIsVisible;
    private int wrongGuesses;
    private int gamesPlayed;
    private int currScore;


    /** INSTANTIATES THE NECESSARY VARIABLES FOR THE LOGIC CLASS*/
    private Hangman() {
        listOfWords = new ArrayList<>();
        listOfLettersInWord = new ArrayList<>();
        listOfHighscores = new ArrayList<>();
        listOfWinsLosses = new ArrayList<>();
        listOfResults = new ArrayList<>();
        listOfWinsLosses.add(0);
        gamesPlayed = 0;
        restartGame();
    }

    /** SINGLETON DECLARATION */
    public static Hangman getInstance() {
        return ourInstance;
    }

    /** RESTARTS THE GAME */
    public void restartGame(){
        if (wrongGuesses == 6){
            currScore = 0;
        }
        listOfLettersInWord.clear();
        wrongGuesses = 0;
        currLetter = "";
        gameOver = false;
        if (listOfWords.isEmpty()){
            listOfWords.add("pineapple");
            listOfWords.add("cookie");
            listOfWords.add("wood");
            listOfWords.add("log");
            currWord = listOfWords.get(new Random().nextInt(listOfWords.size()));
        }
        else {
            currWord = listOfWords.get(new Random().nextInt(listOfWords.size()));
        }
        updateVisibleWord();
    }

    /** COMPLETELY RESETS THE GAME, HIGHSCORES AND SO ON */
    public void resetGame(){
        listOfWinsLosses.clear();
        listOfHighscores.clear();
        listOfResults.clear();
        gamesPlayed = 0;
        listOfWinsLosses.add(0);
    }

    /** UPDATES THE SCORE WITH SOME HARDCODED VALUES FOR FUN */
    public void updateScore(){
        if (lastGuessWasRight){
            currScore += 15;
        }
        else {
            currScore -= 10;
        }
        if (gameOver){
            if (wrongGuesses == 6){
                currScore -= 75;
            }
            else {
                currScore += 100;
            }
        }
    }

    /** GUESSES CHARACTERS IN A LETTER */
    public void guessACharacter(String character){
        currLetter = character;
        Log.d("logic", currWord);
        if (character.length() != 1){
            Log.d("logic","Character empty");
            return;
        }
        if (listOfLettersInWord.contains(character)){
            Log.d("logic", "Word already contains the character: " + character);
            return;
        }
        if (wordIsVisible || wrongGuesses == 6){
            Log.d("logic","Game is over");
            return;
        }

        listOfLettersInWord.add(character);

        if (currWord.contains(character)){
            Log.d("logic", "Guessing " + character +" was right");
            lastGuessWasRight= true;
        }
        else {
            Log.d("logic", "Guessing " + character +" was not right");
            lastGuessWasRight = false;
            wrongGuesses += 1;
            Log.d("logic", "There are now " + wrongGuesses + " wrong guesses");
            if (wrongGuesses == 6){
                gameOver = true;
            }
        }
        updateVisibleWord();

    }

    /** UPDATES THE VISIBLE WORD */
    private boolean updateVisibleWord() {
        visibleWord = "";
        wordIsVisible = true;

        for (int i = 0; i < currWord.length(); i++) {
            String letter = currWord.substring(i, i + 1);
            if (listOfLettersInWord.contains(letter)){
                visibleWord += letter;
            }
            else {
                visibleWord += "*";
                wordIsVisible = false;
            }
        }
        return isTheGameFinished();
    }

    /** RETRIEVES A COMMA SEPERATED FILE AND CONVERTS IT TO WORDS IN A LIST */
    public void getWordsFromSheets() throws IOException {
        String sheetsID1 = "https://docs.google.com/spreadsheets/d/";
        String sheetsID2 = "1TYT4cwgluOF4IE2OBNkcQfaZmz7VLSMfbj0HYaVBsug";
        String sheetsID3 = "/export?format=csv&id=";

        String data = getURL(sheetsID1 + sheetsID2 + sheetsID3);

        for(String linesOfData : data.split("\n")){
            String[] sheetData = linesOfData.split(",",-1);

            String currWord = sheetData[0].trim().toLowerCase();
            if (currWord.isEmpty()){
                continue;
            }
            listOfWords.add(currWord);
        }
    }

    /** USES THE URL TO FIND AND RETRIEVE THE FILE AND CONVERTS IT TO A STRING */
    private static String getURL(String url) throws IOException {
        BufferedReader buffReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder strBuilder = new StringBuilder();

        String linesOfData = buffReader.readLine();

        while (linesOfData != null){
            strBuilder.append(linesOfData + "\n");
            linesOfData = buffReader.readLine();
        }
        return strBuilder.toString();
    }

    /** CHECKS IF THE GAME IS FINISHED */
    public boolean isTheGameFinished(){
        if (wordIsVisible || wrongGuesses == 6){
            gameOver = true;
            return true;
        }
        else {
            gameOver = false;
            return false;
        }
    }


   /**
    ** GETTERS AND SETTERS BELOW
    **/

    public List<String> getListOfWords() {
        return listOfWords;
    }

    public List<Integer> getListOfHighscores() {
        return listOfHighscores;
    }

    public void setListOfHighscores(List<Integer> listOfHighscores) {
        this.listOfHighscores = listOfHighscores;
    }

    public List<Integer> getListOfWinsLosses() {
        return listOfWinsLosses;
    }

    public void setListOfWinsLosses(List<Integer> listOfWinsLosses) {
        this.listOfWinsLosses = listOfWinsLosses;
    }

    public String getCurrWord() {
        return currWord;
    }

    public String getVisibleWord() {
        return visibleWord;
    }

    public String getCurrLetter() {
        return currLetter;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isLastGuessWasRight() {
        return lastGuessWasRight;
    }

    public int getWrongGuesses() {
        return wrongGuesses;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getCurrScore() {
        return currScore;
    }

    public void setCurrScore(int currScore) {
        this.currScore = currScore;
    }

    public List<Result> getListOfResults() {
        return listOfResults;
    }

    public void setListOfResults(List<Result> listOfResults) {
        this.listOfResults = listOfResults;
    }

    public void setCurrWord(String currWord) {
        this.currWord = currWord;
        updateVisibleWord();
    }
}
