package org.example;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Server {

    static Semaphore semaphore;

    static Vector<String>received_messages;

    static Vector<Thread>server_threads;
    private static final int port = 20;
    private static int next_port = 21;

    InetAddress host = InetAddress.getLocalHost();



    public Server() throws IOException {
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        server_threads=new Vector<>();
        InetAddress host = InetAddress.getLocalHost();
        semaphore = new Semaphore(1);
        received_messages = new Vector<>();

        try
        {
            Socket ready = new Socket(host.getHostName(), port);

            ObjectOutputStream ready_string_stream = new ObjectOutputStream(ready.getOutputStream());
            String ready_message = "ready";
            ready_string_stream.writeObject(ready_message);
            ready_string_stream.close();
            ready.close();
        }
        catch(ConnectException e)
        {

        }



        ServerSocket server = new ServerSocket(port);
        while(true)
        {
            System.out.println("Server delegator process is waiting for a connection.");
            Socket wait = server.accept();
            System.out.println("Server delegator received an initial request, welcoming message is being sent.");
            ObjectOutputStream ready = new ObjectOutputStream(wait.getOutputStream());
            Message ready_message = new Message();
            ready_message.setText("ready");
            ready.writeObject(ready_message);


            ObjectInputStream incoming = new ObjectInputStream(wait.getInputStream());
            Message incoming_message;
            while(true)
            {
                try
                {
                    incoming_message = (Message) incoming.readObject();
                    break;
                }
                catch(EOFException e)
                {
                    continue;
                }
            }
            if(incoming_message.getNumber()==0)
            {
                System.out.println("Server delegator received shutdown command. Threads that are currently serving clients will keep working.");
                break;
            }
            System.out.println("Server received information as to the number of messages that will be sent.");
            Message welcoming_message = new Message();
            welcoming_message.setNumber(next_port);

            welcoming_message.setText("Thank you for connecting");

            ObjectOutputStream response = new ObjectOutputStream(wait.getOutputStream());

            ServerThread new_client_thread= new ServerThread(next_port, incoming_message.getNumber(), received_messages, semaphore);
            server_threads.add(new Thread(new_client_thread));
            server_threads.lastElement().start();
            response.writeObject(welcoming_message);
            response.close();
            incoming.close();
            ready.close();
            response.close();
            wait.close();
            next_port++;
        }
        System.out.println("Complete shutdown of the delegator will now commence.");
        System.out.println("All messages received by the server will be dumped now:");
        for(int i=0;i<received_messages.size();i++)
        {
            System.out.println(received_messages.get(i));
        }
        server.close();
    }
}
