package ND3;

import java.util.concurrent.CyclicBarrier;

public class MasterThread extends Thread
{
    /* THREADING */
    private final CyclicBarrier barrier;
    private final SlaveThread[] slaves;
    private final int numOfCyckles;
    private final int numOfSlaves;
    // private static final int GRID_SIZE = 10;

    /* GRID LOGIC */
    private int grid_size;
    //SlaveThreads read form this
    private Grid current_grid;
    //SlaveThread write to this
    private Grid next_grid;

    /* FOR SLAVE THREADS */
    private int chuckSize;
    private int startRow;
    private int endRow;

    public MasterThread(int grid_size, int numOfCyckles, int numOfSlaves)
    {
        /* THREADING INIT */
        // this.barrier = barrier;
        this.numOfSlaves = numOfSlaves;
        this.slaves = new SlaveThread[numOfSlaves];
        this.numOfCyckles = numOfCyckles;
        this.grid_size = grid_size;
        this.barrier = new CyclicBarrier(numOfSlaves + 1, () ->
        {
            copyGrid();
            current_grid.print_grid();
            System.out.println("All threads reached the barrier. Proceeding to the next cycle...\n");
        });
    
        /* GRID INIT */
        this.current_grid = new Grid(this.grid_size);
        this.next_grid = new Grid(this.grid_size);

        /* DIVIDE WORK FOR THREADS */
        this.chuckSize = this.grid_size/numOfSlaves;
        //Create the threads but dont start the work yet
        for(int i = 0; i < numOfSlaves; i++)
        {
            startRow = i * chuckSize;
            //if this is the last division just go till the end
            endRow = ( i == (numOfSlaves - 1) ) ? (this.grid_size - 1) : startRow + chuckSize;
            slaves[i] = new SlaveThread(this.barrier, i, startRow, endRow, this);
        }
        initailizeGrid();
    }

    @Override
    public void run()
    {

        try
        {
            for (int i = 0; i < numOfSlaves; i++) {
                // slaves[i] = new SlaveThread(barrier, i);
                //now start the slaves
                slaves[i].start();
            }
            for(int i = 0; i < numOfCyckles; i++)
            {
                System.out.println("\n Master: Strating task cycle" + (i + 1));

                // // Simulate master's work for the cycle
                // System.out.println("Master is performing its task...");
                // Thread.sleep(400); // Simulated work

                //wait at the barrier
                System.out.println("Master is waiting at the barrier for slave cycles to finisg ...");
                barrier.await();

                System.out.println("Master: Completed cycle " + (i + 1));
            }

            System.out.println("Master: Shutting down the slave threads...");
            for(SlaveThread slave : slaves)
            {
                slave.shutdown();
            }
            for (SlaveThread slave : slaves) {
                slave.join();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /* HELPERS */
    public int getNeighbours(int row, int col)
    {
        int neighbours = 0;

        for(int i = -1; i <= 1; i++)
        {
            for(int j = -1; j <= 1; j++)
            {
                //ignore the cell itself
                if(i == 0 && j == 0)
                {
                    continue;
                }
                else if((row + i >= 0 && row + i <= (grid_size - 1)) && (col + j >= 0 && col + j <= (grid_size - 1)))
                {
                    if(current_grid.get_value(row + i, col + j) == 'X')
                    {
                        neighbours++;
                    }

                }
            }
        }

        return neighbours;
    }

    public int get_GRIDSIZE()
    {
        return this.grid_size;
    }

    public char get_cell(int row, int col)
    {
        return current_grid.get_value(row, col);
    }

    public void set_cell(int row, int col, char value)
    {
        next_grid.set_value(row, col, value);
    }

    public void copyGrid()
    {
        for(int i = 0; i < this.grid_size; i++)
        {
            for(int j = 0; j < this.grid_size; j++)
            {
                char otherValue = next_grid.get_value(i, j);
                current_grid.set_value(i, j, otherValue);
            }
        }
    }

    private void initailizeGrid()
    {
        for(int i = 0; i < this.grid_size; i++)
        {
            for(int j = 0; j < this.grid_size; j++)
            {
                char newValue = (Math.random() > 0.5) ? 'X' : '-';
                current_grid.set_value(i, j, newValue);
            }
        }
    }

}
