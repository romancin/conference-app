package org.ale.thot.selenium;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CurrentSessionsTest {
	
	private static String serverUrl = "http://localhost:8080/thot-app";
	private static final String SYSTEM_PROPERTY = "server.url";
	private WebDriver driver;
	private String baseUrl;
	
	
	@Before
	public void setUp(){
		driver = new FirefoxDriver();
		baseUrl = System.getProperty(SYSTEM_PROPERTY);
		if(StringUtils.isEmpty(baseUrl)){
			baseUrl = serverUrl;
		}
		
	}
	
	@Test
	public void openCurrentSessionsPage(){
		open("/currentSessions");
		driver.findElement(By.id("current_sessions"));
		
	}

	private void open(String url) {
		driver.navigate().to(baseUrl+url);
	}
	
	@After
	public void tearDownWebDriver(){
		driver.close();
	}
}
