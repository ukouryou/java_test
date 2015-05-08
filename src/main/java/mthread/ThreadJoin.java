package mthread;

public class ThreadJoin {
	
	static class Counter implements Runnable{

		@Override
		public void run() {
			int counter = 0;
			while(100>counter) {
				System.out.println(counter);
				counter++;
			}
		}
		
	}

	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(new Counter());
		t.start();
		System.out.println("wait for counter thread ");
		//comment out the following line to test again
		t.join();
		System.out.println("done");
	}

}
