package basic.study;

public class FinallyTest {
	
	public int inc() {
		int x ;
		try {
			x = 1;
			return x;
		} catch (Exception e) {
			x = 2;
			return x;
		} finally {
			x = 3;
		}
	}

	public static void main(String[] args) {
		
		
		FinallyTest test = new FinallyTest();
		System.out.println(test.inc());

	}

}
