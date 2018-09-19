import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class WoFServer {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        HashMap<Character, ArrayList<Integer>> wordMap;
        GameData receivedSerializedData, serializedGameData;

        // Get Network Initialization Info
        BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the Port #: ");
        String portInput = fromKeyboard.readLine();
        int port = Integer.parseInt(portInput.replaceAll("\\s+", ""));

        // Initialize Server
        ServerNetworkUtils snu = new ServerNetworkUtils(port);
        snu.createServerSocket();
        snu.setReceivePacket();
        System.out.println("Server is waiting on port #" + port);
        
        // Setup the game
        System.out.println("Enter secret word: ");
        String secret = fromKeyboard.readLine().toLowerCase(); // Ensure it is all lowercase
        wordMap = setWordMapping(secret);
        System.out.println("Starting the game with word: " + secret);

        while (true) {
            receivedSerializedData = snu.getSerializedGameData();
            snu.serverPrint();
            serializedGameData = processGameData(receivedSerializedData, secret, wordMap);
            if (serializedGameData == null) {
                System.out.println("Restarting a new game...");
                System.out.println("Enter secret word: ");
                secret = fromKeyboard.readLine();
                wordMap = setWordMapping(secret);
                continue;
            }
            snu.sendSerializedGameData(serializedGameData);
        }
    }

    /*
     * Set the mapping for the secret word using a HashMap
     * Provides mapping for each character and its corresponding 
     * indices in the string using an ArrayList of Integers
     */
    private static HashMap<Character, ArrayList<Integer>> setWordMapping(String s) {
        // Initialize HashMap with empty int array for every letter
        HashMap<Character, ArrayList<Integer>> mapping = new HashMap<>();
        for (char ch = 'a'; ch <= 'z'; ++ch) {
            mapping.put(Character.valueOf(ch), new ArrayList<Integer>());
        }
        // Set the indices for the 'secret' word in the HashMap
        char[] word = s.toCharArray();
        for (int i = 0; i < word.length; i++) {
            mapping.get(word[i]).add(i);
        }
        return mapping;
    }

    private static GameData processGameData(GameData data, String secret, HashMap<Character, ArrayList<Integer>> wordMap) {
        // Start of Game --> Initialize response with secret word
        if (data.getWord().equals("")) {
            char[] chars = new char[secret.length()];
            Arrays.fill(chars, '-');
            data.setWord(new String(chars));
        }
        // Player has decide to quit the game --> Sadboi
        else if (data.getWord().equals("exit")) {
            data = null;
        }
        // Player has not guessed the word --> Process guess and return
        else {
            Character guess = data.getGuess();
            data.setCharInWord(guess, wordMap.get(guess));
            data.removeLetter(guess);
            if (!data.getWord().contains("-")) {
                data.setMessage("You WIN!!!!");
            }
            data.incrementGuesses();
        }
        return data;
    }
}
