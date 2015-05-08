/**
 * 
 */
package memory.analysis;

import java.util.HashSet;

/**
 * Oct 17, 2014
 * @author andy
 */
public class HashSetAnalysis {
	  private static int fSAMPLE_SIZE = 100;
	  private static long fSLEEP_INTERVAL = 100;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		HashSetAnalysis analysis = new HashSetAnalysis();
		analysis.test();
		
	}
	
	public void test() {
		
		 long startMemoryUse = getMemoryUse();
	     
		 //HashSet<Integer> hashset = new HashSet<>();
		 /*for(int i = 0; i < 10; i++) {
			 hashset.add(i);
		 }*/
		 Object1 o1  = new Object1();
		 
		 long endMemoryUse = getMemoryUse();
		 
		 long memoryuse = endMemoryUse - startMemoryUse;
		 log("the size of memory used is :" + memoryuse);
	      
	}
	
	private long getMemoryUse(){
	    putOutTheGarbage();
	    long totalMemory = Runtime.getRuntime().totalMemory();
	    putOutTheGarbage();
	    long freeMemory = Runtime.getRuntime().freeMemory();
	    return (totalMemory - freeMemory);
	  }
	
	private void putOutTheGarbage() {
	    collectGarbage();
	    collectGarbage();
	  }

	  private void collectGarbage() {
	    try {
	      System.gc();
	      Thread.currentThread().sleep(fSLEEP_INTERVAL);
	      System.runFinalization();
	      Thread.currentThread().sleep(fSLEEP_INTERVAL);
	    }
	    catch (InterruptedException ex){
	      ex.printStackTrace();
	    }
	  }
	  
	  private static void log(String aMessage){  
		System.out.println(aMessage);
	}

}
