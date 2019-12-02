package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8888;
    private static Connection instance;
    private Socket clientSocket;
    DataOutputStream out;
    Connection() {
        try {
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            out = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    public Socket getClientSocket(){
        return clientSocket;
    }

    public void makeQuery(String query){
        try
        {
            out.writeUTF(query);
            out.flush();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }
}
