package ND3;

public class Main
{
    private static final int NUM_SLAVES = 3;
    private static final int NUM_TASK_CYCLES = 5;
    private static final int GRID_SIZE = 10;

    public static void main(String[] args)
    {
        MasterThread master = new MasterThread(GRID_SIZE, NUM_TASK_CYCLES, NUM_SLAVES);
        master.start();

        try
        {
            master.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        System.out.println("MASTER: all task are complete");
    }
}
