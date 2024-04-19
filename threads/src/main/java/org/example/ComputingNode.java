package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.Semaphore;

public class ComputingNode implements Runnable{

    final private int node_number;
    volatile PriorityQueue<Long> computing_list;

    final String result_list;
    Semaphore computing_listSemaphore;
    Semaphore result_listSemaphore;

    Semaphore consoleSemaphore;



    private boolean thread_running = Boolean.TRUE;
    private void fibonacci(Long n)
    {
        Vector<Long>results = new Vector<>();
        for(Long i = Long.valueOf(1); i<n; i++)
        {
            if(n%i==0)
            {
                results.add(i);
            }
        }

        try {
            result_listSemaphore.acquire();

            PrintWriter file = new PrintWriter(new FileWriter(result_list, true));
            file.write(n +":");
            for(Long i=0L;i<results.size();i++)
            {
                file.write(" "+results.get(Math.toIntExact(i)));
                if(i+1 != results.size())
                {
                    file.write(",");
                }
            }
            file.write("\n");
            file.close();

            result_listSemaphore.release();

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run()
    {
        while(thread_running)
        {
            try {
                consoleSemaphore.acquire();
                System.out.println("Computing node number "+node_number+" has finished calculations and will now wait for an assignment.");
                consoleSemaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
           }

            Long number= 0L;
            synchronized (computing_list) {
                try {
                    computing_list.wait();
                } catch (InterruptedException e) {
                    System.out.println("Thread number "+node_number+" interrupted while waiting.");
                    break;
                }

                try {
                    computing_listSemaphore.acquire();
                    number = computing_list.remove();
                    computing_listSemaphore.release();

                    consoleSemaphore.acquire();
                    System.out.println("Computing node "+node_number+" has been assigned a case. Computing of the following data will now commence: "+number);
                    consoleSemaphore.release();
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted, while reading number to compute!");

                } catch (NoSuchElementException e){
                    break;
                }


            }

            fibonacci(number);


        }

    }

    public synchronized void stop()
    {
        thread_running = false;
    }

    ComputingNode(PriorityQueue<Long>computing_list, String result_list, Semaphore computing_listSemaphore, Semaphore result_listSemaphore, int node_number, Semaphore consoleSemaphore)
    {
        this.computing_list = computing_list;
        this.result_list = result_list;
        this.computing_listSemaphore = computing_listSemaphore;
        this.result_listSemaphore = result_listSemaphore;
        this.node_number = node_number;
        this.consoleSemaphore = consoleSemaphore;
    }
}
