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
        fromKeyboard.close();

        GameData toReceive, toSend;
        GameUtils gu = new GameUtils();

        toSend = new GameData(playerName, new int[3][3], new GameMove(0, 0, 1), false, true, true, 0);

        toSend = getInput(toSend);
        
        Player.sendData(toSend);
        try {
            while (true) {
                toReceive = getData();
                
                toSend = getInput(toReceive);  // Get player input
                Player.sendData(toSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Closing out the socket!");
        socket.close();
    }

    private static GameData getInput(GameData toSend) {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int playerRow, playerCol, playerMove;
        ArrayList<Integer> availableMoves;
        
        if (toSend.isP1Turn()) { availableMoves = toSend.getP1Moves(); }
        else { availableMoves = toSend.getP2Moves(); }
        

        try {
            System.out.println("What number do you want to choose? ");
            System.out.println("Options: " + availableMoves.toString());
            playerMove = Integer.parseInt(input.readLine());
            System.out.println("What row? (0, 1, 2) ");
            playerRow = Integer.parseInt(input.readLine());
            System.out.println("What col? (0, 1, 2) ");
            playerCol = Integer.parseInt(input.readLine());

            GameMove move = new GameMove(playerRow, playerCol, playerMove);
    
            toSend = gu.applyMove(toSend, move);     
        } catch (NumberFormatException e) {
            toSend = null;
        }

        while (toSend == null) {
            System.out.println("Invalid move. Please input a valid move\n");

            try {
                System.out.println("What number do you want to choose? ");
                System.out.println("Options: " + availableMoves.toString());
                playerMove = input.readLine();
                System.out.println("What row? (0, 1, 2) ");
                playerRow = Integer.parseInt(input.readLine());
                System.out.println("What col? (0, 1, 2) ");
                playerCol = Integer.parseInt(input.readLine());
    
                move = new GameMove(playerRow, playerCol, playerMove);
    
                toSend = gu.applyMove(toSend, move);
            } catch (NumberFormatException e) {
                toSend = null;
            }
        }
        input.close();
        return toSend;
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
