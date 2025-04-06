import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    private static final ExecutorService executor = Executors.newFixedThreadPool(5);
    private static final CyclicBarrier barrier = new CyclicBarrier(5); //4 slaves + 1 master

    public static void main(String[] args) throws InterruptedException {
        Runnable master = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println("Starting slaves");
                        for (int i = 100; i < 500; i += 100) {
                            executor.submit(getRunnable(i));
                        }
                        barrier.await();
                        System.out.println("All slaves done");
                    }
                } catch (InterruptedException | BrokenBarrierException ex) {
                    System.out.println("Bye Bye");
                }
            }
        };

        executor.submit(master);
        Thread.sleep(2000);
        executor.shutdownNow();

    }

    public static Runnable getRunnable(final int sleepTime) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Entering thread " + Thread.currentThread() + " for " + sleepTime + " ms.");
                    Thread.sleep(sleepTime);
                    System.out.println("Exiting thread " + Thread.currentThread());
                    barrier.await();
                } catch (BrokenBarrierException | InterruptedException ex) {
                }
            }
        };

    }
}
