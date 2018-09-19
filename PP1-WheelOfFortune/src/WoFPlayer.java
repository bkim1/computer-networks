import java.net.InetAddress;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WoFPlayer {
    public static void main (String[] args) throws IOException, ClassNotFoundException {
        GameData toSend, toReceive;
        ArrayList<String> wordList = new ArrayList<String>();
        BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter IP Address: ");
        String ip = fromKeyboard.readLine();
        InetAddress serverAddress = InetAddress.getByName(ip);
        
        System.out.println("Enter Port #: ");
        int port = Integer.parseInt(fromKeyboard.readLine());
        PlayerNetworkUtils pnu = new PlayerNetworkUtils(serverAddress, port);
        pnu.createClientSocket();

        System.out.println("Enter your name: ");
        String name = fromKeyboard.readLine();
        System.out.println("Name: " + name);
        GameData data = new GameData(name);
        
        pnu.sendSerializedGameData(data);
        
        mainLoop:  // Label for breaking out of play again repeats
        while (true) {
            toReceive = pnu.getSerializedGameData();
            if (toReceive != null && toReceive.getMessage().contains("WIN")) {
                System.out.println("CONGRATS! You got it!");
                System.out.println("It took you " + toReceive.getNumberOfGuesses() + " to guess the word!");
                wordList.add(toReceive.getWord());  // Add word to list for tracking
                innerLoop:
                while (true) {
                    System.out.println("Do you want to play another game? (Y/N): ");
                    String ans = fromKeyboard.readLine().toUpperCase();
                    if (ans.equals("N")) { break mainLoop; }  // Break to main loop
                    else if (ans.equals("Y")) {
                        // toSend = startGame(pnu, fromKeyboard);
                        GameData restartData = new GameData(name);
                        restartData.setMessage("restart");
        
                        pnu.sendSerializedGameData(restartData);
                        System.out.println("Waiting for server to restart game");
                        toReceive = pnu.getSerializedGameData();
                        break innerLoop;
                    }
                    else {
                        System.out.println("Incorrect input. Please try again.");
                    }
                }
            }
            System.out.println(toReceive.getWord());
            System.out.println("Current number of guesses: " + toReceive.getNumberOfGuesses());
            System.out.println("Available letters: " + toReceive.getLetters());
            System.out.println("Guess a letter: ");
            String playerInput = fromKeyboard.readLine();
            toSend = new GameData(toReceive);
            if (playerInput.length() > 1 && playerInput.equals("exit")) {
                toSend.setMessage(playerInput);
                pnu.sendSerializedGameData(toSend);
                break;
            }
            else if (playerInput.length() > 1) {
                System.out.println("Only a single letter is allowed. Please try again.\n");
                while (true) {
                    System.out.println("Current number of guesses: " + toReceive.getNumberOfGuesses());
                    System.out.println("Available letters: " + toReceive.getLetters());
                    System.out.println("Guess a letter: ");
                    playerInput = fromKeyboard.readLine();
                    if (playerInput.length() == 1) { break; }
                }
            }
            // Set the guess
            Character ch = Character.valueOf(playerInput.charAt(0));
            toSend.setGuess(ch);
            
            pnu.sendSerializedGameData(toSend);
         }

         // Close out the game for the player
         System.out.println("Thanks for playing " + name + "!");
         System.out.println("Here are all the words you guessed: " + wordList.toString());
         pnu.closeSocket();
    } 
}
