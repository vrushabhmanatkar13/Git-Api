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
	
	public static Response getUserRepos(RequestSpecification request,String path, PrintStream ps) {
		
		Response responce=request.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
				.when().get(path);	
		return responce;
		
	}
	
	
	public static Response getAllRepos(RequestSpecification request, String pathparameters, PrintStream ps) {
		Response responce=request.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
				.when().get(pathparameters);
		return responce;
		
	}
	
	public static Response createRepo(RequestSpecification request, CreateRepoPOJO body, String path ,PrintStream ps) {	
		Response responce= request.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
				.body(body).when().post(path); ///user/repos
		return responce;
	}
	
	public static Response deleteRepo(RequestSpecification request,String path,PrintStream ps) {
		Response responce=request.filter(RequestLoggingFilter.logRequestTo(ps)).filter(ResponseLoggingFilter.logResponseTo(ps))
				.when().delete(path);
		return responce;
	}

}
