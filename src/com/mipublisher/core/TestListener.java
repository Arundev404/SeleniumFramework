package com.mipublisher.core;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class TestListener implements ITestListener {

	WebDriver driver=null;
	Utils utils = new Utils();
	String filePath = System.getProperty("user.dir")+utils.getConfigurationData("ReportConfiguration.ScreenshotPath");
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	private static ThreadLocal<ExtentReports> extentThread = new ThreadLocal<ExtentReports>();
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
    public static ExtentTest Test;
    String screenShotPath;
    
	
	@Override
	public synchronized void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		ExtentTest child = extentThread.get().createTest(result.getMethod().getMethodName());
		extentTest.set(child);
		extentTest.get().info("Testcase Started");
	}

	@Override
	public synchronized void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		extentTest.get().info("Testcase Completed");
	}

	@Override
	public synchronized void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		String testName = result.getMethod().getXmlTest().getName();
		String methodName=result.getMethod().getMethodName().toString().trim();
    	
    	if(!result.getThrowable().toString().contains("java.lang.AssertionError: null")) {
    		String screenshotDirectory = utils.getConfigurationData("ReportConfiguration.ScreenshotDirectory");
    		takeScreenShot(methodName+"_"+testName);
      		extentTest.get().fail(result.getThrowable());
    		screenShotPath = filePath+result.getMethod().getMethodName()+"_"+testName+".png";
    		extentTest.get().log(Status.FAIL, MarkupHelper.createLabel(result.getMethod().getMethodName()+" Test case FAILED due to below issues:", ExtentColor.RED));
    		try {
    			extentTest.get().fail("Snapshot below: " + extentTest.get().addScreenCaptureFromPath(screenshotDirectory));
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	extentTest.get().info("Testcase Completed");
    	
	}
	
	public void takeScreenShot(String methodName) {
    	//get the driver
    	driver = TestRunner.getCurrentDriver();
    	 File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
         //The below method will save the screen shot in d drive with test method name 
            try {
				FileUtils.copyFile(scrFile, new File(filePath+methodName+".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
    }

	@Override
	public synchronized void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		extentTest.get().skip(result.getThrowable());
	}

	@Override
	public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
		String browserName = context.getCurrentXmlTest().getParameter("browserName");
		WebDriver driver = TestRunner.createInstance(browserName);
		TestRunner.setWebDriver(driver);
		String OSType = utils.getConfigurationData("ReportConfiguration.OS");
		String HostName = utils.getConfigurationData("ReportConfiguration.HostName");
		String Environment = utils.getConfigurationData("ReportConfiguration.Environment");
		String Username = utils.getConfigurationData("ReportConfiguration.Username");
		String DocumentTitle = utils.getConfigurationData("ReportConfiguration.DocumentTitle");
		String ReportName = utils.getConfigurationData("ReportConfiguration.ReportName");
        
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + File.separator + utils.getConfigurationData("ReportConfiguration.ReportPath")+"ExtentReportTestNG_"+browserName+".html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		
		extent.setSystemInfo("OS", OSType);
		extent.setSystemInfo("Host Name", HostName);
		extent.setSystemInfo("Environment", Environment);
		extent.setSystemInfo("Username", Username);
		
		// Set our document title, theme etc..
		htmlReporter.config().setDocumentTitle(DocumentTitle);
		htmlReporter.config().setReportName(ReportName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.DARK);
		extentThread.set(extent);
		  
	}

	@Override
	public synchronized void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		WebDriver driver = TestRunner.getCurrentDriver();
        if (driver != null) {
            driver.quit();
        }
        
        extentThread.get().flush();
	}

}
