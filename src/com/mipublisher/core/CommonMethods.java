package com.mipublisher.core;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.ByAngularBinding;
import com.paulhammant.ngwebdriver.NgWebDriver;

//import com.aventstack.extentreports.ExtentTest;

public class CommonMethods extends DefaultPage
{	
	public  WebDriver driver;
	public String method;
	public String locatorValueFromXML;
	public String data;
	//public ExtentTest test;
	Utils utils = new Utils();
	//private WebElement nativeElement;
	private static NgWebDriver ngDriver;

	public CommonMethods(WebDriver driver) 
	{
		this.driver = driver;		
	}

	public void setMethod(String testMethod)
	{
		this.method = testMethod;
	}


	public enum LOCATORTYPE 
	{
		ID, XPATH, CLASS, LINKTEXT, NAME, PARTIALLINKTEXT, CSSSELECTOR, TAGNAME,BINDING, MODEL
	}
	public By getElement(String locator) 
	{
		By by = null;
		String locatorType = locator.split(":")[0].toUpperCase();
		String locatorValue =  locator.split(":")[1];	
		//System.out.println("locatorvalue=" + locatorValue);
		try 
		{
			switch (LOCATORTYPE.valueOf(locatorType)) {

			case ID:
				by = By.id(locatorValue);
				return by;

			case XPATH:
				by = By.xpath(locatorValue);
				return by;

			case CLASS:
				by = By.className(locatorValue);
				return by;

			case NAME:
				by = By.name(locatorValue);
				return by;

			case LINKTEXT:
				by = By.linkText(locatorValue);
				return by;

			case PARTIALLINKTEXT:
				by = By.partialLinkText(locatorValue);
				return by;

			case CSSSELECTOR:
				by = By.cssSelector(locatorValue);
				return by;

			case TAGNAME:
				by = By.tagName(locatorValue);
				return by;
			case BINDING:
				
				by =ByAngularBinding.xpath(locatorValue);
				return by;
			case MODEL:
				by =ByAngular.model(locatorValue);
				return by;

			default:
				return null;
			}
		} 
		catch (Exception e) {
			extentTest.get().info("Exception occurred in getBy. Locator value: "+ locatorValue);
		}
		return by;
	}

