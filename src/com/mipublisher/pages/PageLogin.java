package com.mipublisher.pages;

import org.testng.Assert;

import com.mipublisher.core.DefaultPage;
import com.mipublisher.core.Utils;

public class PageLogin extends DefaultPage
{
	Utils utils = new Utils();
	String Usercredential_errormessage = getTestData("TestData_PageLogin.UserCredential_errormessage");
	String SettingsCredential_errormessage = getTestData("TestData_PageLogin.SettingsCredential_errormessage");
	       
	
	
	//It will Launch our website
	public void fnlaunchsites()
	{		
		getDriver().launchsite(utils.getConfigurationData("InitialConfig.SiteUrl"));
		
			if(getDriver().isDisplayed("PageLogin.Settings_id"))
			{
				
			}
			else
			{
				for(int i=0;i<5;i++){
					getDriver().refresh();
					if(getDriver().isElementPresent("PageLogin.Settings_id")){
						break;
					}		
				}				
		    }		
	}
	
	//Function to set valid app settings like ,Host and site address
		public void fnappsettingsvalid(String strhostaddress,String strsiteaddress)
		{		
			getDriver().click("PageLogin.Settings_id");
			getDriver().sendValues(strhostaddress,"PageLogin.Host_id");
			getDriver().sendValues(strsiteaddress,"PageLogin.Site_id");
			getDriver().click("PageLogin.Save_btn");
			
		}	
	//Function to set invalid app settings like ,Host and site address
	public void fnappsettingsinvalid(String strhostaddress,String strsiteaddress)
	{		
		getDriver().click("PageLogin.Settings_id");
		getDriver().sendValues(strhostaddress,"PageLogin.Host_id");
		getDriver().sendValues(strsiteaddress,"PageLogin.Site_id");
		getDriver().click("PageLogin.Save_btn");
		
		if(getDriver().isElementPresent("PageLogin.Settingscredentials_errmsg"))
		{
			String errormessage=getDriver().getText("PageLogin.Settingscredentials_errmsg");
			Assert.assertEquals(errormessage,SettingsCredential_errormessage);
		}

	}
	
	//Function to login with invalid credentials
		public void fnlogininvalid(String strusername,String strpassword)
		{	//getDriver().waitForElement("PageLogin.Username");		
			getDriver().sendValues(strusername,"PageLogin.Username");
			getDriver().sendValues(strpassword,"PageLogin.Password");
			getDriver().waitForElement("PageLogin.client_id");
			getDriver().angulardropdownslection("PageLogin.client_id","PageLogin.Ivedix");
			getDriver().angulardropdownslection("PageLogin.App_id","PageLogin.Windows");
			getDriver().click("PageLogin.LOGIN");	
			if(getDriver().isElementPresent("PageLogin.Usercredentials_errmsg"))
			{
				String errormessage=getDriver().getText("PageLogin.Usercredentials_errmsg");
				Assert.assertEquals(errormessage,Usercredential_errormessage);
				
			}
			
		}
		
	
		//Function to login with valid credentials
	   public void fnloginvalid(String strusername,String strpassword)
	    {	//getDriver().waitForElement("PageLogin.Username");		
		getDriver().sendValues(strusername,"PageLogin.Username");
		getDriver().sendValues(strpassword,"PageLogin.Password");
		getDriver().angulardropdownslection("PageLogin.client_id","PageLogin.Ivedix");
		getDriver().angulardropdownslection("PageLogin.App_id","PageLogin.Windows");
		getDriver().click("PageLogin.LOGIN");	
		
	    }
	   
	
	
	
	
}
