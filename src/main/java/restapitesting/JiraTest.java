/**
 * 
 */
package restapitesting;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

import java.io.File;

import org.testng.Assert;

/**
 * @author SiddharthGupta
 *
 */
public class JiraTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RestAssured.baseURI = "http://localhost:8080/";
	

		// login API
//		String loginResponse = given().log().all().header("Content-Type", "application/json")
//				.body("{ \"username\": \"guptasiddharth\", \"password\": \"Panchkula@2023\" }").log().all().when()
//				.post("rest/auth/1/session").then().extract().response().asString();
//
//		JsonPath js = new JsonPath(loginResponse);
//		String sessionValue = js.getString("session.value");
//		System.out.println(sessionValue);

//		given().pathParam("id", "10100").header("Content-Type", "application/json")
//				.header("Cookie", "JSESSIONID=" + sessionValue)
//				.body("{\r\n" + "    \"body\": \"This is my first comment.\",\r\n" + "    \"visibility\": {\r\n"
//						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
//						+ "}")
//				.when().post("/rest/api/2/issue/{id}/comment").then().log().all().assertThat().statusCode(201);

		// **************************************Using session
		// filter***********************************

		SessionFilter session = new SessionFilter();

		String loginResponse = given().log().all().header("Content-Type", "application/json")
				.body("{ \"username\": \"guptasiddharth\", \"password\": \"Panchkula@2023\" }").log().all()
				.filter(session).when().post("rest/auth/1/session").then().extract().response().asString();
		
		String expectedMessage = "This is my first comment.";

		String commentResponse = given().pathParam("id", "10100").header("Content-Type", "application/json")
				.body("{\r\n" + "    \"body\": \""+expectedMessage+"\",\r\n" + "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
						+ "}")
				.filter(session).when().post("/rest/api/2/issue/{id}/comment").then().extract().asString();

		JsonPath js1 = new JsonPath(commentResponse);
		String commentId = js1.getString("id");
		System.out.println("Comment id: " +commentId);
		
		// ********** to add a screenshot********************

//		given().pathParam("id", "10100").header("X-Atlassian-Token", "no-check")
//				.header("Content-Type", "multipart/form-data")
//				.multiPart("file", new File("./src/main/java/files/jira.text")).filter(session).when()
//				.post("/rest/api/2/issue/{id}/attachments").then().log().all().assertThat().statusCode(200);

		// ********* to get all the details for a bug ticket *************

//		String issueDetails = given().filter(session).pathParam("id", "10100").queryParam("fields", "comment").when()
//				.get("/rest/api/2/issue/{id}").then().log().all().extract().toString();
//		
		String issueDetails = given().filter(session).pathParam("id", "10100").queryParam("fields", "comment").log().all().when()
				.get("/rest/api/2/issue/{id}").then().log().all().extract().response().asString();

//		System.out.println(issueDetails);
		
		JsonPath js = new JsonPath(issueDetails);
		int commentsCount = js.getInt("fields.comment.comments.size()");
		System.out.println("Total count of comments= " + commentsCount);
		for (int i = 0; i < commentsCount; i++) {
			String commentIdIssue = js.get("fields.comment.comments["+i+"].id").toString();
			if (commentIdIssue.equalsIgnoreCase(commentId)) {
				String message = js.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expectedMessage);
				
			}
		}
	}

}
