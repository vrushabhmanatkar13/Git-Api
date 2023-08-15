package com.endpoints;

import static io.restassured.RestAssured.*;

import java.io.PrintStream;

import com.payloads.CreateRepoPOJO;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserRepos {
	
	public static Response getUserRepos(RequestSpecification request,String username,String reponame, PrintStream ps) {
		
		Response responce=given().spec(request).pathParam("username", username).pathParam("reponame", reponame).filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
				.when().get("/repos/{username}/{reponame}");	
		return responce;
		
	}
	
	
	public static Response getAllRepos(RequestSpecification request, String pathparameters, PrintStream ps) {
		Response responce=given().spec(request).pathParam("username", pathparameters).filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
				.when().get("/users/{username}/repos");
		return responce;
		
	}
	
	public static Response createRepo(RequestSpecification request, CreateRepoPOJO body, PrintStream ps) {
		
		Response responce= given().spec(request).filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
				.body(body).when().post("/user/repos");
		return responce;
	}
	
	public static Response deleteRepo(RequestSpecification request,String username,String reponame,PrintStream ps) {
		Response responce=given().spec(request).pathParam("username", username).pathParam("reponame", reponame).filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
				.when().delete("/repos/{username}/{reponame}");
		return responce;
	}

}
