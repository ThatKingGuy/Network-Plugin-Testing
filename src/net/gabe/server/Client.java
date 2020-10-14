package net.gabe.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Client {

    JTextArea incoming;
    JTextField outgoing;
    PrintWriter writer;
    Socket socket;
    BufferedReader reader;

    public static void main(String[] args) {
        
        //new Client().go();

    }

    public void go(){
        JFrame frame = new JFrame("Chat Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        panel.add(qScroller);
        panel.add(outgoing);
        panel.add(sendButton);
        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setSize(800, 500);
        frame.setVisible(true);
    }

    private void setUpNetworking() {
        try{
            socket = new Socket("127.0.0.1", 5000);
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(socket.getOutputStream());
            System.out.println("Started Networking");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SendButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                writer.println("BOB,"+outgoing.getText());
                writer.flush();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            String message;
            try{

                while((message = reader.readLine()) != null){
                    System.out.println("Sending packet \""+ message +"\"");
                    incoming.append(formatMessage(message));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String formatMessage(String message){
        String[] data = new String[2];
        data[0] = message.split(",")[0];
        data[1] = message.split(",")[1];
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return "["+dtf.format(now)+" INFO]: <"+data[0]+"> "+data[1]+"\n";
    }


}
