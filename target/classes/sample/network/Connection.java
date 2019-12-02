package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8888;
    private static Connection connection;
    private Socket clientSocket;
    DataOutputStream out;
    DataInputStream in;
    Connection() {
        try {
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getInstance() {
        if (connection == null) {
            connection = new Connection();
        }
        return connection;
    }

    public Socket getClientSocket(){
        return clientSocket;
    }

    public Connection close()
    {
        try
        {
            connection.makeQuery("exit");
            clientSocket.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
        return null;
    }


    public String getResponse()
    {
        try
        {
            String response = in.readUTF();
            return response;
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
        return null;
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
