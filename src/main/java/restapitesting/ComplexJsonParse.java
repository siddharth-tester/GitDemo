package restapitesting;

import org.testng.Assert;

import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JsonPath js = new JsonPath(payload.CoursePrice());

		// Print number of courses returned by API

		int count = js.getInt("courses.size()");
		System.out.println(count);

		// Print purchased amount
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(totalAmount);

//		1. Print No of courses returned by API
//
//		2.Print Purchase Amount
//
//		3. Print Title of the first course
//
//		4. Print All course titles and their respective Prices
//
//		5. Print no of copies sold by RPA Course
//
//		6. Verify if Sum of all Course prices matches with Purchase Amount

		String firstCourseTitle = js.get("courses[0].title");
		System.out.println(firstCourseTitle);

		String coursetitle;
		int price;
		for (int i = 0; i < count; i++) {
			coursetitle = js.get("courses[" + i + "].title");
			price = js.getInt("courses[" + i + "].price");
			System.out.println("Course Title = " + coursetitle + "||" + "Price = " + price);

		}

		int rpaCopies;
		for (int i = 0; i < count; i++) {
			coursetitle = js.get("courses[" + i + "].title");
			if (coursetitle.equalsIgnoreCase("RPA")) {
				rpaCopies = js.getInt("courses[" + i + "].copies");
				System.out.println("RPA copies = " + rpaCopies);
				break;
			}
		}

		System.out.println("============Tricky One===============");

		int sumOfCoursePrices = 0;
		int numberOfCopies;
		int total = 0;
		for (int i = 0; i < count; i++) {
			price = js.getInt("courses[" + i + "].price");
			numberOfCopies = js.getInt("courses[" + i + "].copies");
			sumOfCoursePrices = (price * numberOfCopies);
			total += sumOfCoursePrices;
		}

		System.out.println(total);
		int purchasedAmt = js.getInt("dashboard.purchaseAmount");
		Assert.assertEquals(total, purchasedAmt);

	}

}
