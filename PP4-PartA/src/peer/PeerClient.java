package peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import serializedData.*;
import utils.PeerToBrokerUtils;;

public class PeerClient {
    static Map<String, PeerInfo> registeredFiles;
    static Map<String, Thread> serverThreads;

    public static void main(String[] args) throws UnknownHostException, IOException {
        InetAddress serverAddress;
        int port;

        if (args.length > 0) {
            serverAddress = InetAddress.getByName(args[0]);
            port = Integer.parseInt(args[1]);
        }
        else {
            BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
    
            // Get connection info
            System.out.println("Enter IP Address of Broker: ");
            String ip = fromKeyboard.readLine();
            serverAddress = InetAddress.getByName(ip);
            
            System.out.println("Enter Port # of Broker: ");
            port = Integer.parseInt(fromKeyboard.readLine());
        }
        PeerToBrokerUtils pbu = new PeerToBrokerUtils(serverAddress, port);
        pbu.createClientSocket();

        registeredFiles = new HashMap<>();
        serverThreads = new HashMap<>();

        PeerInfo rcvInfo, sendInfo;

        try {
            while (true) {
                sendInfo = getInput();
                System.out.println(sendInfo);
                pbu.sendSerializedPacket(sendInfo);
                rcvInfo = pbu.getSerializedPacket();

                if (rcvInfo == null) { continue; }
                processResponse(rcvInfo);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void processResponse(PeerInfo resp) {
        switch(resp.getAction()) {
            case REG_OK:
                System.out.println("REG OK: Successfully registered the file!\n");
                registeredFiles.put(resp.getFileName(), resp);

                // Spawn up new PeerSendThread
                PeerServerThread serverThread = new PeerServerThread(resp.getPort(), resp.getFileName());
                Thread sThread = new Thread(serverThread);
                serverThreads.put(resp.getFileName(), sThread);
                sThread.start();
                break;
            case REG_FAIL:
                System.out.println("REG FAIL: Either there was an issue with the broker" +
                                   "or you already have a file associated with\n" +
                                   "Sorry you'll have to try again :(\n");
                break;
            case SEARCH_OK:
                System.out.println("SEARCH OK: Successfully found the file!\n" +
                                   "Peer Info: " + resp);
                
                // Spawn up new PeerRcvThread
                try {
                    PeerRcvThread rcvThread = new PeerRcvThread(resp);
                    Thread rThread = new Thread(rcvThread);
                    rThread.start();
                } catch (IOException e) {
                    System.out.println("Problem connecting with the other peer!");
                }
                break;
            case SEARCH_FAIL:
                System.out.println("SEARCH FAIL: No file was found with the file name " +
                                   resp.getFileName() + "\n" +
                                   "Sorry you'll have to try again :(\n");
                break;
            case UNREG_OK:
                System.out.println("UNREG OK: Successfully unregistered the file!\n");
                registeredFiles.remove(resp.getFileName());

                // Close that thread
                Thread closeThread = serverThreads.get(resp.getFileName());
                closeThread.interrupt();
                break;
            case UNREG_FAIL:
                System.out.println("UNREG FAIL: Something went wrong with the broker...\n" +
                                   "Sorry you'll have to try again :(\n");
                break;
            default:
                break;
        }

    }

    private static PeerInfo getInput() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        PeerInfo info;
        breakLabel:
        while (true) {
            System.out.println("What would you like to do?\n" +
                               "Here are your options: \n" +
                               "1) Register a file\n" +
                               "2) Unregister a file\n" +
                               "3) Get a file\n");
            System.out.println("Please input 1, 2, or 3...");
            
            String option = input.readLine();
            switch(option) {
                case "1":
                    info = new PeerInfo(Action.REG);

                    System.out.println("What's the file name?");
                    info.setFileName(input.readLine());

                    System.out.println("What port number to host file on?");
                    info.setPort(Integer.parseInt(input.readLine()));

                    info.setIP(InetAddress.getLocalHost());
                    break breakLabel;
                case "2":
                    info = new PeerInfo(Action.UNREG);
                    System.out.println("What's the file name?");
                    info.setFileName(input.readLine());

                    if (registeredFiles.containsKey(info.getFileName())) {
                        PeerInfo p = registeredFiles.get(info.getFileName());
                        info.setPort(p.getPort());
                        info.setIP(InetAddress.getLocalHost());
                    }
                    else {
                        System.out.println("You have not registered a file with that name.");
                    }
                    break breakLabel;
                case "3":
                    info = new PeerInfo(Action.SEARCH);
                    System.out.println("What file do you want to get?");
                    info.setFileName(input.readLine());
                    break breakLabel;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }

        return info;
    }
    
}