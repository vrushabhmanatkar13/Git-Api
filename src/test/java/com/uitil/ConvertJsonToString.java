package com.uitil;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ConvertJsonToString {

	public static JsonPath convertJsonToString(Response responce) {
		 JsonPath js=new JsonPath(responce.asString());
		 return js;
	}
	
}
