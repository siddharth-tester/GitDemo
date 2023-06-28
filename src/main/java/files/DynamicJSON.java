package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJSON {

	@Test(dataProvider = "BooksData")
	public void AddBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().log().all().header("Content-Type", "application/json")
				.body(payload.AddBook(isbn, aisle)).when().post("/Library/Addbook.php").then().assertThat()
				.statusCode(200).extract().response().asString();

		JsonPath js = ReusableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println(id);
	}

	@DataProvider(name = "BooksData")
	public Object[][] getData() {

		// array = collection of elements
		// Multi-D array = collection of arrays
		return new Object[][] { { "qwerty", "1212" }, { "uiopo", "3434" }, { "bnmnm", "2321" } };
	}

}
