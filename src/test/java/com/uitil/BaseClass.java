package com.uitil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Base64;
import java.util.Properties;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.specification.AuthenticationSpecification;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import io.restassured.specification.SpecificationQuerier;

public class BaseClass {

	public static PrintStream printStream(String path) throws FileNotFoundException {

		PrintStream ps = new PrintStream(new File(path));
		return ps;
	}

	public static String decodeToBase64(String encoded) {
		byte[] decode = Base64.getMimeDecoder().decode(encoded);
		return new String(decode);
	}

	public static Properties properties(String filepath) throws FileNotFoundException, IOException {
		Properties pr = new Properties();
		pr.load(new FileInputStream(new File(filepath)));
		return pr;
	}

	public static String objectMapper(Object name) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		String value = mapper.writeValueAsString(name);
		return value;
	}
	
	public static RequestSpecification requestSpecification(String token,String accpect) {
		RequestSpecification requestspecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Bearer " + token)
				.setAccept(accpect)
				.setContentType(ContentType.JSON)
				.build();
		return requestspecification;
	}
	
	
	public static ResponseSpecification responceSpecification(int statuscode, String server) {
		ResponseSpecification responcespecification = new ResponseSpecBuilder()
				.expectContentType(ContentType.JSON)
				.expectStatusCode(statuscode)
				.expectHeader("Server", server)
				.build();
	         return responcespecification;
	}
	
	public static void logRequest(RequestSpecification req, ThreadLocal<ExtentTest> test) {
		QueryableRequestSpecification request=SpecificationQuerier.query(req);
		
		test.get().log(Status.INFO, "Base Url: "+request.getBaseUri());
		test.get().log(Status.INFO, "Complete URI: "+request.getUserDefinedPath());
		test.get().log(Status.INFO, "Method: "+request.getMethod());
		test.get().log(Status.INFO, "UserDefine path: "+request.getPathParams());
		test.get().log(Status.INFO, "Headers: "+request.getHeaders().asList().toString());
		
		 
	}
	
}
