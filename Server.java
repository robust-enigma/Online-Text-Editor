import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    static Vector<ClientHandler> ClientsVec = new Vector<>();

    static int nClients = 0; //numbers of clients connected to server

    @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9999);
        System.out.println("The Server is waiting for the client...");

        // running infinite loop for getting
        // client request
        while (true) {
                // socket object to receive incoming client requests
                Socket SocServer = server.accept();
                System.out.println("Start Connection");

                System.out.println("The client with port number = " + SocServer.getPort() + " Added to the server");

                // obtaining input and out streams
                System.out.println("Waiting for the ID of the Client");

                ObjectInputStream ServerInput = new ObjectInputStream(SocServer.getInputStream());
                ObjectOutputStream ServerOutput = new ObjectOutputStream(SocServer.getOutputStream());

                //BufferedReader ClientMessage = new BufferedReader(new InputStreamReader(ServerInput));
                String id = ServerInput.readUTF();

                System.out.println("Client ID : " + id);
                ClientHandler ClientsHand = new ClientHandler(SocServer, id, ServerInput, ServerOutput);  //to handle multiple clients connected to server
			
                // Create a new Thread with this object.
                Thread thread = new Thread(ClientsHand); //for each client one thread

                System.out.println("Adding this client to active client list");

                // add this client to active clients list
                ClientsVec.add(ClientsHand);

                // start the thread.
                thread.start();

                nClients++;

        }
    }
}

class ClientHandler implements Runnable { //Runnable is an interface that is to be implemented by a class whose instances are intended to be executed by a thread
    static Vector<ClientHandler> NEWClientsVec = new Vector<>();
    final ObjectInputStream input;
    final ObjectOutputStream output;
    DataOutputStream dout;
    public String ID;
    Socket s;

    // Constructor
    public ClientHandler(Socket s, String ID, ObjectInputStream input, ObjectOutputStream output) {
        this.ID = ID;
        this.s = s;
        this.input = input;
        this.output = output;
        
        try {
	  dout = new DataOutputStream(s.getOutputStream());
	  } catch (IOException e) {
	  // TODO Auto-generated catch block
	e.printStackTrace();
}
    }

    public void run() {

        try 
        {
            while (true) {
                System.out.println("The Client Sent this Text to share..");

                // receive "share"
                String received = input.readUTF();

                if (received.equals("Share")) {
                    // receive the IDs
                    received = input.readUTF();
                    NEWClientsVec.clear(); //the clients to which data is intented to share

                    String[] IDs = received.split("\\-"); //suppose the ids passed have // then demarcation is done based on this
                    for (String s : IDs) {
                        for (ClientHandler mc : Server.ClientsVec) { //ClientsVec is all the active clients connected to server including to sender 
                            if (s.equals(mc.ID) && !mc.ID.equals(this.ID)) { //sender cannot send it to itself so, that and condition
                                NEWClientsVec.add(mc);
                                break;
                            }
                        }
                        // when the the other clients want to edit show this at the parent
                        NEWClientsVec.add(this);
                    }
                }
                else if(received.equals("Upload"))
                {
                    try{
                    String rcv = input.readUTF();
                    System.out.println(rcv);
                    File fi = new File("E:\\server\\" + rcv);
                    rcv = input.readUTF();

                    System.out.print(rcv);// Create a file writer that doesn't append
                    FileWriter wr = new FileWriter(fi, false); //false so, that if u select a file that already has text, new text will overwrite previous text
                    //if true, new text will be appended to old tex
                    // Create buffered writer to write
                    BufferedWriter w = new BufferedWriter(wr); //writing the content in desired location

                    // Write
                    w.write(rcv);
                    w.flush();
                    w.close();
                    }
                    catch(Exception e)
                    {
                    e.printStackTrace();
                    }
 
 }
                
                else{
                    received = input.readUTF(); //taking the input text
                    for (ClientHandler mc : NEWClientsVec){ //reflect the text to every shared client
                        if (!ID.equals(mc.ID)) { //sender cannot send text to itself, so that if condition
                            mc.output.writeUTF(received);
                            mc.output.flush(); //clear the buffer after every operation
                        }
                    }
                }

    }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            e.getMessage();
        }

    }
}