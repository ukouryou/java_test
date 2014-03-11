/**
 *
 */
package basic.study;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Jan 24, 2014
 * @author andy
 */
public class CounterTask extends RecursiveTask<Integer>{
    private static final long serialVersionUID = -7541056197769206485L;

    private static final int THRESHOLD = 2;
    private int start;
    private int end;

    public CounterTask(int start,int end) {
        this.start = start;
        this.end = end;
    }


    @Override
    protected Integer compute() {
        int sum = 0;

        boolean canCompute = (end -start) <= THRESHOLD;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum +=i;
            }
        } else {
            int middle = (end + start ) / 2;
            CounterTask counterTask = new CounterTask(start, middle);
            CounterTask counterTask2 = new CounterTask(middle+1, end);
            counterTask.fork();
            counterTask2.fork();

            int result = counterTask.join();
            int result2 = counterTask2.join();
            sum = result + result2;

        }


        return sum;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CounterTask counterTask = new CounterTask(1, 4);
        Future<Integer> result = forkJoinPool.submit(counterTask);
        try {
            System.out.println(result.get());
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
