package com.RESTAPI;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.endpoints.CreateFile;
import com.endpoints.UserRepos;
import com.payloads.CommiterInfo;
import com.payloads.DeleteFilePOJO;
import com.payloads.FileContentPOJO;
import com.payloads.FileUpdatePOJO;
import com.uitil.ConvertJsonToString;
import com.uitil.Sparkreport;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserFiles extends SetUp_TearDown{
	
	RequestSpecification request;
	String filename;
	String sha;
	String filepath;
	String updatesha;
FileContentPOJO filecontent;
CommiterInfo info;
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		request=requestSpecification(properties.getProperty("token"));
		filecontent=new FileContentPOJO();
		info=new CommiterInfo();
	}
	
	
	@Test(priority = 1)
	public void createFile() throws Exception {
		
		filecontent.setMessage("Add new file");
		filecontent.setContent("Hi I am creating new File A");
		info.setEmail("vrushabhmanatkar13@gmail.com");
		info.setName("Vrushabh Manatkar");
		filecontent.setCommitter(info);
		
		String path="/repos/vrushabhmanatkar13/ABC/contents/text.txt";
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\CREATE_FILE");
		
		Response responce = CreateFile.createFile(request, path, filecontent, loginfo);
		logRequest(request, Sparkreport.test);	
		logRequestBody(objectMapper(filecontent), Sparkreport.test);
	    logResponce(responce, Sparkreport.test);
		
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

	@Test(priority = 2)
	public void getFile() throws FileNotFoundException {
		String path="/repos/vrushabhmanatkar13/ABC/contents/text.txt";
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\GET_FILE");
		Response responce = CreateFile.getFile(request, path, loginfo);
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		
		logRequest(request, Sparkreport.test);
		
		logResponce(responce, Sparkreport.test);
		
		
		Assert.assertEquals(responce.getStatusCode(), 200);
		Assert.assertEquals(js.getString("name"), filename);
		Assert.assertEquals(js.getString("path"), filepath);
		Assert.assertEquals(js.getString("sha"), sha);
		String content = js.getString("content");
		Assert.assertEquals(decodeToBase64(content), decodeToBase64(filecontent.getContent()));

		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");
		
		
	}

	@Test(priority = 3)
	public void updateFile() throws Exception {
		String path="/repos/vrushabhmanatkar13/ABC/contents/text.txt";
		
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\UPDATE_FILE");
		FileUpdatePOJO updatefile = new FileUpdatePOJO();
		updatefile.setMessage("Up date File");
		updatefile.setContent("Hi I am Updating File content");
		updatefile.setSha(sha);
		Response responce = CreateFile.updateFile(request, path, updatefile, loginfo);
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		
		logRequest(request, Sparkreport.test);
		logRequestBody(objectMapper(updatefile), Sparkreport.test);
		logResponce(responce, Sparkreport.test);
		
		Assert.assertEquals(responce.getStatusCode(), 200);
		Assert.assertFalse(js.getString("content.sha").isEmpty());
		Assert.assertEquals(js.getString("content.name"), filename);
		Assert.assertEquals(js.getString("commit.message"), updatefile.getMessage());
		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");
		
		
		PrintStream loginfo1 = printStream(System.getProperty("user.dir") + "\\LogReport\\GETUPDATE_FILE");
		Response responce1 = CreateFile.getFile(request, path, loginfo1);
		JsonPath js1 = ConvertJsonToString.convertJsonToString(responce1);
		updatesha = js1.getString("sha");
		
		logResponce(responce1, Sparkreport.test);
		
		Assert.assertNotEquals(updatesha, sha);
		Assert.assertEquals(js.getString("content.sha"), updatesha);
		Assert.assertEquals(decodeToBase64(js1.getString("content")), decodeToBase64(updatefile.getContent()));
		
		

	}

	@Test(priority = 4)
	public void deleteFile() throws Exception {
		String path="/repos/vrushabhmanatkar13/ABC/contents/text.txt";
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\DELETE_FILE");
		DeleteFilePOJO deletefile = new DeleteFilePOJO();
		deletefile.setMessage("Delete File");
		deletefile.setSha(updatesha);
		
		Response responce = CreateFile.deleteFile(request,path , deletefile, loginfo);			
		JsonPath js = ConvertJsonToString.convertJsonToString(responce);
		logRequest(request, Sparkreport.test);
		logRequestBody(objectMapper(deletefile), Sparkreport.test);
		logResponce(responce, Sparkreport.test);
		
		Assert.assertEquals(responce.getStatusCode(), 200);
		Assert.assertEquals(js.getString("content"), null);
		Assert.assertEquals(js.getString("commit.message"), deletefile.getMessage());
		Assert.assertNotEquals(js.getString("commit.sha"), deletefile.getSha());

		Assert.assertEquals(js.getString("commit.committer.name").replace("_", " "), info.getName());

		Assert.assertEquals(responce.getHeader("Content-Type"), "application/json; charset=utf-8");
		Assert.assertEquals(responce.getHeader("Server"), "GitHub.com");
		
	}
	
	
	@Test(priority = 5)
	public void deleteUserRepo() throws FileNotFoundException {
		String path="/repos/vrushabhmanatkar13/ABC";
		PrintStream loginfo = printStream(System.getProperty("user.dir") + "\\LogReport\\DELETE_REPO.txt");
		Response responce = UserRepos.deleteRepo(request,path, loginfo);
		
		logRequest(request, Sparkreport.test);
		logResponce(responce, Sparkreport.test);
	
		Assert.assertEquals(responce.getStatusCode(), 204);	
		
	}

}
