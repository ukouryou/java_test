package java8.lambdaexpressions;


class Hello {
	  public Runnable r = new Runnable() {
	      public void run() {
	        System.out.println(this);
	        System.out.println(toString());
	        System.out.println(Hello.this);
	        System.out.println(Hello.this.toString());
	      }
	    };
	    
	    public Runnable r_lambda = () -> {
	        System.out.println(this);
	        System.out.println(toString());
	      };

	  public String toString() {
	    return "Hello's custom toString()";
	  }
	}

	public class InnerClassExamples2 {
	  public static void main(String... args) {
	    Hello h = new Hello();
	    h.r.run();
	    h.r_lambda.run();
	  }
	}
	
