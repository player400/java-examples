package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String args []) throws IOException, ClassNotFoundException {
        int server_port = 20;

        Scanner scanner = new Scanner(System.in);
        System.out.println("How many messages would you like to transmit?");
        int n = Integer.parseInt(scanner.nextLine());

        InetAddress host = InetAddress.getLocalHost();


        Socket initializing_socket =new Socket(host.getHostName(), server_port);
        System.out.println("Connected to server.");
        ObjectOutputStream initial = new ObjectOutputStream(initializing_socket.getOutputStream());

        ObjectInputStream ready_string_stream = new ObjectInputStream(initializing_socket.getInputStream());

        Message ready_string = (Message) ready_string_stream.readObject();

        System.out.println("Received message from server: "+ready_string.getText());










        Message initial_message = new Message();
        initial_message.setNumber(n);

        initial.writeObject(initial_message);

        if(n==0)
        {
            return;
        }



        ObjectInputStream response = new ObjectInputStream(initializing_socket.getInputStream());

        Message welcoming_message = (Message) response.readObject();
        initializing_socket.close();

        server_port = welcoming_message.getNumber();
        System.out.println("Connected to server thread on port "+server_port+".");


        for(int i=0;i<n;i++){
            Socket transmitting_socket = new Socket(host.getHostName(), server_port);

            ObjectOutputStream transmitting = new ObjectOutputStream(transmitting_socket.getOutputStream());
            System.out.println("Type your next message.");
            String text = scanner.nextLine();
            Message new_message =  new Message();
            new_message.setText(text);
            transmitting.writeObject(new_message);
            System.out.println("Message number "+i+" sent to server on port "+server_port+".");

            ObjectInputStream incoming = new ObjectInputStream(transmitting_socket.getInputStream());
            Message incoming_message = (Message) incoming.readObject();
            System.out.println("Confirmation received from server on port "+server_port+": "+incoming_message.getNumber()+" "+incoming_message.getText()+".");

            if(i==n-1)
            {
                Message finishing_message = (Message) incoming.readObject();
                System.out.println("Final message received: "+finishing_message.getText());
            }
            transmitting.close();
            incoming.close();
            transmitting_socket.close();
        }







    }

}
