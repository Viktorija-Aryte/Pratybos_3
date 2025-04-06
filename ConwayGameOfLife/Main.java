package ConwayGameOfLife;
/*
 * @author Viktorija Aryte
 * 
 * Description:
 * - divide the grid into row based division (one thread one row)
 N is the size, 
 cout how long it takes for threads to wait for the barrier
 
 */
public class Main
{
    public static void main(String[] args)
    {
        int generation = 2;
        int numOfThread = 4;

        //create and start the master thread
        MasterThread master = new MasterThread(generation, numOfThread);
        master.start();
        try
        {
            master.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
}
