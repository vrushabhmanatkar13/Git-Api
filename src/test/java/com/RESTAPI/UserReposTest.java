package com.RESTAPI;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.testng.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.endpoints.UserRepos;
import com.payloads.CommiterInfo;
import com.payloads.CreateRepoPOJO;
import com.payloads.UserProfilePOJO;
import com.uitil.BaseClass;
import com.uitil.ConvertJsonToString;
import com.uitil.Sparkreport;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserReposTest extends SetUp_TearDown {

	RequestSpecification request;
	public static String reponame;
	public static String username;
	public static int id;
	public static String node_id;
	
	public CreateRepoPOJO repo;
	public UserProfilePOJO userprofile;

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {

		request = requestSpecification(properties.getProperty("token"));
		
		 repo = new CreateRepoPOJO();
		this.userprofile=UserProfileTest.userprofile;
		
	}

	@Test(priority = 1)
	public void createRepo() throws Exception {

		repo.setName("ABC");
		repo.setDescription("New Repo");
		repo.setHomepage("https://github.com");
		repo.setHas_issues(true);
		repo.setHas_projects(true);
		repo.setHas_wiki(true);
		

		String path = "user/repos";
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\CREATE_REPO.txt");

		Response responce = UserRepos.createRepo(request, repo, path, loginfo);
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);

		reponame = js.getString("name");
		username = js.getString("owner.login");
		id = js.getInt("id");
		node_id = js.getString("node_id");

		logRequest(request, Sparkreport.test);
		logRequestBody(objectMapper(repo), Sparkreport.test);
		logResponce(responce, Sparkreport.test);
		
		
		Assert.assertEquals(responce.getStatusCode(), 201);
		Assert.assertFalse(id == 0);
		Assert.assertFalse(node_id.isBlank());
		Assert.assertEquals(js.getString("name"), repo.getName());
		Assert.assertEquals(js.getString("full_name"), userprofile.getLogin() + "/" + repo.getName());
		Assert.assertFalse(js.getBoolean("private"));
		Assert.assertEquals(js.getString("description"), repo.getDescription());
		Assert.assertTrue(js.getBoolean("has_issues"));
		Assert.assertTrue(js.getBoolean("has_projects"));
		Assert.assertTrue(js.getBoolean("has_wiki"));

		Assert.assertEquals(js.getString("owner.login"), userprofile.getLogin());
		Assert.assertEquals(js.getInt("owner.id"), userprofile.getId());
		Assert.assertEquals(js.getString("owner.node_id"), userprofile.getNode_id());

		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");

	}

	@Test(priority = 2)
	public void getAllRepo() throws FileNotFoundException {
		
		String path = "users/vrushabhmanatkar13/repos";
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\GET_ALLREPO");
		Response responce = UserRepos.getAllRepos(request, path, loginfo);
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		logRequest(request, Sparkreport.test);
		logResponce(responce, Sparkreport.test);

		// for get all size of array which having no object key
		// if array have object key then use (key[].size())
		JSONArray array = new JSONArray(responce.getBody().asString());
		for (int i = 0; i <= array.length(); i++) {
			String name = js.get("[" + i + "].name");
			if (name.equals(repo.getName())) {
				Assert.assertEquals(js.getString("[" + i + "].name"), repo.getName());
				Assert.assertEquals(js.getString("[" + i + "].full_name"),
						userprofile.getLogin() + "/" + repo.getName());
				Assert.assertEquals(js.getString("[" + i + "].description"), repo.getDescription());
				break;
			}
		}
		Assert.assertEquals(responce.getStatusCode(), 200);
		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");

	}

	@Test(priority = 3)
	public void getUserRepo() throws FileNotFoundException {
	

		String path = "repos/vrushabhmanatkar13/" + repo.getName();
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\GET_REPO.txt");
		Response responce = UserRepos.getUserRepos(request, path, loginfo);
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);

		logRequest(request, Sparkreport.test);
		logResponce(responce, Sparkreport.test);

		Assert.assertEquals(responce.getStatusCode(), 200);
		Assert.assertEquals(js.getInt("id"), id);
		Assert.assertEquals(js.getString("node_id"), node_id);
		Assert.assertEquals(js.getString("name"), repo.getName());
		Assert.assertEquals(js.getString("full_name"), userprofile.getLogin() + "/" + repo.getName());
		Assert.assertEquals(js.getString("description"), repo.getDescription());

		Assert.assertEquals(js.getString("owner.login"), userprofile.getLogin());
		Assert.assertEquals(js.getInt("owner.id"), userprofile.getId());
		Assert.assertEquals(js.getString("owner.node_id"), userprofile.getNode_id());

		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");

	}

}
