package com.RESTAPI;

import java.io.FileNotFoundException;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;

import org.testng.ITestContext;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import org.testng.annotations.Test;

import com.endpoints.UserProfile;
import com.payloads.UserProfilePOJO;
import com.uitil.ConvertJsonToString;
import com.uitil.Sparkreport;

import io.restassured.RestAssured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserProfileTest extends SetUp_TearDown {

	RequestSpecification request;
	public static UserProfilePOJO userprofile;

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("resource", "users");
		parameters.put("username", "vrushabhmanatkar13");
		request = requestSpecification(properties.getProperty("token"), parameters);
		userprofile = new UserProfilePOJO();
	}

	@Test(priority = 1)

	public void getUserProfile() throws FileNotFoundException {

		PrintStream profile = printStream(System.getProperty("user.dir") + "\\LogReport\\USER_PROFILE.txt");
		Response responce = UserProfile.getUserProfile(request, profile);

		logRequest(request, Sparkreport.test);
		logResponce(responce, Sparkreport.test);

		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");
		Assert.assertEquals(responce.getStatusCode(), 200);

		JsonPath js = ConvertJsonToString.convertJsonToString(responce);

		userprofile.setLogin(js.getString("login"));
		userprofile.setId(js.getInt("id"));
		userprofile.setNode_id(js.getString("node_id"));

		Assert.assertEquals(userprofile.getLogin(), "vrushabhmanatkar13");
		Assert.assertFalse(userprofile.getId() == 0);
		Assert.assertEquals(js.get("bio"), "QA & Automation Engineer ");
		Assert.assertEquals(js.get("created_at"), "2022-07-27T09:59:15Z");
		Assert.assertEquals(js.get("name"), "Vrushabh_Manatkar");
		Assert.assertEquals(js.getString("url"), RestAssured.baseURI + "users/" + userprofile.getLogin());

		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");
		

	}

}
