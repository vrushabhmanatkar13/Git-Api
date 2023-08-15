package com.uitil;


import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.RESTAPI.UserProfileTest;


public class ITestResultListner extends UserProfileTest implements ITestListener {

	
	
	public void onTestStart(ITestResult result) {
		report.create_test(result.getMethod().getMethodName(), properties.getProperty("AutherName"));
	}

	public void onTestSuccess(ITestResult result) {
		try {
			report.test_pass("statuscode","Responce",result);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void onTestFailure(ITestResult result) {
		try {
			report.test_fail("statuscode","Responce",result);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult result) {
		report.test_skip(result.getMethod().getMethodName());
	}

	public void onFinish(ITestContext context) {
		
	}

}
