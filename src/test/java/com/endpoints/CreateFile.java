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
	
	
	
public static Response createFile(RequestSpecification request,String path, FileContentPOJO filecontent, PrintStream ps) {
		
		Response responce = request.body(filecontent)
				.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
	           .when()
		           .put(path);
		
		   return responce;
	}

public static Response getFile(RequestSpecification request,String path, PrintStream ps) {
	Response responce = request.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
			.when()
	           .get(path);

	return responce ;
	
}
public static Response updateFile(RequestSpecification request,String path, FileUpdatePOJO fileupdate, PrintStream ps) {
	
	Response responce = request.body(fileupdate)
			.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
           .when()
	           .put(path);
	
	   return responce;
}
public static Response deleteFile(RequestSpecification request,String path, DeleteFilePOJO deletefile, PrintStream ps) {
	
	Response responce = request.body(deletefile)
			.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
           .when()
	           .delete(path);
	
	   return responce;
}

}