	//The below method will save the screen shot in Screenshots folder under Reports folder
	public void takeScreenShot() 
	{
		testCaseLogStatus = true;
		//get the driver
		driver = TestRunner.getCurrentDriver();
		String screenshotPath = utils.getConfigurationData("ReportConfiguration.ScreenshotPath");
		String screenshotDirectory = utils.getConfigurationData("ReportConfiguration.ScreenshotDirectory");
		String filePath = System.getProperty("user.dir")+screenshotPath;
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String fileName = new Date().getTime()+".png";

		try 
		{
			FileUtils.copyFile(scrFile, new File(filePath+fileName));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		try {
			extentTest.get().fail("Snapshot below: " + extentTest.get().addScreenCaptureFromPath(screenshotDirectory+fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void launchsite(String siteUrl)
	{
		try
		{	
			driver.manage().window().maximize();
			TestRunner.getCurrentDriver().get(siteUrl);
			
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}	    	    
	}

	public void sendKeys(String testData, String locatorName) 		
	{
		try
		{
			data= utils.getTestData(testData);	
			waitForElement(locatorName);
			locatorValueFromXML = utils.getLocatorData(locatorName);
			driver.findElement(getElement(locatorValueFromXML)).clear();			
			driver.findElement(getElement(locatorValueFromXML)).sendKeys(data);				
			extentTest.get().pass("Succesfully Entered "+ data +" on " + locatorName);
		}
		catch (Exception NoSuchElementException) 
		{			
			extentTest.get().fail(locatorName +" is not present in the page");
			takeScreenShot();
		}
	}	
 
	//Created by Devan	:for sending string values to a field in testcase level
	public void sendValues(String Data, String locatorName) 	
	{
		try
		{
			
			waitForElement(locatorName);
			locatorValueFromXML = utils.getLocatorData(locatorName);
			driver.findElement(getElement(locatorValueFromXML)).clear();			
			driver.findElement(getElement(locatorValueFromXML)).sendKeys(Data);				
			extentTest.get().pass("Succesfully Entered "+ Data +" on " + locatorName);
		}
		catch (Exception NoSuchElementException) 
		{			
			extentTest.get().fail(locatorName +" is not present in the page");
			takeScreenShot();
		}
	}	

	public void click(String locatorName)
	{
		try
		{
			waitForElement(locatorName);
			locatorValueFromXML = utils.getLocatorData(locatorName);
			driver.findElement(getElement(locatorValueFromXML)).click();
			extentTest.get().pass("Clicking on " + locatorName);
			//extentTest.get().pass("");
		}
		catch (ElementNotVisibleException e) 
		{
			extentTest.get().fail("Element - " + locatorName + " was not visible to click");
			takeScreenShot();
		} 

		catch (NoSuchElementException e) 
		{
			extentTest.get().fail("Element - " + locatorName	+ " was not found in the page");
			takeScreenShot();
		} 

		catch (StaleElementReferenceException e) 
		{
			wait(5);
			try {
				waitForElement(locatorName);
				String locatorValueFromXML = utils.getLocatorData(locatorName);
				driver.findElement(getElement(locatorValueFromXML)).click();
			} catch (Exception ex) {
				extentTest.get().fail("Could not click on the element " + locatorName);
				e.printStackTrace();
			}
			takeScreenShot();
		} 

		catch (TimeoutException timeout) 
		{
			extentTest.get().fail("Time out exception encounterd : " + locatorName);
			takeScreenShot();
		} 

		catch (Exception e) 
		{
			e.printStackTrace();
			if (e.getMessage().toString().contains("clickable")) 
			{
				extentTest.get().fail("Element " + locatorName + " was not clickable. please check for any another element on top of this which is blocking the click ");
			}
			extentTest.get().fail("Could not click on the element : " + locatorName);
			e.printStackTrace();
			takeScreenShot();
		}
	}

	public String getText(String locatorName) 
	{
		waitForElement(locatorName);
		String locatorValueFromXML = utils.getLocatorData(locatorName);
		try 
		{

			return driver.findElement(getElement(locatorValueFromXML)).getText();
		} 
		catch (NoSuchElementException e) 
		{
			extentTest.get().fail(locatorName + "is not present in the page");
			takeScreenShot();
		}
		return "";
	}

	public String getPageTitle() 
	{
		try 
		{
			extentTest.get().pass("Title fetched");
			return driver.getTitle();


		} 
		catch (Exception e) 
		{
			extentTest.get().fail("Exception while getting current page title");
			takeScreenShot();
			return "";		
		}

	}	

	public boolean isElementPresent(String locatorName) 
	{
		waitForElement(locatorName);
		String locatorValueFromXML = utils.getLocatorData(locatorName);

		try 
		{
			return driver.findElement(getElement(locatorValueFromXML)).isDisplayed();
		} 
		catch (NoSuchElementException e) 
		{
			extentTest.get().fail("Element not found");
			takeScreenShot();
			return false;
		}
		catch (StaleElementReferenceException e) 
		{
			wait(5);
			extentTest.get().fail("Stale state exception for " + locatorName + " : "+ locatorValueFromXML);
			takeScreenShot();
			return false;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			takeScreenShot();
			return false;
		}

	}
	
	public boolean waitForJStoLoad() {   //to wait for JS to load.Done by Devan

	    WebDriverWait wait = new WebDriverWait(driver, 50);
	    final JavascriptExecutor jse = (JavascriptExecutor)driver;

	    // wait for jQuery to load
	    ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
	      @Override
	      public Boolean apply(WebDriver driver) {
	        try {
	          return ((Long)jse.executeScript("return jQuery.active") == 0);
	          
	        }
	        catch (Exception e) {
	          return true;
	        }
	      }
	    };

	    // wait for Javascript to load
	    ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
	      @Override
	      public Boolean apply(WebDriver driver) {
	        return jse.executeScript("return document.readyState")
	            .toString().equals("complete");
	      }
	    };

	  return wait.until(jQueryLoad) && wait.until(jsLoad);
	}

	public boolean isDisplayed(String locatorName)// To find whether element is present
	{
		String locatorValueFromXML = utils.getLocatorData(locatorName);


		try 
		{
			driver.findElement(getElement(locatorValueFromXML)).isDisplayed();
			extentTest.get().pass("Element is present in locator " + locatorName);
			return true;
			//extentTest.get().pass("Element is present in locator " + locatorName);
		} 
		catch (Exception e) 
		{	  
			return false;
		}
	}

	public void waitForElement(String locatorName) {
		int timeToWait = 10;

		for (int i = 0; i < timeToWait; i++) {
			if (isDisplayed(locatorName)) {
				break;
			} else {
				wait(1);
			}
		}
	}

	public boolean wait(int waitTimeInSeconds) 
	{
		try 
		{
			Thread.sleep(waitTimeInSeconds * 1000);
			return true;
		} 		
		catch (Exception e) 
		{

			extentTest.get().fail("Exception occured in  wait");
			takeScreenShot();
			return false;
		}
	}

	public void doubleClick(String locatorName)//to perform double click action.Done by Reshma
	{

		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement locator = driver.findElement(getElement(locatorValueFromXML));
			Actions act = new Actions(driver);	
			Action doubleclicked = act.moveToElement(locator).doubleClick().build();
			doubleclicked.perform();
			extentTest.get().pass("DoubleClicking on " + locatorName +"is sucessfull");
		}
		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			takeScreenShot();
		}	 
	}
	public boolean mousehover(String locatorName)// To perform mousehover action.Done by Reshma
	{
		//String locatorValueFromXML = utils.getLocatorData(locatorName);
		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement mainMenu = driver.findElement(getElement(locatorValueFromXML));
			//mainMenu.click();
			Actions actions = new Actions(driver);
			actions.moveToElement(mainMenu).perform();	
			extentTest.get().pass("Mousehover on " + locatorName);
		}
		catch (StaleElementReferenceException e) 
		{
			wait(5);
			extentTest.get().fail(locatorName + "is not present in the page");
			takeScreenShot();
			//extentTest.get().fail("Stale state exception for " + locatorName + " : "+ locatorValueFromXML);
			return false;
		}
		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			takeScreenShot();
			e.printStackTrace();
		}
		return false;		
	}


	public void clickandhold(String locatorName)// Done by Reshma
	{
		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement clickandhold = driver.findElement(getElement(locatorValueFromXML));
			Actions actions = new Actions(driver);
			actions.clickAndHold(clickandhold).perform();
			extentTest.get().pass("clickandhold is sucessfull " + locatorName);
			//actions.release();
		}
		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			takeScreenShot();
			e.printStackTrace();
		}}

	public void rightclick(String locatorName)// Done by Reshma
	{
		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement ele = driver.findElement(getElement(locatorValueFromXML));

			Actions act = new Actions(driver);
			act.moveToElement(ele);	

			act.contextClick(ele);
			act.keyDown(Keys.CONTROL).sendKeys(Keys.TAB).click().build().perform();
			extentTest.get().pass("rightclick on " + locatorName);
		}
		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			takeScreenShot();
			e.printStackTrace();
		}
	}	


	public String getwindowtitle() // Done by Reshma
	{
		try 
		{
			String handle1=driver.getWindowHandle();
			System.out.println("parent window:" +handle1 +"has title:" +driver.getTitle());
			return driver.getWindowHandle();
		} 
		catch (Exception e) 
		{	
			extentTest.get().fail(e.getMessage());
			takeScreenShot();
			e.printStackTrace();		
		}
		return "";
	}	

	public String switchwindow()// Done by Reshma
	{
		try
		{

			for (String handle2 : driver.getWindowHandles())
			{
				driver.switchTo().window(handle2);
				System.out.println("child window:" +handle2 +"has title:" +driver.getTitle());
				//return driver.getWindowHandle();
			}}
		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			takeScreenShot();
			e.printStackTrace();
		}
		return "";
	}


	public void closewindow()// Done by Reshma
	{
		try
		{
			driver.close();
		}
		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			takeScreenShot();
			e.printStackTrace();
		}
	}
	public void sendkeysWithJavascriptExecutor(String testData,String locatorName)// Done by Reshma
	{
		try
		{
			waitForElement(locatorName);
			String data = utils.getTestData(testData);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement ele = driver.findElement(getElement(locatorValueFromXML));		

			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("arguments[0].value='"+ data +"';", ele);
		}

		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			takeScreenShot();
			e.printStackTrace();
		}

	}
	public void clickWithJavascriptExecutor(String locatorName)// Done by Reshma
	{
		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement click = driver.findElement(getElement(locatorValueFromXML));

			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", click);


		}

		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			takeScreenShot();
			e.printStackTrace();
		}


	}
	public String randomGenerator(String text)// Done by Reshma
	{
		try
		{
			Random randomGenerator = new Random();
			int randomInt = randomGenerator.nextInt(1000); 
			String randomvalue = text + randomInt;				
			extentTest.get().pass("Succesfully Entered random value "+ text +" on " + randomvalue);
			return randomvalue;
		}

		catch (Exception e) 
		{			
			extentTest.get().fail(e.getMessage());
			takeScreenShot();
			e.printStackTrace();
		}

		return "";
	}
	
	
	public String randomAlphanumeric() //Added by Devan
	{
		Random rn        = new Random();
		int    range     = 9999999 - 1000000 + 1;  
		int    randomNum =  rn.nextInt(range) + 1000000;  // For 7 digit number


		Random rc = new Random();
		char   c  = (char)(rc.nextInt(26) + 'A');
		String str = randomNum+""+c; 
		return str;
	}

	public String selectByIndex(String locatorName, int index)// Done by Reshma
	{

		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement sourceElement = driver.findElement(getElement(locatorValueFromXML));	
			sourceElement.click();
			wait(2);
			Select dropdown = new Select(sourceElement);

			dropdown.selectByIndex(index);
			extentTest.get().pass("select dropdown " + locatorName);
		}
		catch (Exception e) 
		{

			extentTest.get().fail("Exception occured in  selecting the index");
			takeScreenShot();
		}
		return locatorName;	 
	}

	public void selectByValue(String locatorName1, String locatorName2)// Done by Reshma
	{

		try
		{
			waitForElement(locatorName1);
			String locatorValueFromXML1 = utils.getLocatorData(locatorName1);
			WebElement sourceElement = driver.findElement(getElement(locatorValueFromXML1));	
			sourceElement.click();
			Select dropdown = new Select(sourceElement);	
			
			dropdown.selectByValue(locatorName2);
			extentTest.get().pass("select dropdown " +locatorName1);
		}
		catch (Exception e) 
		{

			extentTest.get().fail("Exception occured in  selecting the Value");
			takeScreenShot();
		}
		 
	}

	public void angulardropdownslection(String locatorName1, String locatorName2)// Done by Devan
	{

		try
		{   wait(3);
			waitForElement(locatorName1);
			String locatorValueFromXML1 = utils.getLocatorData(locatorName1);
			String locatorValueFromXML2 = utils.getLocatorData(locatorName2);
			WebElement sourceElement = driver.findElement(getElement(locatorValueFromXML1));	
			sourceElement.click();
			waitForElement(locatorName2);	
			WebElement selectionElement = driver.findElement(getElement(locatorValueFromXML2));	
			selectionElement.click();
			
			extentTest.get().pass("select dropdown " +locatorName1);
		}
		catch (Exception e) 
		{

			extentTest.get().fail("Exception occured in  selecting the Value");
			takeScreenShot();
		}
		 
	}
	
	public void datentimepickerwithsendkeys(String locatorName1, String dateFormat)// Done by Devan
	{

		try
		{
			waitForElement(locatorName1);
			String locatorValueFromXML1 = utils.getLocatorData(locatorName1);
			WebElement sourceElement = driver.findElement(getElement(locatorValueFromXML1));
			sourceElement.click();
			sourceElement.sendKeys(dateFormat);		
			sourceElement.sendKeys(Keys.TAB);
			wait(3);
			extentTest.get().pass("select dropdown " +locatorName1);
		}
		catch (Exception e) 
		{

			extentTest.get().fail("Exception occured in  selecting the Value");
			takeScreenShot();
		}
		 
	}
	
	
	
	
	
	
	public void datepickerwithclick(String locatorName1, String date)// Done by Devan
	{

		try
		{
			waitForElement(locatorName1);
			String locatorValueFromXML1 = utils.getLocatorData(locatorName1);
			WebElement sourceElement = driver.findElement(getElement(locatorValueFromXML1));	
			sourceElement.sendKeys(date);	
			wait(3);
			extentTest.get().pass("select dropdown " +locatorName1);
		}
		catch (Exception e) 
		{

			extentTest.get().fail("Exception occured in  selecting the Value");
			takeScreenShot();
		}
		 
	}

	//Created by Parvathy.KS
	//Default value for iMinDrop = 0
	public String SelectRandomValueFromDropdown(String locatorName, int iMinDrop)
	{
		String selectValue = "";
		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement select_random = driver.findElement(getElement(locatorValueFromXML));

			Select dropdown = new Select(select_random);

			//To get the highest index of the Dropdown
			List<WebElement> droplist = dropdown.getOptions();            
			int iHighDrop = droplist.size();

			//To Select an index randomly between minDrop and highDrop            
			int iSelect = new Random().nextInt(iHighDrop - iMinDrop)+ iMinDrop;                        
			dropdown.selectByIndex(iSelect);

			//Return the selected Text
			selectValue = dropdown.getFirstSelectedOption().getText();
			System.out.println("selected option is: "+ selectValue);
			extentTest.get().pass("Successfully selected from the Dropdown:  " + selectValue);
			return selectValue;        
		}

		catch (NoSuchElementException e) 
		{
			extentTest.get().fail("Element - " + locatorName    + " : Cannot locate option with selected index or Element is not present in the Page");            
		} 

		catch (TimeoutException timeout) 
		{
			extentTest.get().fail("Time out exception encounterd : " + locatorName);
		} 
		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			e.printStackTrace();            
		}
		return selectValue;                 
	}


	public void refresh()
	{

		try
		{	
			driver.navigate().refresh();	
			extentTest.get().pass("Successfully refreshed the page");
		}
		catch (Exception e) 
		{
			extentTest.get().fail("Exception occured in  refreshing the page");
			takeScreenShot();
		}	    	    

	}



	public void clear(String locatorName)// Done by Reshma
	{

		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			driver.findElement(getElement(locatorValueFromXML)).clear();
			extentTest.get().pass("Cleared on " + locatorName);
		}
		catch (ElementNotVisibleException e) 
		{
			extentTest.get().fail("Exception occured in  clearing the field");
			takeScreenShot();
		}	
	}

	public int tableRowSize(String locatorName)//To get number of rows in table. Done by Reshma
	{

		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement locator=driver.findElement(getElement(locatorValueFromXML));
			List<WebElement> rows=locator.findElements(By.tagName("tr"));
			int tablesize=rows.size();
			extentTest.get().pass("Row size is " + tablesize);
			return tablesize;
		}


		catch (Exception e) 
		{
			extentTest.get().fail("Failed to get row size");
		}
		return 0;

	}
	public void keyUp(String testData,String locatorName)//to enter lowercase characters.Done by Reshma
	{

		try
		{
			waitForElement(locatorName);
			String data = utils.getTestData(testData);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement ele = driver.findElement(getElement(locatorValueFromXML));		
			ele.clear();		
			Actions act = new Actions(driver);	
			act.click(ele).keyUp(Keys.SHIFT).sendKeys(data).perform();
			extentTest.get().pass("Entered lowercase characters to " + locatorName);
		}
		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
		}	 
	}
	public void keyDown(String testData, String locatorName)//to enter uppercase characters.Done by Reshma
	{

		try
		{
			waitForElement(locatorName);
			String data = utils.getTestData(testData);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement ele = driver.findElement(getElement(locatorValueFromXML));		
			ele.clear();
			Actions act = new Actions(driver);	
			act.click(ele).keyDown(Keys.SHIFT).sendKeys(data).perform();			
			extentTest.get().pass("Entered uppercase characters to  " + locatorName);
		}
		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
		}	 
	}
	public void dragAndDrop(String Sourcelocator, String Destinationlocator)//To perform drag and drop.Done by Reshma
	{
		try
		{
			waitForElement(Sourcelocator);
			waitForElement(Destinationlocator);
			String locatorValueFromXML = utils.getLocatorData(Sourcelocator);
			String locatorValueFromXML1 = utils.getLocatorData(Destinationlocator);
			WebElement Sourcelocatorid=driver.findElement(getElement(locatorValueFromXML));
			WebElement Destinationlocatorid=driver.findElement(getElement(locatorValueFromXML1));
			Actions action = new Actions(driver);
			action.dragAndDrop(Sourcelocatorid, Destinationlocatorid).build().perform();
			extentTest.get().pass("Drag and drop to " + Destinationlocator +"is sucess");
		}

		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			e.printStackTrace();
		}
	}
	public void checkTestLogStatus() {
		if(extentTest.get().getStatus().toString().contains("fail")) {
			Assert.fail();
		}
	}

	//Added by Arun John , to accept 
	public void acceptAlert(){
		try{
			Alert alert = driver.switchTo().alert();
			alert.accept();
			
		}
		catch(Exception e){
			extentTest.get().fail("Exception occured while accept/dismiss alert");
			takeScreenShot();
		}
	}
	//Added by Arun John ,dismiss alert
	public void cancelAlert()
	{
		try{
			Alert alert = driver.switchTo().alert();
			alert.dismiss();
		}
		catch(Exception e){
			extentTest.get().fail("Exception occured while accept/dismiss alert");
			takeScreenShot();
		}
	}
	

	//Added by Arun John , to display the text in the alert message
	public void handlerAlertTextMsg(){
		try{

			Alert alert = driver.switchTo().alert();
			String alert_msg = alert.getText();
			System.out.println("Alert message is : "+alert_msg);
		}
		catch(Exception e){
			extentTest.get().fail("Exception occured while retreiving alert message");
			takeScreenShot();
		}
	}


	//Method to select a particular value from dropdown-Written By Nisha Nandagopan

	public void selectByVisibleText(String locatorName,String testData ){
		try
		{
			waitForElement(locatorName);
			String data= utils.getTestData(testData);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			waitForElement(locatorName);
			Select dropdown=new Select(driver.findElement(getElement(locatorValueFromXML)));
			dropdown.selectByVisibleText(data);
			extentTest.get().pass("select dropdown by value " + locatorName);
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			extentTest.get().fail("Exception occured in  selecting the dopdown by value");

		}

	}

	//Method to select the currently selected value from dropdown-Written By Nisha Nandagopan

	public String getCurrentValueFromDropdown(String locatorName)
	{

		try
		{
			waitForElement(locatorName);						
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			waitForElement(locatorName);
			Select dropdown=new Select(driver.findElement(getElement(locatorValueFromXML)));						
			WebElement option =dropdown.getFirstSelectedOption();
			String currentValue=option.getText();
			extentTest.get().pass("Successfully fetched current value from " + locatorName);
			return currentValue;
		}
		catch (Exception e) 
		{

			extentTest.get().fail("Exception occured in  getting the current value from dopdown");
			return null;
		}

	}

	//Created by Prasanth
	//Created for get data from Alertbox	
	public String getalerttext() 
	{
		try 
		{       
			Alert alert = driver.switchTo().alert();
			// Capturing alert message.    
			String alertMessage= alert.getText();
			System.out.println(alertMessage);
			// Accepting alert		
			alert.accept();
		} 
		catch (Exception e) 
		{			
			extentTest.get().fail("Exception occured in capturing data");
			takeScreenShot();
		}

		return "";
	}	

	//Created by Prasanth
	//Created for insert data to Alertbox
	public void senddatatoalert(String text)
	{
		try 
		{
			Alert alert = driver.switchTo().alert();
			// Send data to alert.    
			alert.sendKeys(text);                       
			// Accepting alert		
			alert.accept();
		}

		catch (Exception e) 
		{			
			extentTest.get().fail("Exception occured while inserting data");
			takeScreenShot();
		}
	}


	//Added by devan
	// Upload a file,where path is the local path of the folder in which the file exist
	public void UploadFile(String locatorName,String Path)
	{
		try
		{
			waitForElement(locatorName);
			String data = utils.getTestData(Path);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement UploadBtn = driver.findElement(getElement(locatorValueFromXML));
			UploadBtn.sendKeys(data);

		}	
		catch (Exception e) 
		{
			extentTest.get().fail("Exception occured while Uploading the file");
			takeScreenShot();
		}

	}

	//Added by devan
	// Upload a file with AutoIt ,where Path should be the path of Upload.exe
	public void UploadFileUsingAutoIt(String locatorName,String Path)
	{
		try
		{
			waitForElement(locatorName);
			String data = utils.getTestData(Path);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement UploadBtn = driver.findElement(getElement(locatorValueFromXML));
			UploadBtn.click();
			Runtime.getRuntime().exec(data);

		}	
		catch (Exception e) 
		{
			extentTest.get().fail("Exception occured while Uploading the file");
			takeScreenShot();
		}

	}

	public boolean isAlertPresent()
	{
		try
		{

			driver.switchTo().alert();
			return true;
		}   
		catch (NoAlertPresentException Ex)
		{
			return false;
		}   
	}

	//  Nov 8 - Scroll to an element using javascriptexecutor

	public void scrolltoelement(String locatorName)
	{
		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement click = driver.findElement(getElement(locatorValueFromXML));

			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].scrollIntoView(true);", click);

		}

		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			takeScreenShot();
			e.printStackTrace();
		}


	}

	// Nov 8 - Scroll to an element using action

	public void moveToElementUsingActions(String locatorName)
	{

		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement ele = driver.findElement(getElement(locatorValueFromXML));
			Actions actions = new Actions(driver);			
			actions.moveToElement(ele).perform();

			extentTest.get().pass("Scrolled to Elemnt: " + locatorName);
		}
		catch (Exception e) 
		{
			extentTest.get().fail(e.getMessage());
			e.printStackTrace();
		}    

	}

	public void switchToIframe(String frameName)
	{
		try
		{ 

			driver.switchTo().frame(frameName);
		}
		catch (Exception e)
		{
			extentTest.get().fail("iFrames could not be found.");
		}
	}
	public void switchToParent(String frameName)
	{
		try
		{ 
			driver.switchTo().parentFrame();
		}
		catch (Exception e)
		{
			extentTest.get().fail("Parent Frame could not be found.");
		}
	}
	
	
	public void compressFolder(String pathToOutput, String pathToContentToAdd) {
		
		try {
			ZipFile zipFile = new ZipFile(pathToOutput);
            File f = new File(pathToContentToAdd);    
            
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); 
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            File[] files = f.listFiles();
            for (File file : files) {
            	if (!file.isHidden()) {
            		if (file.isDirectory() && file.list().length>0) {
                    	zipFile.addFolder(file, parameters);
                    	File[] subFiles = file.listFiles();
                    	for(File subFile : subFiles) {
                    		zipFile.addFile(subFile, parameters);
                    	}
                    } else {
                    	if(file.isFile()) {
                    		zipFile.addFile(file, parameters);
                    	}
                    	
                    }
                }
                
            }
		} 
		catch (ZipException e) {
			e.printStackTrace();
		}
	}

	public void getFontSize(String locatorName)
	{

		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement element = driver.findElement(getElement(locatorValueFromXML));

			String fontSize = element.getCssValue("font-size");
			extentTest.get().pass("Successfully obtained the FontSize of " + locatorName + ": "+ fontSize);
		}
		catch (Exception e) 
		{
			extentTest.get().fail("Failed to obtain the FontSize of " + locatorName);
			takeScreenShot();
		}
	}

	public void getFontColor(String locatorName)
	{

		try
		{
			waitForElement(locatorName);
			String locatorValueFromXML = utils.getLocatorData(locatorName);
			WebElement element = driver.findElement(getElement(locatorValueFromXML));

			String fontColor = element.getCssValue("color");
			extentTest.get().pass("Successfully obtained the FontColor of " + locatorName + ": "+ fontColor);
		}
		catch (Exception e) 
		{
			extentTest.get().fail("Failed to obtain the FontColor of " + locatorName);
			takeScreenShot();
		}
	}

	public String getCurrentUrl() 
	{
		String current_URL = "";
		try 
		{
			current_URL = driver.getCurrentUrl(); 
			extentTest.get().pass("URL is fetched");
			return current_URL;


		} 
		catch (Exception e) 
		{
			extentTest.get().fail("Exception while getting current URL");
			takeScreenShot();
			return current_URL; 
		}

	}

	//Common method to pass text directly using sendKeys method

	public void sendKeys_withDirectTestData(String data, String locatorName)
	{
		try
		{
			locatorValueFromXML = utils.getLocatorData(locatorName);
			driver.findElement(getElement(locatorValueFromXML)).clear();
			driver.findElement(getElement(locatorValueFromXML)).sendKeys(data);
			extentTest.get().pass("Succesfully Entered "+ data +" on " + locatorName);
		}
		catch (Exception NoSuchElementException)
		{   
			extentTest.get().fail(locatorName +" is not present in the page");
			takeScreenShot();
		}
	}

	public boolean isSelected(String locatorName)
	{
		String locatorValueFromXML = utils.getLocatorData(locatorName);
		boolean isSelected = false;

		try
		{
			isSelected =  driver.findElement(getElement(locatorValueFromXML)).isSelected();
			extentTest.get().pass("Checkbox is checked in locator " +locatorName);
			return isSelected;
		}
		catch (Exception e)
		{   
			return isSelected;
		}

	}



	//Created by Parvathy KS
	//Common method for writing data to an XML
	//We need to pass the parameters for naming the XML, Value to be written, Attribute name.
	//This common method is strictly based on our Testdata.xml format

	public void writeToXML(String Value_to_be_written, String XML_Name, String AttributeName)
	{

		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();  

			//Add elements to document
			//create Root Element and append rootelement to document, rootElement = testData
			Document doc = dBuilder.newDocument();
			Element rootElement = doc.createElement("testData");   
			doc.appendChild(rootElement);

			//Create data element and place them under the root
			Element data = doc.createElement("data");
			rootElement.appendChild(data);

			//Set attribute to data element
			Attr attr = doc.createAttribute("name");
			attr.setValue(AttributeName);
			data.setAttributeNode(attr);

			//Create Value element
			Element value = doc.createElement("value");
			value.appendChild(doc.createTextNode(Value_to_be_written));
			data.appendChild(value);

			//write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);

			//write to console and file

			String path = "D:/New_Selenium_Cabot_Framework_Latest/test_automation/Resources/test-data/" + XML_Name + ".xml";
			StreamResult file = new StreamResult(new File(path));
			//write data
			//transformer.transform(source, console);
			transformer.transform(source, file);
			//System.out.println(path);
			extentTest.get().pass("XML File saved Successfuly");    

		}
		catch (Exception exp) 
		{
			exp.printStackTrace();
		}
	}

	public void keyboardEventsUsingSendKeys(Keys keyboardCharacter) //To perform keyboard events
	{
		try{ 
			Actions act = new Actions(driver); 
			act.sendKeys(keyboardCharacter).build().perform();
			extentTest.get().pass("Sucessfully performed keyboard events" );

		}
		catch (Exception e) 
		{
			extentTest.get().fail("Unable to perform keyboard events");
		}  
	}
	
	public boolean isEnabled(String locatorName)// To find whether button is enabled
	{
		 String locatorValueFromXML = utils.getLocatorData(locatorName);
	
	
		 try
		 {
			 boolean button= driver.findElement(getElement(locatorValueFromXML)).isEnabled();
			  extentTest.get().pass("Button is enabled in locator " + locatorName);
			  return button;
			 
		 }
		 catch (Exception e)
		 {
		 
			  extentTest.get().fail("Element - " + locatorName + " was not visible to click");
			  takeScreenShot();
			  return false;
		 }
	}
	
	public String getAttributeValue(String locatorName, String attributeName) {
		
		String locatorValueFromXML = utils.getLocatorData(locatorName);
		 try
		 {
			 String attrValue = driver.findElement(getElement(locatorValueFromXML)).getAttribute(attributeName);
			 extentTest.get().pass("Attribute value fetched from " + locatorName);
			  return attrValue;
			 
		 }
		 catch (Exception e)
		 {
		 
			  extentTest.get().fail("Element - " + locatorName + " was not found");
			  takeScreenShot();
			  return "";
		 }
	}	 
	

		 public void handleangularpage()
		 {
			 ngDriver = new NgWebDriver((JavascriptExecutor) driver);
				ngDriver.waitForAngularRequestsToFinish();
				 extentTest.get().pass("Waits for angular request to finish" );
		 }
		 
		 
		
		public void dateandtimechanger(int year,int month,int date,int hourOfDay,int minute)// Done by Devan
			{

				try
				{  
					 Calendar cal = Calendar.getInstance();
				       // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				        cal.set(year,month,date,hourOfDay,minute);
				}
				catch (Exception e) 
				{

					extentTest.get().fail("Exception occured in  selecting the Value");
					takeScreenShot();
				}
				 
			}
		
		 
		
		//Created by Devan	:for getting a value or count from a group of text by trimming unwanted data.
		public void getcountfromtext(String locatorName) 	
		{
			try
			{
								
				getDriver().waitForJStoLoad();
				String count = getDriver().getText(locatorName);
				extentTest.get().pass("The current count of " + locatorName + " is " + count);
			}
			catch (Exception NoSuchElementException) 
			{			
				extentTest.get().fail(locatorName +" is not present in the page");
				takeScreenShot();
			}
		}

		public String setDate(int i) {
			// Method to set a date by finding current date 
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");  
			   LocalDateTime now = LocalDateTime.now(); 
			   LocalDateTime date=now.plusDays(i);
			   String updateddate=dtf.format(date);
			return updateddate;
		}	
		
		public String setrandomtime(int i) {
			// Method to set random time
			   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");  
			   LocalDateTime now = LocalDateTime.now(); 
			   LocalDateTime time=now.plusHours(i);
			   String updatedtime=dtf.format(time);
			return updatedtime;
			
		}	
		
		public String setDatetime() {
			// Method to set a date by finding current date 
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");  
			   LocalDateTime now = LocalDateTime.now(); 			   
			   String datetime=dtf.format(now);
			   return datetime;
			
		}	
		
		
		public void dropdownwithstringselect(String locatorName1,String locatorName2,String str)// Done by Devan
		{
			try
			 {
				//ngDriver.waitForAngularRequestsToFinish();
				wait(3);
				String locatorValueFromXML1 = utils.getLocatorData(locatorName1);
				WebElement sourceElement = driver.findElement(getElement(locatorValueFromXML1));				
				sourceElement.click();
				wait(2);
				String locatorValueFromXML2 = utils.getLocatorString(locatorName2,str);
				System.out.println(locatorValueFromXML2);
				WebElement sourceElement1 = driver.findElement(getElement(locatorValueFromXML2));	
				sourceElement1.click();

				  extentTest.get().pass("Attribute value fetched from " + locatorName1);
				  
				 
			 }
			 catch (Exception e)
			 {			 
				  extentTest.get().fail("Element - " + locatorName1 + " was not found");
				  takeScreenShot();
				 
			 }
			
		}
		
		
		
		
		
		
}

