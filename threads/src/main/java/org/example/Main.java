package org.example;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        int THREADS = Integer.parseInt(args[0]);

        Semaphore computing_listSemaphore = new Semaphore(1);
        Semaphore result_listSemaphore = new Semaphore(1);
        Semaphore consoleSemaphore = new Semaphore(1);
        PriorityQueue<Long> computing_list = new PriorityQueue<>();
        String result_list = "wyniki.txt";

        ComputingNode [] nodes = new ComputingNode[THREADS];

        File file = new File(result_list);
        file.delete();
        file.createNewFile();

        for(int i=0;i<THREADS;i++)
        {
            nodes[i] = new ComputingNode(computing_list, result_list, computing_listSemaphore, result_listSemaphore, i, consoleSemaphore);
        }



        int toCompute = 0;


        Vector<Thread>computingThreads = new Vector<>();
        for(int i=0;i<THREADS;i++)
        {

            computingThreads.add( new Thread(nodes[i]));
            computingThreads.get(i).start();
        }

        ComputingGatekeeper load_balancer = new ComputingGatekeeper(computing_list, computingThreads);

        Thread balancerThread = new Thread(load_balancer);
        balancerThread.start();

        Scanner reader = new Scanner(System.in);
        while(true)
        {

            Long input = reader.nextLong();

            if(input==0){
                break;
            }
            try {
                computing_listSemaphore.acquire();
                computing_list.add(input);
                load_balancer.incrementToCompute();
                computing_listSemaphore.release();



            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted");
            }

        }



        for(int i=0;i<THREADS;i++)
        {
            nodes[i].stop();
            if(computingThreads.get(i).getState()== Thread.State.WAITING)
            {
                computingThreads.get(i).interrupt();
            }
        }
        load_balancer.stop();

        while (true)
        {
            boolean allStopped = Boolean.TRUE;
            for(int i=0;i<THREADS;i++)
            {

                if(computingThreads.get(i).getState()== Thread.State.RUNNABLE)
                {
                    allStopped = Boolean.FALSE;
                }
            }
            if(allStopped)
            {
                break;
            }
        }






    }
}