package restapitesting;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import files.ReusableMethods;
import files.payload;

public class Basics {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// validate if Add Place API is working as expected

		// GIVEN - all input details
		// WHEN - Submit the API
		// THEN - Validate the response

		RestAssured.baseURI = "https://rahulshettyacademy.com";
//		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
//				.body(payload.AddPlace()).when().post("maps/api/place/add/json").then().assertThat().statusCode(200)
//				.body("scope", equalTo("APP")).header("server", equalTo("Apache/2.4.41 (Ubuntu)")).extract().response()
//				.asString();
		
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(new String(Files.readAllBytes(Paths.get("./src/main/java/files/addPlace.json")))).when().post("maps/api/place/add/json").then().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("server", equalTo("Apache/2.4.41 (Ubuntu)")).extract().response()
				.asString();

		System.out.println(response);

		// How to convert a String to JSON (parsing JSON)
		JsonPath js = new JsonPath(response);
		String placeId = js.getString("place_id");
		System.out.println(placeId);
		System.out.println("=================================================================");
		System.out.println("=======UPDATE CALL===============");
		// To update place

		String newAddress = "Summer Walk, Africa";

		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body("{\r\n" + "\"place_id\":\"" + placeId + "\",\r\n" + "\"address\":\"" + newAddress + "\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n" + "}\r\n" + "")
				.when().put("maps/api/place/update/json").then().assertThat().log().all().statusCode(200)
				.body("msg", equalTo("Address successfully updated"));
		
		System.out.println("=================================================================");
		System.out.println("=======GET CALL===============");
		System.out.println("postJira");
		System.out.println("postJira1");

		// To GET place

		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
				.when().get("maps/api/place/get/json").then().assertThat().log().all().statusCode(200).extract()
				.response().asString();

		JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress, newAddress);
	}

}
