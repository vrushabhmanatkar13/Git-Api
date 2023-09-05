package com.RESTAPI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.uitil.BaseClass;
import com.uitil.Sparkreport;

import io.restassured.RestAssured;

public class SetUp_TearDown extends BaseClass {
	public static Properties properties;
	public static Sparkreport report;

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() throws FileNotFoundException, IOException {
		properties = properties(".\\src\\test\\resources\\Config.properties");
		report = new Sparkreport(properties.getProperty("ReportTitle"), properties.getProperty("ReporterName"),
				properties.getProperty("BaseUrl"));
	}

	@BeforeTest(alwaysRun = true)
	public void beforeTest() {
		RestAssured.baseURI = properties.getProperty("BaseUrl");
		
	}
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestContext result) {
		report.flush();
		
	}

}
