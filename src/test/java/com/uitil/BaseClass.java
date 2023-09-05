package com.uitil;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
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
import io.restassured.response.Response;
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
	

	
	public static RequestSpecification requestSpecification(String token, Map<String, String> map) {
		RequestSpecification requestspecification = new RequestSpecBuilder()
			    .addPathParams(map)
			    .addHeader("Authorization", "Bearer "+token)
				.setContentType(ContentType.JSON)
				.build();
		return given().spec(requestspecification);
	}
	
	public static RequestSpecification requestSpecification(String token) {
		RequestSpecification requestspecification = new RequestSpecBuilder()
			    .addHeader("Authorization", "Bearer "+token)
				.setContentType(ContentType.JSON)
				.build();
		return given().spec(requestspecification);
	}
	public static ResponseSpecBuilder responceSpecification() {
		ResponseSpecBuilder responcespec = new ResponseSpecBuilder();
		responcespec.expectStatusCode(200);
	         return responcespec;
	}
	
	public static void logRequest(RequestSpecification req, ThreadLocal<ExtentTest> test) {
		QueryableRequestSpecification request=SpecificationQuerier.query(req);
		
		test.get().log(Status.INFO, "Base Url: "+request.getBaseUri());
		test.get().log(Status.INFO, "Complete URI: "+request.getURI());
		test.get().log(Status.INFO, "Method: "+request.getMethod());
		test.get().log(Status.INFO, "UserDefine path: "+request.getPathParams());
		test.get().log(Status.INFO, "Headers: "+request.getHeaders().asList().toString());
		
		
		System.out.println("===================== Request ==========================");
		System.out.println("");
		System.out.println("Base Url: "+request.getBaseUri());
		System.out.println("Complete URI: "+request.getURI());
		System.out.println("Method: "+request.getMethod());
		System.out.println("UserDefine path: "+request.getPathParams());
		System.out.println("Headers: "+request.getHeaders().asList().toString());
		
	}
	
	public static void logRequestBody(String body,ThreadLocal<ExtentTest> test) {
		test.get().log(Status.INFO, MarkupHelper.createCodeBlock(body, CodeLanguage.JSON));
		System.out.println("===================== Request Body ==========================");
		System.out.println("");
		System.out.println(body);
	}
	
	public static void logResponce(Response responce, ThreadLocal<ExtentTest> test) {
		test.get().log(Status.INFO, "Status Code: "+responce.getStatusCode());
		test.get().log(Status.INFO, "Responce Headers: "+responce.getHeaders().asList().toString());
		test.get().log(Status.INFO,  MarkupHelper.createCodeBlock(responce.getBody().asString(), CodeLanguage.JSON));
		
		System.out.println("===================== Responce ==========================");
		System.out.println("");
		System.out.println("Status Code: "+responce.getStatusCode());
		System.out.println("Responce Headers: "+responce.getHeaders().asList().toString());
		System.out.println( responce.asPrettyString());
	}
	
}
