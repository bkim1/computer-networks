package peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import object.Action;
import object.BrokerPacket;
import object.Room;
import utils.PeerToBrokerUtils;
import utils.PeerUtils;

public class Peer {
    private static PeerUtils peerU;
    private static String username, sessionID;
    private static PeerToBrokerUtils pbu;
    private static InetAddress serverAddress;
    private static int serverPort;

    public static void main(String[] args) throws UnknownHostException, IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));

        if (args.length > 0) {
            serverAddress = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);
        }
        else {
            // Get connection info
            System.out.println("Enter IP Address of Broker: ");
            String ip = fromKeyboard.readLine();
            serverAddress = InetAddress.getByName(ip);
            
            System.out.println("Enter Port # of Broker: ");
            serverPort = Integer.parseInt(fromKeyboard.readLine());
        }
        pbu = new PeerToBrokerUtils(serverAddress, serverPort);
        pbu.createClientSocket();

        BrokerPacket sendInfo, rcvInfo;

        try {
            while (true) {
                sendInfo = getInput();

                System.out.println(sendInfo);
                pbu.sendSerializedPacket(sendInfo);
                rcvInfo = pbu.getSerializedPacket();

                System.out.println("Receiving: \n" + rcvInfo + "\n");

                if (rcvInfo == null) { continue; }
                processResponse(rcvInfo);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void processResponse(BrokerPacket resp) throws IOException, ClassNotFoundException {
        switch(resp.getResult()) {
            case REG_OK:
                System.out.println("REG OK: Successfully registered as a new user!\n");
                sessionID = resp.getSessionID();
                username = resp.getUser();
                enterRoom(resp);
                break;

            case REG_FAIL:
                System.out.println("REG FAIL: There's already a user registered with that username!\n" +
                                   "Sorry you'll have to try again :(\n");
                break;

            case LOGIN_OK:
                System.out.println("LOGIN OK: Successfully logged in!\n" +
                                   "BrokerPacket: " + resp);
                sessionID = resp.getSessionID();
                username = resp.getUser();
                enterRoom(resp);
                break;

            case LOGIN_FAIL:
                System.out.println("LOGIN FAIL: Invalid credentials! " +
                                   "Sorry you'll have to try again :(\n");
                break;

            case LOGOUT_OK:
                System.out.println("LOGOUT OK: Successfully logged out!\n");
                sessionID = null;
                username = null;
                break;

            case LOGOUT_FAIL:
                System.out.println("LOGOUT FAIL: Failed to logout!\n");
                break;

            case GET_ROOMS_OK:
                System.out.println("GET ROOMS OK: Successfully got the list of rooms!");
                enterRoom(resp);
                break;

            case GET_ROOMS_FAIL:
                System.out.println("GET ROOMS FAIL: Failed to get the list of rooms!");
                break;

            case UNAUTH:
                System.out.println("UNAUTHORIZED: You need to login or register as a new user first!");
                break;
            
            default:
                break;
        }
    }

    private static BrokerPacket getInput() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        BrokerPacket info = new BrokerPacket();
        breakLabel:
        while (true) {

            System.out.println("\nWhat would you like to do?\n" +
                               "Here are your options: \n" +
                               "1) Register as a new user\n" +
                               "2) Login\n" +
                               "3) Logout\n" +
                               "4) Get Rooms\n");
            System.out.println("Please input 1, 2, 3, or 4...");
            
            String option = input.readLine();
            switch(option) {
                case "1":
                    if (sessionID != null && username != null) {
                        System.out.println("You are already logged in!\n");
                        break;
                    }
                    System.out.println("Username: ");
                    info.setUser(input.readLine());

                    System.out.println("Password: ");
                    info.setPassword(input.readLine());

                    info.setAction(Action.REG);
                    break breakLabel;

                case "2":
                    if (sessionID != null && username != null) {
                        System.out.println("You are already logged in!\n");
                        break;
                    }
                    System.out.println("Username: ");
                    info.setUser(input.readLine());

                    System.out.println("Password: ");
                    info.setPassword(input.readLine());

                    info.setAction(Action.LOGIN);
                    break breakLabel;

                case "3":
                    info.setUser(username);
                    info.setSessionID(sessionID);
                    info.setAction(Action.LOGOUT);
                    break breakLabel;

                case "4":
                    info.setUser(username);    
                    info.setSessionID(sessionID);
                    info.setAction(Action.GET_ROOMS);
                    break breakLabel;
                
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
        return info;
    }

    private static void enterRoom(BrokerPacket packet) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        List<Room> rooms = packet.getRooms();

        System.out.println("\nHere are the available rooms: ");
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println(i + ") " + rooms.get(i).getName());
        }
        System.out.println("\nEnter the number of the room you wish to join, " +
                           "or type 'exit' to return to the main menu: ");

        while (true) {
            try {
                String resp = input.readLine();
                if (resp.equals("exit")) { break; }

                int roomNumber = Integer.parseInt(resp);
                InetAddress ip = InetAddress.getByName(rooms.get(roomNumber).getAddress());
                peerU = new PeerUtils(ip, rooms.get(roomNumber).getPort(), username);
                peerU.joinGroup();

                PeerRcvThread rcvThread = new PeerRcvThread(peerU);
                PeerSendThread sendThread = new PeerSendThread(peerU);

                Thread rThread = new Thread(rcvThread, "Rcv Thread");
                Thread sThread = new Thread(sendThread, "Send Thread");
                rThread.setDaemon(true);

                sThread.start();
                rThread.start();

                sThread.join();
                peerU.leaveGroup();

                System.out.println("\n");

                break;
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Invalid input. Please try again.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}