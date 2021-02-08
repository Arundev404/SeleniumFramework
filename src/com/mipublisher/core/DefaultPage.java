package com.mipublisher.core;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

public abstract class DefaultPage extends TestListener
{	
	public WebDriver driver;
	public boolean testCaseLogStatus = false;
	Utils utils = new Utils();
	
	public TestRunner getTestRunner()
	{
		return new TestRunner();
	}
		
	public CommonMethods getDriver() 
	{
		return getTestRunner().getCommonMethods();
	}	
	
	public String getTestData(String fileName,String testScriptName,String dataKey){
		//Get the method that called getTestData
		Utils utils = new Utils();
		String testData=utils.getTestaDataFromXML(fileName,testScriptName,dataKey);
		if(testData.isEmpty()){
			extentTest.get().fail("Test data missing for data key :"+dataKey);
		}
		return testData;
	}

	public String getTestData(String dataKey){
		//Get the method that called getTestData
		Utils utils = new Utils();
		String testData=utils.getTestData(dataKey);
		if(testData.isEmpty()){
			extentTest.get().fail("Test data missing for data key :"+dataKey);
		}
		return testData;
	}
	
	@BeforeSuite
	public void beforeSuit() {
		String filePathToClean = System.getProperty("user.dir") + File.separator + utils.getConfigurationData("ReportConfiguration.ScreenshotPath") +File.separator;
		File f = new File(System.getProperty("user.dir") + File.separator + utils.getConfigurationData("ReportConfiguration.ScreenshotPath"));
		File zip_file = new File(System.getProperty("user.dir") + File.separator + utils.getConfigurationData("ReportConfiguration.ZipPath"));
		if(f.exists() && f.isDirectory()) {
			cleanDirectory(filePathToClean);
		}else {
			f.mkdirs();
		}
		if(zip_file.exists() && zip_file.isDirectory()) {
			cleanZipDirectory(System.getProperty("user.dir") + File.separator + utils.getConfigurationData("ReportConfiguration.ZipPath"));
		} else {
			zip_file.mkdirs();
		}
		
	}
	
	public void cleanDirectory(String pathToDirectory)
    {
        
        try
        {    
        	File folder = new File(pathToDirectory);
            File fList[] = folder.listFiles();

            for (File f : fList) {
                if (f.getName().endsWith(".png")) {
                    f.delete(); 
                }
            }
            //FileUtils.cleanDirectory(new File(pathToDirectory));
        }
        catch (Exception e) 
        {
            extentTest.get().fail("Error occured while deleting contents"+e.getMessage());
        }                
        
    }
	
	public void cleanZipDirectory(String zipDirectoryPath)
	{
		File zip_file = new File(zipDirectoryPath+"/test_report.zip");
		zip_file.delete();
	}
	
	@Parameters({ "browserName" })
	@AfterSuite
	public void zipReportToMail(String browserName)
	{
		String zipDestPath = System.getProperty("user.dir") + File.separator + utils.getConfigurationData("ReportConfiguration.ZipPath") +File.separator + "test_report.zip";
		String zipSourcePath = System.getProperty("user.dir") + File.separator +"Reports";
		getDriver().compressFolder(zipDestPath,zipSourcePath);
	}
		
}