package com.endpoints;

import static io.restassured.RestAssured.given;

import java.io.PrintStream;

import com.payloads.DeleteFilePOJO;
import com.payloads.FileContentPOJO;
import com.payloads.FileUpdatePOJO;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CreateFile {
	
	
	
public static Response createFile(RequestSpecification request,String username,String reponame,String filename, FileContentPOJO filecontent, PrintStream ps) {
		
		Response responce = 
				given().spec(request).pathParam("username", username).pathParam("reponame", reponame).pathParam("filename", filename)
				.body(filecontent)
				.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
	           .when()
		           .put("/repos/{username}/{reponame}/contents/{filename}");
		
		   return responce;
	}

public static Response getFile(RequestSpecification request,String username,String reponame,String filename, PrintStream ps) {
	Response responce = 
			given().spec(request).pathParam("username", username).pathParam("reponame", reponame).pathParam("filename", filename)
			.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
			.when()
	           .get("/repos/{username}/{reponame}/contents/{filename}");

	return responce ;
	
}
public static Response updateFile(RequestSpecification request,String username,String reponame,String filename, FileUpdatePOJO fileupdate, PrintStream ps) {
	
	Response responce = 
			given().spec(request).pathParam("username", username).pathParam("reponame", reponame).pathParam("filename", filename)
			.body(fileupdate)
			.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
           .when()
	           .put("/repos/{username}/{reponame}/contents/{filename}");
	
	   return responce;
}
public static Response deleteFile(RequestSpecification request,String username,String reponame,String filename, DeleteFilePOJO deletefile, PrintStream ps) {
	
	Response responce = 
			given().spec(request).pathParam("username", username).pathParam("reponame", reponame).pathParam("filename", filename)
			.body(deletefile)
			.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
           .when()
	           .delete("/repos/{username}/{reponame}/contents/{filename}");
	
	   return responce;
}

}
