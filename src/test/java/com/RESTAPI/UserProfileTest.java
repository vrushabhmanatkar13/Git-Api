package com.RESTAPI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.logging.LogRecord;

import org.json.JSONArray;
import org.testng.Assert;

import org.testng.ITestContext;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import org.testng.annotations.Test;

import com.endpoints.CreateFile;
import com.endpoints.UserProfile;
import com.endpoints.UserRepos;

import com.payloads.CommiterInfo;
import com.payloads.CreateRepoPOJO;
import com.payloads.DeleteFilePOJO;
import com.payloads.FileContentPOJO;
import com.payloads.FileUpdatePOJO;
import com.payloads.UserProfilePOJO;
import com.uitil.BaseClass;
import com.uitil.ConvertJsonToString;
import com.uitil.Sparkreport;

import io.restassured.RestAssured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserProfileTest extends BaseClass{

	
	public static Sparkreport report;

	public static String reponame;
	public static String username;
	public static int id;
	public static String node_id;

	public static Properties properties;
	public RequestSpecification request;
	UserProfilePOJO userprofile;
	CreateRepoPOJO repo;
	FileContentPOJO filecontent;
	CommiterInfo info;

	String filename;
	String sha;
	String filepath;
	String updatesha;

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() throws FileNotFoundException, IOException {
		properties = properties(".\\src\\test\\resources\\Config.properties");
		//token = properties.getProperty("token");
		 report = new Sparkreport(properties.getProperty("ReportTitle"), properties.getProperty("ReporterName"), properties.getProperty("BaseUrl"));
	}

	@BeforeTest(alwaysRun = true)
	public void beforeTest() {
		RestAssured.baseURI = properties.getProperty("BaseUrl");
		userprofile = new UserProfilePOJO();
		repo = new CreateRepoPOJO();
		filecontent = new FileContentPOJO();
		info = new CommiterInfo();
	  request = BaseClass.requestSpecification(properties.getProperty("token"), "application/vnd.github+json");
	}

	@Test(priority = 1)
	
	public void getUserProfile(ITestContext result) throws FileNotFoundException {
			
		PrintStream profile = printStream(System.getProperty("user.dir") + "\\LogReport\\USER_PROFILE.txt");

		Response responce = UserProfile.getUserProfile(request, "vrushabhmanatkar13", profile);
		
		//logRequest(request, Sparkreport.test);
		//set attribute value of Status Code and Response for Log in Report
		result.setAttribute("Responce", responce.asString());
		result.setAttribute("statuscode", String.valueOf(responce.getStatusCode()));
		
		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");
		Assert.assertEquals(responce.getStatusCode(), 200);

		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		
		userprofile.setLogin(js.getString("login"));
		userprofile.setId(js.getInt("id"));
		userprofile.setNode_id(js.getString("node_id"));
		
		Assert.assertEquals(userprofile.getLogin(), "vrushabhmanatkar13");
		Assert.assertFalse(userprofile.getId() == 0);
		Assert.assertEquals(js.get("bio"), "QA & Automation Engineer  ");
		Assert.assertEquals(js.get("created_at"), "2022-07-27T09:59:15Z");
		Assert.assertEquals(js.get("name"), "Vrushabh_Manatkar");
		Assert.assertEquals(js.getString("url"), RestAssured.baseURI + "/users/" + userprofile.getLogin());
		

		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");
		
		
	}

	@Test(priority = 2)
	public void createRepo(ITestContext result) throws Exception {
		
		repo.setName("ABC");
		repo.setDescription("New Repo");
		repo.setHomepage("https://github.com");
		repo.setHas_issues(true);
		repo.setHas_projects(true);
		repo.setHas_wiki(true);
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\CREATE_REPO.txt");
				
		Response responce = UserRepos.createRepo(request, repo, loginfo);		
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		logRequest(request, Sparkreport.test);
		reponame = js.getString("name");
		username = js.getString("owner.login");
		id = js.getInt("id");
		node_id = js.getString("node_id");
		
		//Log Pay load in Report 
		report.infoJSON(objectMapper(repo));
		
		//set attribute value of Status Code and Response for Log in Report
		result.setAttribute("Responce", responce.asString());
		result.setAttribute("statuscode", String.valueOf(responce.getStatusCode()));
		
		
		
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

	@Test(priority = 3)
	public void getAllRepo(ITestContext result) throws FileNotFoundException {
		
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\GET_ALLREPO");
		Response responce = UserRepos.getAllRepos(request, userprofile.getLogin(), loginfo);
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		logRequest(request, Sparkreport.test);
		result.setAttribute("Responce", responce.asString());
		result.setAttribute("statuscode", String.valueOf(responce.getStatusCode()));
		
		
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

	@Test(priority = 4)
	public void getUserRepo(ITestContext result) throws FileNotFoundException {
	
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\GET_REPO.txt");
		Response responce = UserRepos.getUserRepos(request, userprofile.getLogin(), repo.getName(), loginfo);
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		logRequest(request, Sparkreport.test);
		//set attribute value of Status Code and Response for Log in Report
		result.setAttribute("Responce", responce.asString());
		result.setAttribute("statuscode", String.valueOf(responce.getStatusCode()));
		
		
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

	@Test(priority = 5)
	public void createFile(ITestContext result) throws Exception {
		
		
		filecontent.setMessage("Add new file");
		filecontent.setContent("Hi I am creating new File");
		info.setEmail("vrushabhmanatkar13@gmail.com");
		info.setName("Vrushabh Manatkar");
		filecontent.setCommitter(info);
		
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\CREATE_FILE");
		
		Response responce = CreateFile.createFile(request, username, reponame, "text.txt", filecontent, loginfo);
		logRequest(request, Sparkreport.test);
		//Log Pay load in Report 
		report.infoJSON(objectMapper(filecontent));
		//set attribute value of Status Code and Response for Log in Report
		result.setAttribute("Responce", responce.asString());
		result.setAttribute("statuscode", String.valueOf(responce.getStatusCode()));
		
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		filename = js.getString("content.name");
		sha = js.getString("content.sha");
		filepath = js.getString("content.path");
		
		Assert.assertEquals(responce.getStatusCode(), 201);
		Assert.assertEquals(filename, "text.txt");
		Assert.assertEquals(filepath, "text.txt");
		Assert.assertFalse(sha.isEmpty());

		Assert.assertEquals(js.getString("commit.author.name"), "Vrushabh Manatkar");
		Assert.assertEquals(js.getString("commit.author.email"), "vrushabhmanatkar13@gmail.com");
		Assert.assertFalse(js.getString("commit.author.date").isEmpty());

		Assert.assertEquals(js.getString("commit.committer.name"), info.getName());
		Assert.assertEquals(js.getString("commit.committer.email"), info.getEmail());
		Assert.assertEquals(js.getString("commit.message"), filecontent.getMessage());

		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");
		
		
	}

	@Test(priority = 6)
	public void getFile(ITestContext result) throws FileNotFoundException {
		
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\GET_FILE");
		Response responce = CreateFile.getFile(request, username, reponame, filename, loginfo);
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		
		logRequest(request, Sparkreport.test);
		//set attribute value of Status Code and Response for Log in Report
		result.setAttribute("Responce", responce.asString());
		result.setAttribute("statuscode", String.valueOf(responce.getStatusCode()));
		
		
		
		Assert.assertEquals(responce.getStatusCode(), 200);
		Assert.assertEquals(js.getString("name"), filename);
		Assert.assertEquals(js.getString("path"), filepath);
		Assert.assertEquals(js.getString("sha"), sha);
		String content = js.getString("content");
		Assert.assertEquals(decodeToBase64(content), decodeToBase64(filecontent.getContent()));

		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");
		
		
	}

	@Test(priority = 7)
	public void updateFile(ITestContext result) throws Exception {
		
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\UPDATE_FILE");
		FileUpdatePOJO updatefile = new FileUpdatePOJO();
		updatefile.setMessage("Up date File");
		updatefile.setContent("Hi I am Updating File content");
		updatefile.setSha(sha);
		Response responce = CreateFile.updateFile(request, username, reponame, filename, updatefile, loginfo);
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		
		logRequest(request, Sparkreport.test);
		//Log Pay load in Report 
		report.infoJSON(objectMapper(updatefile));
		//set attribute value of Status Code and Response for Log in Report
		result.setAttribute("Responce", responce.asString());
		result.setAttribute("statuscode", String.valueOf(responce.getStatusCode()));
		
		
		
		Assert.assertEquals(responce.getStatusCode(), 200);
		Assert.assertFalse(js.getString("content.sha").isEmpty());
		Assert.assertEquals(js.getString("content.name"), filename);
		Assert.assertEquals(js.getString("commit.message"), updatefile.getMessage());
		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");
		
		
		PrintStream loginfo1 = printStream(System.getProperty("user.dir") + "\\LogReport\\GETUPDATE_FILE");
		Response responce1 = CreateFile.getFile(request, username, reponame, filename, loginfo1);
		JsonPath js1 = ConvertJsonToString.convertJsonToString(responce1);
		updatesha = js1.getString("sha");
		
		
		//log Responce in Report in JSON Format
		//report.infoJSON(objectMapper(responce1.body()));
		
		Assert.assertNotEquals(updatesha, sha);
		Assert.assertEquals(js.getString("content.sha"), updatesha);
		Assert.assertEquals(decodeToBase64(js1.getString("content")), decodeToBase64(updatefile.getContent()));
		
		

	}

	@Test(priority = 8)
	public void deleteFile(ITestContext result) throws Exception {
		
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\DELETE_FILE");
		DeleteFilePOJO deletefile = new DeleteFilePOJO();
		deletefile.setMessage("Delete File");
		deletefile.setSha(updatesha);
		
		
		Response responce = CreateFile.deleteFile(request, username, reponame, filename, deletefile, loginfo);			
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		logRequest(request, Sparkreport.test);
		//Log Pay load in Report 
		report.infoJSON(objectMapper(deletefile));	
		//set attribute value of Status Code and Response for Log in Report
		result.setAttribute("Responce", responce.asString());
		result.setAttribute("statuscode", String.valueOf(responce.getStatusCode()));
		
		
		
		Assert.assertEquals(responce.getStatusCode(), 200);
		Assert.assertEquals(js.getString("content"), null);
		Assert.assertEquals(js.getString("commit.message"), deletefile.getMessage());
		Assert.assertNotEquals(js.getString("commit.sha"), deletefile.getSha());

		Assert.assertEquals(js.getString("commit.committer.name").replace("_", " "), info.getName());

		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");
		result.setAttribute("Responce", responce.asString());
		result.setAttribute("statuscode", String.valueOf(responce.getStatusCode()));
	}

	@Test(priority = 9)
	public void deleteUserRepo(ITestContext result) throws FileNotFoundException {
		
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\DELETE_REPO.txt");
		Response responce = UserRepos.deleteRepo(request, userprofile.getLogin(), repo.getName(), loginfo);
		
		logRequest(request, Sparkreport.test);
		//set attribute value of Status Code and Response for Log in Report
		result.setAttribute("Responce", responce.asString());
		result.setAttribute("statuscode", String.valueOf(responce.getStatusCode()));	
		
		
		
		Assert.assertEquals(responce.getStatusCode(), 204);	
		
	}

	
	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestContext result) {
		report.flush();
		result.removeAttribute("Responce");
		result.removeAttribute("statuscode");
		
	}
	

}
