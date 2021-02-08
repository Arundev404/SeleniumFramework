package com.mipublisher.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

public class TestRunner 
{
	public WebDriver driver;
	public static Utils utils = new Utils();
	private CommonMethods automationDriver;
	public static String driverPath =  System.getProperty("user.dir")+utils.getConfigurationData("InitialConfig.SeleniumDriverPath");
	
	private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
	
	public TestRunner()
	{
		driver = getCurrentDriver();
		automationDriver = new CommonMethods(driver);
	}
		
	public WebDriver getWebDriver() 
	{
		return getCurrentDriver();
	}
	
	public CommonMethods getCommonMethods() {
		return this.automationDriver;
	}
	
	//get current webdriver in thread
	public static WebDriver getCurrentDriver() {
        return webDriver.get();
        
    }
 
	//set given webdriver to thread
    static void setWebDriver(WebDriver driver) {
        webDriver.set(driver);
    }
    
    //Create new instance of webdriver for the given browser
    static WebDriver createInstance(String browserName) {
        WebDriver driver = null;
        String osName = System.getProperty("os.name").toLowerCase();
        if (browserName.toLowerCase().contains("firefox")) {
        	if(osName.contains("mac")) {
        		
        	}else if(osName.contains("windows")) {
        		System.setProperty("webdriver.firefox.marionette", driverPath + "geckodriver.exe");
        	}
        	driver = new FirefoxDriver();
            return driver;
        }
        if (browserName.toLowerCase().contains("internet")) {
            driver = new InternetExplorerDriver();
            return driver;
        }
        if (browserName.toLowerCase().contains("chrome")) {
        	if(osName.contains("mac")) {
        		System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver");
        	} else if(osName.contains("windows")) {
        		System.setProperty("webdriver.chrome.driver",driverPath + "chromedriver.exe");
        		
        	}
        	ChromeOptions options = new ChromeOptions(); 
    		options.addArguments("disable-infobars"); 
    		 driver = new ChromeDriver(options);
            
            return driver;
        }
        if (browserName.toLowerCase().contains("safari")) {
            driver = new SafariDriver();
            return driver;
        }
        return driver;
    }	
	
}
