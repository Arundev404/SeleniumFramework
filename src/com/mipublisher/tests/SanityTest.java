package com.mipublisher.tests;

import org.testng.annotations.Test;

import com.mipublisher.core.DefaultPage;
import com.mipublisher.pages.ObjectCreationPage;

public class SanityTest extends DefaultPage
{
	ObjectCreationPage driver=new ObjectCreationPage();
	String Unaidsuser = getTestData("TestData_PageLogin.USER_NAME");
	String Invaliduser = getTestData("TestData_PageLogin.Invalid_USERNAME");
	String Password = getTestData("TestData_PageLogin.USER_PASSWORD");
	String Invalidpassword = getTestData("TestData_PageLogin.Invalid_USERPASSWORD");
	String Host_sandbox = getTestData("TestData_PageLogin.Host");
	String Invalidhost = getTestData("TestData_PageLogin.Invalid_Host");
	String Site_core = getTestData("TestData_PageLogin.Site");
	String Title = getTestData("TestData_PageLogin.Title")+getDriver().setDatetime();
	String Description = getTestData("TestData_PageLogin.Description");
	

	@Test(priority=1)
	public void LoginScenario_NegativeTestwithInvalidAppsettings() throws InterruptedException
	{		
		driver.pgLogin.fnlaunchsites();
		driver.pgLogin.fnappsettingsinvalid(Invalidhost,Site_core);
		//driver.pgLogin.fnloginvalid(Unaidsuser,Password);			
	}
	
}

