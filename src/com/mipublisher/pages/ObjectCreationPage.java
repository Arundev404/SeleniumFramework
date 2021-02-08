package com.mipublisher.pages;

import com.mipublisher.core.DefaultPage;

public class ObjectCreationPage extends DefaultPage{
	
	public PageLogin pgLogin;
	
	
	
		
	public ObjectCreationPage()  {
		super();
		pgLogin = new PageLogin();	
		
	}
}
