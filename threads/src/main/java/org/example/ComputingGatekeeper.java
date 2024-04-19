package org.example;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class ComputingGatekeeper implements Runnable{
    volatile PriorityQueue<Long> computing_list;
    volatile Vector<Thread> computingThreads;
    boolean keep_running = Boolean.TRUE;

    private int toCompute = 0;
    private int computed=0;

    @Override
    public void run() {
        while(keep_running)
        {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (computing_list)
            {
                if(toCompute > computed)
                {
                    boolean one_waiting = Boolean.FALSE;
                    for(int i=0;i<computingThreads.size();i++)
                    {
                        if(computingThreads.get(i).getState() == Thread.State.WAITING)
                        {
                            one_waiting = Boolean.TRUE;
                            break;
                        }
                    }
                    if(one_waiting)
                    {
                        computed++;
                        computing_list.notify();
                    }



                }
            }
        }


    }

    public synchronized void incrementToCompute()
    {
        toCompute++;
    }

    public synchronized void stop()
    {
        keep_running = false;
    }
    ComputingGatekeeper(PriorityQueue<Long> computing_list, Vector<Thread>computingThreads)
    {
        this.computingThreads = computingThreads;
        this.computing_list = computing_list;
    }

}
