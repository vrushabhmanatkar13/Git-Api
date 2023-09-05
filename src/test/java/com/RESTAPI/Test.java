package com.RESTAPI;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentTest;

public class Test {

	public static void main(String[] args) {
      //  RestAssured.baseURI="https://api.github.com/users/vrushabhmanatkar13";
	//	given().log().all().when().get().then().log().all();
	//getallrepor();
	//createrepo();
	//	put();
	//	deleterepos();
	//getrepo();
		getuserinfo();
	}
	
	public static RequestSpecification requestSpecification(String token, Map<String, String> map) {
		RequestSpecification requestspecification = new RequestSpecBuilder()
			.addPathParams(map)
				.addHeader("Authorization", "Bearer "+token)
				.setContentType(ContentType.JSON)
				.setBasePath("{users}/{username}")
				.build();
		return given().spec(requestspecification);
	}
	
	
	public static RequestSpecification create(String token,Map<String, String> map) {
		return given().auth().oauth2(token).pathParams(map).contentType(ContentType.JSON).basePath("{users}/{username}");
	}
	
	public static void logRequest(RequestSpecification req) {	
		QueryableRequestSpecification request=SpecificationQuerier.query(req);
		System.out.println(request.getURI());
		System.out.println(request.getPathParams());
		System.out.println(request.getMethod());
		System.out.println(request.getBaseUri());
		
	}
	
	public static void getuserinfo() {
			
         RestAssured.baseURI="https://api.github.com/";
         Map<String, String >map=new HashMap<>();
         map.put("users", "users");
         map.put("username", "vrushabhmanatkar13");
         
         RequestSpecification req = create("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", map);
		
         req.when().get().then().log().all();
		logRequest(req);
	}
	
	public static void getallrepor() {
       RestAssured.baseURI="https://api.github.com/users/vrushabhmanatkar13/repos";
		
		given().log().all().auth().oauth2("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").when().get().then().log().all();
	}
	
	public static void createrepo() {
		
		RestAssured.baseURI="https://api.github.com/user/repos";
		given().log().all().auth().oauth2("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")
		.body("{\"name\":\"Hello-World\",\"description\":\"This is your first repository\",\"homepage\":\"https://github.com\",\"has_issues\":true,\"has_projects\":true,\"has_wiki\":true}")
		.when().post().then().log().all();
	}
	
	public static void getrepo() {
        RestAssured.baseURI="https://api.github.com/repos/vrushabhmanatkar13/ABC";
		
		given().log().all().auth().oauth2("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").when().get().then().log().all();
	}
	
	public static void deleterepos() {
		RestAssured.baseURI="https://api.github.com/repos/vrushabhmanatkar13/ABC";
		given().log().all().auth().oauth2("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").when().delete().then().log().all();
	}
	public static void put() {
	
	RestAssured.baseURI="https://api.github.com/repos/vrushabhmanatkar13/Hello-World";
	given().log().all().auth().oauth2("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")
	.body("{\"description\":\"This is your Edited repository\"}")
	.when().patch().then().log().all();
	
	}
}


//for get all url for user >  https://api.github.com/users/vrushabhmanatkar13
//for get all repos for user > https://api.github.com/users/vrushabhmanatkar13/repos
//for get specific repor >  https://api.github.com/repos/vrushabhmanatkar13/Digital-Codes
//for get all org name > https://api.github.com/users/vrushabhmanatkar13/orgs

//post create repositry >  https://api.github.com/user/repos
// and use oauth2(pass access token)