package ConwayGameOfLife;
/*
* @Viktorija-Aryte
 * Description:
 * Master Thread divides the main grid into sub-grids and give them to individuals Threads
 * Waits till they finish work and prepares the grid for the next gen
 * Has to have a barrier so waits till all of the slaveThreads finish work before proceeding
 */

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MasterThread extends Thread
{
    private static final int GRID_SIZE = 10;
    //How many generations should MasterThread perform
    private int amount_gen;
    //The grid before any slaveThread has done any work in this generation
    private Grid current_grid;
    //The grid where slaveThread are working on
    private Grid next_grid;
    //the amount of slave threads
    private int numThread;
    //TODO: Implement the Cyclic Barrier
    private CyclicBarrier barrier;
    //The SlaveThread array
    private final WorkerThread[] slaves;
    //Global work is finished keyword 
    private volatile boolean globalAllDone;
    
    public MasterThread(int generation, int numSlaveThread)
    {
        this.amount_gen = generation;
        this.numThread = numSlaveThread;
        //initailize the Grids
        this.current_grid = new Grid(GRID_SIZE);
        this.next_grid = new Grid(GRID_SIZE);

        //initailize the barrier
        this.barrier = new CyclicBarrier(numThread, () ->
        {
            //one the slaves finish the work, master copies the next grid to
            //a current grid and reuses the slaves again
            current_grid = copyGrid(next_grid);
        });

        //initialize the slave Thread array
        this.slaves = new WorkerThread[numSlaveThread];
        /* The sub-grid division */
        int sub_gridAmount = GRID_SIZE / numThread;

        /* CREATE THE SLAVE THREADS */

        for(int i = 0; i < numThread; i++)
        {
            int startRow = i * sub_gridAmount;
            int endRow = (i == numThread - 1) ? (GRID_SIZE - 1) : startRow + sub_gridAmount;
            slaves[i] = new WorkerThread(startRow, endRow, this, barrier);
        }

        // this.globalAllDone = false;
        initializeGrid();
    }

    @Override
    public void run()
    {
        /* start the slave threads foreach */
        
        try
        {
            for(WorkerThread slave : slaves )
            {
                slave.start();
            }
            //Slaves Threads are now running
            //how many generations, how many times slaves will be reused
            for(int i = 0; i < amount_gen; i++)
            {
            
                /* wait for the slaves to do their work */
                barrier.await();
                System.out.println("Generation " + i + ":");
                //print the grid
                current_grid.print_grid();
            }

        }
        catch(InterruptedException ex)
        {
            return;
        }
        catch(BrokenBarrierException ex)
        {
            return;
        }
    }
    /* Worker methods */
    public int getNeighbours(int col, int row)
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
                else if((row + i >= 0 && row + i <= (GRID_SIZE - 1)) && (col + j >= 0 && col + j <= (GRID_SIZE - 1)))
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

    public Grid get_currentGrid()
    {
        return current_grid;
    }

    public int get_GridSize()
    {
        return GRID_SIZE;
    }

    public int get_cell(int row, int col)
    {
        return current_grid.get_value(row, col);
    }

    public void update_cell(int row, int col, char value)
    {
        next_grid.set_value(row, col, value);
    }

    public boolean timeToShutDown()
    {
        return globalAllDone;
    }

    private Grid copyGrid(Grid copyFrom)
    {
        Grid copy = new Grid(GRID_SIZE);
        for(int i = 0; i < GRID_SIZE; i++)
        {
            for(int j = 0; j < GRID_SIZE; j++)
            {
                char otherValue = copyFrom.get_value(i, j);
                copy.set_value(i, j, otherValue);
            }
        }
        return copy;
    }

    private void initializeGrid()
    {
        for(int i = 0; i < GRID_SIZE; i++)
        {
            for(int j = 0; j < GRID_SIZE; j++)
            {
                char newValue = Math.random() > 0.5 ? 'X' : '-';
                current_grid.set_value(i, j, newValue);
            }
        }
    }


}
