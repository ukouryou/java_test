/**
 *
 */
package java8.lambdaexpressions;

import org.elasticsearch.common.joda.time.LocalDate;

/**
 * Oct 11, 2014
 * @author andy
 */
public class Person {
    public enum Sex {
        MALE, FEMALE
    }

    private int age;
    String name;
    LocalDate birthday;
    Sex gender;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Sex getGender() {
        return gender;
    }

    public void setGender(Sex gender) {
        this.gender = gender;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    String emailAddress;

    public int getAge() {
        // ...
        return age;
    }
    
    public void setAge(int age) {
		this.age = age;
	}

	public void printPerson() {
		System.out.print(name);
		System.out.println("p");
        // ...
    }

}
