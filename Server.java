import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class Server implements Runnable {

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;

    public Server(){
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run(){
        try {
            server = new ServerSocket(1234);
            pool = Executors.newCachedThreadPool();
            while(!done){
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add((handler));
                pool.execute(handler);
            }
        } catch (Exception e) {
            shutdown();
        }
    }

    public void broadcast(String message){
        for (ConnectionHandler ch : connections) {
            if (ch != null)
                ch.sendMessage(message);
        }
    }

    public void shutdown(){ 
        try {
            done = true;
            if(server != null && !server.isClosed()){
                server.close();
            }
            for (ConnectionHandler ch : connections) {
                ch.shutdown();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }

    class ConnectionHandler implements Runnable {

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String username;
    
        public ConnectionHandler(Socket client){
            this.client = client;
        }
    
        @Override
        public void run(){
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("Please enter a username: ");
                username = in.readLine();
                // ADD SOME USERNAME CHECKS
    
                System.out.println(username + " connected!");
                broadcast(username + " joined the chat!");
                String message;
                while((message = in.readLine()) != null){
                    if(message.startsWith("/user ")){
                        String[] messageSplit = message.split(" ", 2);
                        if(messageSplit.length == 2){
                            broadcast(username + " renamed themselves to " + messageSplit[1]);
                            System.out.println(username + " renamed themselves to " + messageSplit[1]);
                            username = messageSplit[1];
                            out.println("Successfully changed username to " + username);
                        } else {
                            out.println("No username provided!");
                        }
                    } else if(message.startsWith("/quit")){
                        broadcast(username + " left the chat.");
                        shutdown();
                    } else {
                        broadcast(username + ": " + message);
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }
    
        public void sendMessage(String message){
            out.println(message);
        }
    
        public void shutdown() {
            try {
                in.close();
                out.close();
                if(!client.isClosed()){
                    client.close();
                }
            } catch (IOException e) {
                System.out.println("Something went wrong.");
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
