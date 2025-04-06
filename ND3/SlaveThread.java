package ND3;

import java.util.concurrent.CyclicBarrier;

public class SlaveThread extends Thread
{
    /* THREADING */
    private final int slaveID;
    private final CyclicBarrier barrier;
    //for a shutdown of the Threads
    private volatile boolean running = true;

    /* FOR CELL CALCULATIONS */
    //reference to the master thread to access the grids
    private MasterThread master;
    private int startRow;
    private int endRow;
    public SlaveThread(CyclicBarrier barrier, int slaveID, int startRow, int endRow, MasterThread ref_master)
    {
        this.barrier = barrier;
        this.slaveID = slaveID;
        this.startRow = startRow;
        this.endRow = endRow;
        this.master = ref_master;
    }

    @Override
    public void run()
    {
        try
        {
            while(running)
            {
                //Simulate task
                System.out.println("Slave " + slaveID + " is performing a task...");

                /* SLAVE DOES CALULATIONS FOR THE ASSIGNED ROWS */

                //for assigned rows
                for(int i = startRow; i < endRow; i++)
                {
                    //for every column
                    for(int j = 0; j < master.get_GRIDSIZE(); j++)
                    {
                        int neighbours = master.getNeighbours(i, j);
                        //if the cell is alive
                        if(master.get_cell(i, j) == 'X')
                        {
                            //if not 2 or 3 neigtbours cell dies
                            char new_value = (neighbours == 2 || neighbours == 3) ? 'X' : '-';
                            //WRITE NEW VALUE TO NEXTGRID
                            master.set_cell(i, j, new_value);

                        }
                        if(master.get_cell(i, j) == '-')
                        {
                            char new_value = (neighbours == 3) ? 'X' : '-';
                            //WRITE NEW VALUE TO NEXTGRID
                            master.set_cell(i, j, new_value);
                        }
                    }
                }

                // Thread.sleep((long) (Math.random() * 100));
                
                //wait at the barrier
                System.out.println("Slave " + slaveID + " is waiting at the barrier");
                barrier.await();
            }
        }
        catch(Exception e)
        {
            if(running)
            {
                e.printStackTrace();
            }
        }
    }

    public void shutdown()
    {
        running = false;
        this.interrupt();
    }
}
