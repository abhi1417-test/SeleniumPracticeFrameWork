package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.qa.opencart.error.AppError;
import com.qa.opencart.exception.FrameworkException;

public class DriverFactory {
	
	public WebDriver driver;
	public Properties prop;
	public static String highlightEle;
	public OptionsManager optionsManager;
	
	public static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();
	private static final Logger log = LogManager.getLogger(DriverFactory.class);
	/**
	 * This method is returning browser 
	 * @param browserName
	 * @return
	 */
	
	public WebDriver initDriver(Properties prop) {
		
		String browserName = prop.getProperty("browser");
		log.info("browser Name  :" +  browserName);
		//System.out.println("browser Name  :" +  browserName);
		
		highlightEle = prop.getProperty("highlight");
		optionsManager = new OptionsManager(prop);
		
		switch(browserName.trim().toLowerCase()) {
		
		case "chrome":
			//driver = new ChromeDriver();
			tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			break;
			
		case "firefox":
			//driver = new FirefoxDriver();
			tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			break;
		case "edge":
			//driver = new EdgeDriver();
			tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
			
			break;
		case "safari":
			//driver = new SafariDriver();
			tlDriver.set(new SafariDriver());
			break;
			
		default:
			//System.out.println(AppError.INVALID_BROWSER_MESG + ":" + browserName);
			log.error(AppError.INVALID_BROWSER_MESG + ":" + browserName);
			
			throw new FrameworkException("===INVALID BROWSER====");
			
		}
		
		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().fullscreen();
		getDriver().get(prop.getProperty("url"));
		
		return getDriver();
		
	}
	
	/**
	 * this is used to get local copy of the driver anytime..
	 * @return
	 */
	
	public static WebDriver getDriver()
	{
	     return tlDriver.get();
	}
	
	/**
	 * This method is initializing properties file value
	 * @return 
	 */
	
	public Properties initProp() {
		
		prop = new Properties();
		FileInputStream ip = null;
		
		/**
		 * System.getProperty("env") in this env wiil be passed from the maven 
		 *  from cli command  mvn clean install -Denv="qa"
		 * 
		 */
		String envName = System.getProperty("env");
		log.info("Env name  : " + envName);
		
		
		try {
		if(envName == null)
		{
			log.info("no env ... is passes , hence running testcases on QA env...");
			ip = new FileInputStream("./src/test/resources/config/config.properties");
		}
		
		else {
			switch (envName) {
			case "qa":
				ip = new FileInputStream("./src/test/resources/config/config.qa.properties");
				break;
			case "stage":
				ip = new FileInputStream("./src/test/resources/config/config.stage.properties");
				break;
			case "prod":
				ip = new FileInputStream("./src/test/resources/config/config.prod.properties");
				break;
			case "demo":
				ip = new FileInputStream("./src/test/resources/config/config.properties");
				break;
				default:
					log.error("env value is invalid");
			}   
		}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			prop.load(ip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
		
	}
	
	/**
	 * take screen shot
	 * @return
	 */
	
	public static File getScreenshotFile() {
		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);// temp dir
		return srcFile;
	}

	public static byte[] getScreenshotByte() {
		return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);// temp dir

	}

	public static String getScreenshotBase64() {
		return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BASE64);// temp dir

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
