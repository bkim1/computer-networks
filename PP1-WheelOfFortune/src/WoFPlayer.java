import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

        toSend = startGame(pnu, fromKeyboard);
        System.out.println("Received for start of game!");
        System.out.println(toSend.getWord());
        mainLoop:  // Label for breaking out of play again repeats
        while (true) {
            if (toSend != null && toSend.getMessage().contains("WIN")) {
                System.out.println("CONGRATS! You got it!");
                System.out.println("It took you " + toSend.getNumberOfGuesses() + " to guess the word!");
                wordList.add(toSend.getWord());  // Add word to list for tracking
                while (true) {
                    System.out.println("Do you want to play another game? (Y/N): ");
                    String ans = fromKeyboard.readLine().toUpperCase();
                    if (ans.equals("N")) { break mainLoop; }  // Break to main loop
                    else if (ans.equals("Y")) {
                        toSend = startGame(pnu, fromKeyboard);
                        break;
                    }
                    else {
                        System.out.println("Incorrect input. Please try again.");
                    }
                }
                continue; // Player wants to play again --> Continue loop
            }
            System.out.println("Available letters: " + toSend.getLetters());
            System.out.println("Guess a letter: ");
            String playerInput = fromKeyboard.readLine();

            if (playerInput.length() > 1 && playerInput.equals("exit")) {
                toSend.setMessage(playerInput);
            }
            else if (playerInput.length() > 1) {
                System.out.println("Only a single letter is allowed. Please try again.");
                continue;
            }
            pnu.sendSerializedGameData(toSend);
         }
         System.out.println("Here are all the words you guessed: " + wordList.toString());
         pnu.closeSocket();
    }

    private static GameData startGame(PlayerNetworkUtils pnu, BufferedReader r) throws IOException, ClassNotFoundException {
        System.out.println("Enter your name: ");
        String name = r.readLine();
        System.out.println("Name: " + name);
        GameData data = new GameData(name);
        
        pnu.sendSerializedGameData(data);
        GameData received = pnu.getSerializedGameData();
        System.out.println("Received data!");
        return received;
    }
}
