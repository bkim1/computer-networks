import java.io.Serializable;
import java.util.ArrayList;

public class GameData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String player;
    private String currentWord;
    private String message;
    private Character guess;
    private ArrayList<Character> letters;
    private int numGuesses;

    public GameData(String p, String w, String m, Character g, ArrayList<Character> l, int n) {
        this.player = p;
        this.currentWord = w;
        this.message = m;
        this.guess = g;
        this.letters = l;
        this.numGuesses = n;
    }
    public GameData(String p, String w, Character g, ArrayList<Character> l, int n) {
        this.player = p;
        this.currentWord = w;
        this.guess = g;
        this.letters = l;
        this.numGuesses = n;
    }
    
    public GameData(String p) {
        this.player = p;
        this.currentWord = "";
        this.letters = new ArrayList<>();
        this.numGuesses = 0;

        // Initialize letters available to use
        for (char ch = 'a'; ch <= 'z'; ++ch) {
            letters.add(Character.valueOf(ch));
        }
    }
    
    public String getPlayer() { return this.player; }
    public void setPlayer(String p) { this.player = p; }
    
    public String getWord() { return this.currentWord; }
    public void setWord(String s) { this.currentWord = s; }
    public void setCharInWord(Character c, ArrayList<Integer> indices) {
        StringBuilder word = new StringBuilder(this.currentWord);
        for (int i : indices) {
            word.setCharAt(i, c);
        }
        this.currentWord = word.toString();
    }

    public String getMessage() { return this.message; }
    public void setMessage(String s) { this.message = s; }

    public Character getGuess() { return this.guess; }
    public void setGuess(Character g) { this.guess = g; }
    
    public ArrayList<Character> getLetters() { return this.letters; }
    public void removeLetter(Character c) { this.letters.remove(c); }
    public void resetLetters() {
        this.letters = new ArrayList<>();
        // Initialize letters available to use
        for (char ch = 'a'; ch <= 'z'; ++ch) {
            letters.add(Character.valueOf(ch));
        }
    }
    
    public int getNumberOfGuesses() { return this.numGuesses; }
    public void incrementGuesses() { this.numGuesses++; }
    public void resetGuesses() { this.numGuesses = 0; }
}
