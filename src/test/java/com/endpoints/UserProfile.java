package com.endpoints;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;

import java.io.PrintStream;



public class UserProfile {
	

	public static Response getUserProfile(RequestSpecification request,PrintStream ps) {		
		 Response responce = request.filter(RequestLoggingFilter.logRequestTo(ps))
				 .filter(ResponseLoggingFilter.logResponseTo(ps))
				 .when().get("{resource}/{username}");
		 return responce;
	}
	
	
	
	

}
