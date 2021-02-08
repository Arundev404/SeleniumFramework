package com.mipublisher.core;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Utils 
{
	
	String testDataFilePath = "Resources/test-data/";
	String configurationFilePath = "Resources/configuration/";
	String ojectRepositoryFilePath = "Resources/object-repository/";
	public String getLocatorData(String locatorKeyInXML)
	{
		try 
		{
			
			String locatorName = locatorKeyInXML.split("\\.")[1];
			String pageName = locatorKeyInXML.split("\\.")[0];
			String locatorValue = "";
			boolean isLocatorPresent = false;
			
			Document xmlDocument = readXML(ojectRepositoryFilePath + pageName + ".xml");

			if (xmlDocument != null) 
			{
				NodeList locatorNodes = xmlDocument.getElementsByTagName("locator");

				for (int nodeCounter = 0; nodeCounter < locatorNodes.getLength(); nodeCounter++) 
				{
					Element object = (Element) locatorNodes.item(nodeCounter);

					if (object.getAttribute("name").equalsIgnoreCase(locatorName)) 
					{
						locatorValue = object.getElementsByTagName("value").item(0).getTextContent();
						isLocatorPresent = true;
						//System.out.println("Locator Name: " + locatorName+	 " Value: "+ locatorValue + " ");
						break;
					}
				}
			}
			if (isLocatorPresent)
				return locatorValue;
			else
				return "";
			
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
	
	public String getLocatorString(String locatorKeyInXML,String str)//created by devan for handling angular js dropdown
	{
		try 
		{
			
			String locatorName = locatorKeyInXML.split("\\.")[1];
			String pageName = locatorKeyInXML.split("\\.")[0];
			String locatorValue = "";
			String locatorValueConcat="";
		
			boolean isLocatorPresent = false;
			
			Document xmlDocument = readXML(ojectRepositoryFilePath + pageName + ".xml");

			if (xmlDocument != null) 
			{
				NodeList locatorNodes = xmlDocument.getElementsByTagName("locator");

				for (int nodeCounter = 0; nodeCounter < locatorNodes.getLength(); nodeCounter++) 
				{
					Element object = (Element) locatorNodes.item(nodeCounter);

					if (object.getAttribute("name").equalsIgnoreCase(locatorName)) 
					{
						locatorValue = object.getElementsByTagName("value").item(0).getTextContent().concat(str);
						locatorValueConcat=locatorValue.concat("\")]");
						isLocatorPresent = true;
					//	System.out.println("Locator Name: " + locatorName+	 " Value: "+ locatorValueSplit + " ");
						break;
					}
				}
			}
			if (isLocatorPresent)
				return locatorValueConcat;
			else
				return "";
			
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
	public String getTestData(String testDataValueInXML)
	{
		try 
		{
			String locatorName = testDataValueInXML.split("\\.")[1];
			String pageName = testDataValueInXML.split("\\.")[0];
			String locatorValue = "";
			boolean isLocatorPresent = false;
			
			Document xmlDocument = readXML(testDataFilePath + pageName + ".xml");

			if (xmlDocument != null) 
			{
				NodeList locatorNodes = xmlDocument.getElementsByTagName("data");

				for (int nodeCounter = 0; nodeCounter < locatorNodes.getLength(); nodeCounter++) 
				{
					Element object = (Element) locatorNodes.item(nodeCounter);

					if (object.getAttribute("name").equalsIgnoreCase(locatorName)) 
					{
						locatorValue = object.getElementsByTagName("value").item(0).getTextContent();
						isLocatorPresent = true;
						//System.out.println("Locator Name: " + locatorName+	 " Value: "+ locatorValue + " ");
						break;
					}
				}
			}
			if (isLocatorPresent)
				return locatorValue;
			else
				return "";
			
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
	public String getTestaDataFromXML(String testFileName,String testScriptName,String dataKey) 
	{
		String dataValue = "";
		boolean isDataPresent = false;
		Document xmlDocument = readXML(testDataFilePath + testFileName);

		if (xmlDocument != null) {
			NodeList testScriptNodes = xmlDocument.getElementsByTagName("testscript");
			for (int nodeCounter = 0; nodeCounter < testScriptNodes.getLength(); nodeCounter++) {
				Element object = (Element) testScriptNodes.item(nodeCounter);
	
				if (object.getAttribute("name").equalsIgnoreCase(testScriptName)) {
				NodeList dataNodes = object.getElementsByTagName("data");
				
				for (int childNodeCounter = 0; childNodeCounter < dataNodes.getLength(); childNodeCounter++) {	
					
					Element childobject = (Element) dataNodes.item(childNodeCounter);
					if(childobject.getAttribute("key").equalsIgnoreCase(dataKey)){
					dataValue = childobject.getAttribute("value");
					isDataPresent = true;
					break;
					}
				}
				}
			}
		}
		if (isDataPresent)
			return dataValue;
		else
			return "";
	}
	
	public String getConfigurationData(String locatorKeyInXML)
	{
		try 
		{
			String locatorName = locatorKeyInXML.split("\\.")[1];
			String pageName = locatorKeyInXML.split("\\.")[0];
			String locatorValue = "";
			boolean isLocatorPresent = false;
			
			Document xmlDocument = readXML(configurationFilePath + pageName + ".xml");

			if (xmlDocument != null) 
			{
				NodeList locatorNodes = xmlDocument.getElementsByTagName("configuration");

				for (int nodeCounter = 0; nodeCounter < locatorNodes.getLength(); nodeCounter++) 
				{
					Element object = (Element) locatorNodes.item(nodeCounter);

					if (object.getAttribute("name").equalsIgnoreCase(locatorName)) 
					{
						locatorValue = object.getElementsByTagName("value").item(0).getTextContent();
						isLocatorPresent = true;
						//System.out.println("Locator Name: " + locatorName+	 " Value: "+ locatorValue + " ");
						break;
					}
				}
			}
			if (isLocatorPresent)
				return locatorValue;
			else
				return "";
			
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
	public Document readXML(String filePath) 
	{
		try 
		{
			Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filePath));
			xmlDocument.getDocumentElement().normalize();
			return xmlDocument;
		} 
		catch (Exception e) 
		{
			return null;
		}
	}
}
