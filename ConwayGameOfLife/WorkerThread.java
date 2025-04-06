package ConwayGameOfLife;
/*
 * @author Viktorija Aryte
 * Description:
 * - Sub 
 * - We have a current grid and the working grid, threads work on the currentgrid
 * and update the working grid, once threads reach barrier the working thread becomes current thread (a.k.a grid for next gen)
 * - (read from currentGrid, write to nextGrid)
 * - barrier (choose a type) is used so all threads of current gen finish work, before next gen starts
 * 
 * RULES(@link https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life):
 * Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent. 
 * At each step in time, the following transitions occur:
 * - Any live cell with fewer than two live neighbours dies, as if by underpopulation.
 * - Any live cell with two or three live neighbours lives on to the next generation.
 * - Any live cell with more than three live neighbours dies, as if by overpopulation.
 * - Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
 */

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/* SLAVE THREADS */
public class WorkerThread extends Thread
{
    private int row_start;
    private int row_end;
    //TODO: implement CyclicBarrier
    private CyclicBarrier barrier;
    private MasterThread masterThread;
    private int col;

    public WorkerThread(int set_row_start, int set_row_end, MasterThread masterThreadRef, CyclicBarrier barrierRef)
    {
        this.row_start = set_row_start;
        this.row_end = set_row_end;
        this.barrier = barrierRef;
        this.masterThread = masterThreadRef;
        this.col = masterThread.get_GridSize();
    }

    @Override
    public void run()
    {
        char new_value;
        int neighbours;

        //while shutdown was not called dy the MaterThread
        while(!Thread.currentThread().isInterrupted())
        {
            for(int i = row_start; i <= row_end; i++)
            {
                for(int j = 0; j < col; j++)
                {
                    neighbours = masterThread.getNeighbours(i, j);
    
                    //if the cell is alive
                    if(masterThread.get_cell(i, j) == 'X')
                    {
                        //if not 2 or 3 neigtbours cell dies
                        new_value = (neighbours == 2 || neighbours == 3) ? 'X' : '-';
                        //WRITE NEW VALUE TO NEXTGRID
                        masterThread.update_cell(i, j, new_value);
    
                    }
                    if(masterThread.get_cell(i, j) == '-')
                    {
                        new_value = (neighbours == 3) ? 'X' : '-';
                        //WRITE NEW VALUE TO NEXTGRID
                        masterThread.update_cell(i, j, new_value);
                    }
                }
            }
            //once the thread does its work wait for the others to finish
            try
            {
                barrier.await();
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
    }
}
