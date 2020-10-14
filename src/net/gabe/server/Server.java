package net.gabe.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {




    ArrayList<Object> clientOutputStreams;

    public class ClientHandler implements Runnable{
        Socket socket;
        BufferedReader reader;

        public ClientHandler(Socket clientSocket){

            try {
                socket = clientSocket;
                InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            try{
                while((message = reader.readLine()) != null){
                    tellEveryone(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    public static void main(String[] args) {


        //new Server().go();


    }

    public void go() {
        System.out.println("Starting server...");
        clientOutputStreams = new ArrayList<>();
        try{
            ServerSocket serverSocket = new ServerSocket(5000);
            while(true){
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("Connected to client from: "+clientSocket.getLocalAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tellEveryone(String message) {
        System.out.println("Sending packet \""+ message +"\"");
        Iterator it = clientOutputStreams.iterator();
        while(it.hasNext()){
            try{
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
