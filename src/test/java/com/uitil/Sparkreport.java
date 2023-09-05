package com.uitil;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;




public class Sparkreport {
	
	public ExtentSparkReporter spark;
	private static ExtentReports extent;
	public static ThreadLocal<ExtentTest> test=new ThreadLocal<>();
	//public static ExtentTest test;
	public static String TAGNAME;
	public static String REPORTPATH;
	
	public Sparkreport(String documenttitle,String reportername, String hostname) {
		TAGNAME=new SimpleDateFormat("YYYY-MM-DD").format(new Date());
		
		REPORTPATH=System.getProperty("user.dir")+"\\Report\\GitAPI_Report.html";

		spark=new ExtentSparkReporter(REPORTPATH);
		spark.config().setDocumentTitle(documenttitle);
	    spark.config().setReportName(reportername);
	    spark.config().setTheme(Theme.DARK);
	    
		extent=new ExtentReports();
		extent.attachReporter(spark);
	    extent.setSystemInfo("Enviroment/Platform ", hostname);
	    extent.setSystemInfo("OS ", System.getProperty("os.name"));
	    extent.setSystemInfo("Java Version ", System.getProperty("java.version"));
	    	
	}
	
	public void create_test(String name, String auth_name) {
	
	ExtentTest Extenttest=extent.createTest(name).assignAuthor(auth_name).assignCategory(TAGNAME);
	  test.set(Extenttest);
	}
	
	public void create_info(String name) {
		test.get().log(Status.INFO, name);	
	}
	
	public void test_pass(ITestResult result) throws IOException {
  
    test.get().log(Status.PASS, result.getMethod().getMethodName());
  	
	}
	public void test_fail(ITestResult result) throws IOException {
		test.get().log(Status.FAIL, result.getMethod().getMethodName());	 
		test.get().log(Status.FAIL, result.getThrowable());
		result.getThrowable().printStackTrace();	
	}
	public void test_skip(ITestResult result) {
		test.get().log(Status.SKIP, result.getMethod().getMethodName());
	}
	public static void flush() {
		extent.flush();
	}
		
	
}
