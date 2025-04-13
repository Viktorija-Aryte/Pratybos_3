package ND3;

public class Main
{
    private static final int NUM_SLAVES = 12;
    private static final int NUM_TASK_CYCLES = 100;
    private static final int GRID_SIZE = 2004;

    public static void main(String[] args)
    {
        MasterThread master = new MasterThread(GRID_SIZE, NUM_TASK_CYCLES, NUM_SLAVES);
        //Start time 
        long startTime = System.currentTimeMillis();
        master.start();
        try
        {
            master.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("ExecutionTime: " + executionTime + " ms");
        // System.out.println("MASTER: all task are complete");
    }
}
