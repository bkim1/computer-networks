import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Player {
	private static Socket socket;
	private static GameUtils gu;

    public static void main (String[] args) throws IOException {
        BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter server IP Address: ");
        String ip = fromKeyboard.readLine();
        InetAddress serverAddress = InetAddress.getByName(ip);

        System.out.println("Enter port number: ");
        int port = Integer.parseInt(fromKeyboard.readLine());

        socket = new Socket(serverAddress, port);

        System.out.println("Enter player name: ");
        String playerName = fromKeyboard.readLine();

        GameData toReceive, toSend;
        boolean won;
        gu = new GameUtils();

        toSend = new GameData(playerName, new int[3][3], new GameMove(0, 0, 0), false, true, true, 0);

        toSend = getInput(toSend);
        
        Player.sendData(toSend);
        try {
            while (true) {
                toReceive = getData();
                
                if (toReceive.isEnd()) {
                    toSend = restartGame(toReceive.isTie(), false, toReceive);
                    if (toSend != null) { 
                        toSend = getInput(toSend);
                        Player.sendData(toSend);
                        continue;
                    } 
                    else { break; }
                }
                toSend = getInput(toReceive);  // Get player input

                // Check for win!
                won = gu.checkForWin(toSend);
                if (won || toSend.isTie()) {
                    if (won && toSend.isTie()) { toSend.setTie(false); }
                    toSend.gameOver();
                    Player.sendData(toSend);
                    toSend = restartGame(toSend.isTie(), true, toSend);

                    if (toSend == null) { break; }
                }
                Player.sendData(toSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Player.sendData(null);
        System.out.println("Closing out the socket!");
        socket.close();
    }

    private static GameData restartGame(boolean tie, boolean won, GameData oldGame) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String ans;
        if (tie) { System.out.println("It's a tie! Like that would ever happen..."); }
        else if (won) { System.out.println("You won! Congrats!"); }
        else { System.out.println("You lost! Ya hate to see it!"); }
        
        System.out.println("Want to play another? (Y/N) ");
        ans = input.readLine();

        while (!"Y".equals(ans.toUpperCase()) && !"N".equals(ans.toUpperCase())) {
            System.out.println("Gotta use 'Y' or 'N'...");

            System.out.println("Want to play another? (Y/N) ");
            ans = input.readLine();
        }
        GameData data = null;
        if ("Y".equals(ans.toUpperCase())) {
            System.out.println("Ok! Restarting the game...");
            System.out.println("You're now playing with the opposite numbers!");
            data = getInput(new GameData(oldGame.getPlayer(), new int[3][3], new GameMove(0, 0, 0), false, true, !oldGame.isP1Odd(), 0));
        }
        return data;
    }

    private static GameData getInput(GameData toSend) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int playerRow, playerCol, playerMove;
        GameMove move;
        ArrayList<Integer> availableMoves;
        
        if (toSend.isP1Turn()) { availableMoves = toSend.getP1Moves(); }
        else { availableMoves = toSend.getP2Moves(); }
        
        GameData temp = toSend;
        try {
            System.out.println("What number do you want to choose? ");
            System.out.println("Options: " + availableMoves.toString());
            playerMove = Integer.parseInt(input.readLine());
            System.out.println("What row? (0, 1, 2) ");
            playerRow = Integer.parseInt(input.readLine());
            System.out.println("What col? (0, 1, 2) ");
            playerCol = Integer.parseInt(input.readLine());

            move = new GameMove(playerRow, playerCol, playerMove);
    
            temp = gu.applyMove(toSend, move);     
        } catch (NumberFormatException e) {
            temp = null;
        }

        while (temp == null) {
            System.out.println("Invalid move. Please input a valid move\n");

            try {
                System.out.println("What number do you want to choose? ");
                System.out.println("Options: " + availableMoves.toString());
                playerMove = Integer.parseInt(input.readLine());
                System.out.println("What row? (0, 1, 2) ");
                playerRow = Integer.parseInt(input.readLine());
                System.out.println("What col? (0, 1, 2) ");
                playerCol = Integer.parseInt(input.readLine());
    
                move = new GameMove(playerRow, playerCol, playerMove);
    
                temp = gu.applyMove(toSend, move);
            } catch (NumberFormatException e) {
                temp = null;
            }
        }
        return temp;
    }

    private static void sendData(GameData gd) throws IOException {
        OutputStream os = socket.getOutputStream(); 
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.flush();
        oos.writeObject(gd);   //send object to server
        oos.flush();
    }

    private static GameData getData() throws IOException {
    	GameData toReceive = null;
        InputStream is = socket.getInputStream();  
        ObjectInputStream ois = new ObjectInputStream(is);  
        try {
            toReceive = (GameData) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  
        if (toReceive != null) {
            System.out.println("From server: \n" + toReceive.toString());
        }
        return toReceive;
    }

}
