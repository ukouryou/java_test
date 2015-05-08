/**
 *
 */
package java8.lambdaexpressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import java8.lambdaexpressions.Person.Sex;

/**
 * Oct 11, 2014
 * @author andy
 */
public class PersonChecker {

    public static void printPersons(
            List<Person> roster, CheckPerson tester) {
            for (Person p : roster) {
                if (tester.test(p)) {
                    p.printPerson();
                }
            }
        }
    
    public static void printPersonsWithPredicate(
    	    List<Person> roster, Predicate<Person> tester) {
    	    for (Person p : roster) {
    	        if (tester.test(p)) {
    	            p.printPerson();
    	        }
    	    }
    	}
    
    public static void processPersons(
    	    List<Person> roster,
    	    Predicate<Person> tester,
    	    Consumer<Person> block) {
    	        for (Person p : roster) {
    	            if (tester.test(p)) {
    	                block.accept(p);
    	            }
    	        }
    	}

    public static void processPersonsWithFunction(
    	    List<Person> roster,
    	    Predicate<Person> tester,
    	    Function<Person, String> mapper,
    	    Consumer<String> block) {
    	    for (Person p : roster) {
    	        if (tester.test(p)) {
    	            String data = mapper.apply(p);
    	            block.accept(data);
    	        }
    	    }
    	}
    
    public static <X, Y> void processElements(
    	    Iterable<X> source,
    	    Predicate<X> tester,
    	    Function <X, Y> mapper,
    	    Consumer<Y> block) {
    	    for (X p : source) {
    	        if (tester.test(p)) {
    	            Y data = mapper.apply(p);
    	            block.accept(data);
    	        }
    	    }
    	}

    public static <T, SOURCE extends Collection<T>, DEST extends Collection<T>>
    DEST transferElements(
        SOURCE sourceCollection,
        Supplier<DEST> collectionFactory) {
        
        DEST result = collectionFactory.get();
        for (T t : sourceCollection) {
            result.add(t);
        }
        return result;
}
    

    interface CheckPerson {
        boolean test(Person p);
    }


    //Approach 4: Specify Search Criteria Code in an Anonymous Class
	public void t1(List<Person> roster) {
		 printPersons(
	                roster,
	                new CheckPerson() {
	                    public boolean test(Person p) {
	                        return p.getGender() == Person.Sex.MALE
	                            && p.getAge() >= 18
	                            && p.getAge() <= 25;
	                    }
	                }
	            );
	}

	//Specify Search Criteria Code with a Lambda Expression
	public void t2(List<Person> roster) {
        printPersons(
                roster,
                (Person p) -> p.getGender() == Person.Sex.MALE
                && p.getAge() >= 18 &&
                p.getAge() <= 25
            );
	}

	//Use Standard Functional Interfaces with Lambda Expressions
	public void t3(List<Person> roster) {
		printPersonsWithPredicate(
				roster,
				p -> p.getGender() == Person.Sex.MALE
				&& p.getAge() >= 18
				&& p.getAge() <= 25
				);

	}

	// Use Lambda Expressions Throughout Your Application
	public static void t4(List<Person> roster) {
		processPersons(
			     roster,
			     p -> p.getGender() == Person.Sex.MALE
			         && p.getAge() >= 18
			         && p.getAge() <= 25,
			     p -> p.printPerson()
			);
	}

	public static void t5(List<Person> roster) {
		processPersonsWithFunction(
			    roster,
			    p -> p.getGender() == Person.Sex.MALE
			        && p.getAge() >= 18
			        && p.getAge() <= 25,
			    p -> p.getEmailAddress(),
			    email -> System.out.println(email)
			);
	}

	//Use Generics More Extensively
	public static void t6(List<Person> roster) {
		processElements(
			    roster,
			    p -> p.getGender() == Person.Sex.MALE
			        && p.getAge() >= 18
			        && p.getAge() <= 25,
			    p -> p.getEmailAddress(),
			    email -> System.out.println(email)
			);
	}
	
	//Use Aggregate Operations That Accept Lambda Expressions as Parameters
	public static void t7(List<Person> roster) {
		roster
	    .stream()
	    .filter(
	        p -> p.getGender() == Person.Sex.MALE
	            && p.getAge() >= 18
	            && p.getAge() <= 25)
	    .map(p -> p.getEmailAddress())
//	    .collect(Collectors.toList())
	    .forEach(email -> System.out.println(email));
	}
    
    public static void main(String[] args) {
        List<Person> roster = new ArrayList<Person>();
        Person person = new Person();
        person.setGender(Sex.MALE);
        person.setAge(20);
        person.setName("a ha!!!");
        person.setEmailAddress("1@t");
        roster.add(person);
        
        Person person1 = new Person();
        person1.setGender(Sex.MALE);
        person1.setAge(29);
        person1.setName("a hey!!!");
        person1.setEmailAddress("2@t");
        roster.add(person1);
        
        t7(roster);
       
        
        //part 2
        Set<Person> rosterSetLambda =
        	    transferElements(roster, () -> { return new HashSet<>(); });
        Set<Person> rosterSet = transferElements(roster, HashSet::new);

        /*File dir = new File("/an/dir/");
        FileFilter directoryFilter = (File f) -> f.isDirectory();
        File[] dirs = dir.listFiles(directoryFilter);


        List<String> names = new ArrayList<>();
        names.add("TaoBao");
        names.add("ZhiFuBao");

        List<String> lowercaseNames = names.stream().map((name) -> {return name.toLowerCase();}).collect(Collectors.toList());*/

        
        

    }

}
