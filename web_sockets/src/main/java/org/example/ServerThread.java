package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.Semaphore;

public class ServerThread implements Runnable{
    volatile private Vector<String> messages_received;

    Semaphore semaphore;
    final private int number_of_messages;


    final private int my_port;

    @Override
    public void run() {

        try {
            ServerSocket server = new ServerSocket(my_port);
            InetAddress host = InetAddress.getLocalHost();
            System.out.println("New server thread is listening under port "+my_port+".");
            for(int i=0;i<number_of_messages;i++)
            {
                Socket wait = server.accept();
                ObjectInputStream incoming = new ObjectInputStream(wait.getInputStream());
                Message incoming_message = (Message) incoming.readObject();
                System.out.println("Server thread under port "+my_port+" has received message number "+i+".");
                Message response_message = new Message();
                response_message.setNumber(200);
                response_message.setText("received");
                ObjectOutputStream response = new ObjectOutputStream(wait.getOutputStream());
                response.writeObject(response_message);

                if(i==number_of_messages-1)
                {
                    System.out.println("Server thread under port "+my_port+" has received all messages and will be terminated.");
                    Message finish_message = new Message();
                    finish_message.setText("finished");
                    finish_message.setNumber(1);
                    response.writeObject(finish_message);
                    System.out.println("Server thread under port "+my_port+" has transmitted the final message.");
                }
                incoming.close();
                response.close();
                semaphore.acquire();
                messages_received.add(incoming_message.getText());
                semaphore.release();
            }

            server.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("Server thread under port "+my_port+" has been violently stopped!");
        }


    }
    ServerThread(int my_port, int number_of_messages, Vector<String>messages_received, Semaphore semaphore)
    {
        this.my_port=my_port;
        this.number_of_messages=number_of_messages;
        this.messages_received = messages_received;
        this.semaphore = semaphore;
    }


}
