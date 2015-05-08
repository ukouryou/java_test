package java8.basic.threading.jmm;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReorderTest1 {
	private int a = 0;
	private int b = 0;

	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		ReorderTest1 rt = new ReorderTest1();
		rt.doTest();
	}

	public void doTest() throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newFixedThreadPool(2);

		for (int i = 1; i < 1000000; i++) {
			CountDownLatch latch = new CountDownLatch(1);
			Future futureA = executor.submit(new ThreadATask(latch));
			Future futureB = executor.submit(new ThreadBTask(latch));
			latch.countDown();
			int x = (int) futureB.get();
			int y = (int) futureA.get();
			if (x == y) {
				System.out.println("x=" + x + " : " + "y=" + y);
				System.out.println("reorder in " + i);
				return;
			}

			// 重置状态
			a = 0;
			b = 0;
		}
		// 在此关闭线程池
	}

	private class ThreadATask implements Callable {
		private CountDownLatch barrier;

		public ThreadATask(CountDownLatch barrier) {
			this.barrier = barrier;
		}

		public Integer call() throws InterruptedException,
				BrokenBarrierException {
			barrier.await();

			a = 1; // A1
			return b; // A2
		}
	}

	private class ThreadBTask implements Callable {
		private CountDownLatch barrier;

		public ThreadBTask(CountDownLatch barrier) {
			this.barrier = barrier;
		}

		public Integer call() throws InterruptedException,
				BrokenBarrierException {
			barrier.await();

			b = 2; // B1
			return a; // B2
		}
	}
}
