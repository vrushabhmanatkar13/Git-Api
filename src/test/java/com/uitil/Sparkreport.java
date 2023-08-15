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
	public void infoJSON(String name) {
		test.get().log(Status.INFO, MarkupHelper.createCodeBlock(name, CodeLanguage.JSON));	
	}
	
	public void test_pass(String status ,String attribute ,ITestResult result) throws IOException {
  //  test.generateLog(Status.PASS, result.getMethod().getMethodName());
    test.get().log(Status.PASS, (String)result.getTestContext().getAttribute(status));
    test.get().log(Status.PASS, MarkupHelper.createCodeBlock((String) result.getTestContext().getAttribute(attribute), CodeLanguage.JSON));
   
		
	}
	public void test_fail(String status,String attribute, ITestResult result) throws IOException {
	//	test.generateLog(Status.FAIL, result.getMethod().getMethodName());
		test.get().log(Status.FAIL, (String)result.getTestContext().getAttribute(status));	 
		test.get().log(Status.FAIL, MarkupHelper.createCodeBlock((String) result.getTestContext().getAttribute(attribute), CodeLanguage.JSON));
		result.getThrowable().printStackTrace();
		
		
		
	}
	public void test_skip(String name) {
		test.get().log(Status.SKIP, name);
	}
	public static void flush() {
		extent.flush();
	}
		
	
}
