package java8.lambdaexpressions;

public class TestClass {

	public static void main(String[] args) {
//		Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
		Converter<String, Integer> converter = Integer::valueOf;
		Integer converted = converter.convert("123");
		System.out.println(converted);    // 123
	}
}
