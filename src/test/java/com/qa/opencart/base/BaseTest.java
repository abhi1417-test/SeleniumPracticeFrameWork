package com.qa.opencart.base;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.pages.AccountPage;
import com.qa.opencart.pages.CommonsPage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.ProductInfoPage;
import com.qa.opencart.pages.RegitserLoginPage;
import com.qa.opencart.pages.SearchReasultPage;
//import com.aventstack.chaintest.plugins.ChainTestListener;


//@Listeners(ChainTestListener.class)
//@Listeners({ChainTestListener.class,TestAllureListener.class})
public class BaseTest {
	protected WebDriver driver;
	protected Properties prop;
	protected DriverFactory df;
	 protected LoginPage loginPage;
	 protected AccountPage accPage;
	 protected SearchReasultPage  searchResultPage;
	 protected ProductInfoPage productInfoPage;
	 protected RegitserLoginPage registerPage;
	 protected CommonsPage commonsPage;
	 
	 
	 @Parameters({"browser"})
	@BeforeTest
	public void setUp(@Optional("chrome")String browserName) {
		
		
		df = new DriverFactory ();
		prop = df.initProp();
		System.out.println("Properties loaded: " + prop);
		
		if(browserName!=null)
		{
			prop.setProperty("browser", browserName);
		}

	   driver= df.initDriver(prop);
		loginPage = new LoginPage(driver);
		commonsPage = new CommonsPage(driver);
		
	}
	 
	 @AfterMethod // will be running after each @test method
		public void attachScreenshot(ITestResult result) {
			
			if (!result.isSuccess()) {// only for failure test cases -- true
				ChainTestListener.embed(DriverFactory.getScreenshotFile(), "image/png");
			}

			//ChainTestListener.embed(DriverFactory.getScreenshotFile(), "image/png");

		}
	
	@AfterTest
	public void tearDown() {
		driver.quit();
		
	}

}
