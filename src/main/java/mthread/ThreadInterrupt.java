package mthread;

public class ThreadInterrupt {

	static class InterruptThreadTest extends Thread{

		@Override
		public void run() {
			for(int i = 0; i < 4; i++) {
				try {
					System.out.println("thread" + i);
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("interrupt ");
					System.out.println(" loop " + i);
					System.out.println(Thread.interrupted());
				}
			}
		}
		
	}
	
	
	static class InterruptThreadTest1 extends Thread{

		@Override
		public void run() {
			for(int i = 0; i < 100000; i++) {
					System.out.println("thread" + i);
					if(Thread.interrupted()){
						System.out.println("interrupt " + i);
						System.out.println("interrupt " + Thread.interrupted());
						return;
					}
			}
		}
		
	}
	
	static class InterruptThreadTest2 extends Thread{

		@Override
		public void run() {
			for(int i = 0; i < 100000; i++) {
					System.out.println("thread" + i);
					if(Thread.currentThread().isInterrupted()){
						System.out.println("interrupt " + i);
						System.out.println("interrupt " + Thread.currentThread().isInterrupted());
						return;
					}
			}
		}
		
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		InterruptThreadTest t = new InterruptThreadTest(); 
		t.start();
		System.out.println("sleep start");
		Thread.sleep(300);
		System.out.println("interrupt start");
		t.interrupt();
		
		/*InterruptThreadTest1 t1 =  new InterruptThreadTest1();
		t1.start();
		System.out.println("sleep start");
		Thread.sleep(10);
		System.out.println("interrupt start");
		t1.interrupt();*/
		
	}

}
