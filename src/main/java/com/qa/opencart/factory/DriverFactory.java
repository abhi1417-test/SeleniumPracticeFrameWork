package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
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
		
		boolean remoteExeution = Boolean.parseBoolean(prop.getProperty("remote"));

		switch (browserName.trim().toLowerCase()) {
		case "chrome":
			if (remoteExeution) {
				// run tcs on remote - grid
				init_remoteDriver("chrome");
			} else {
				// run tcs in local
				tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			}
			break;
		case "firefox":
			if (remoteExeution) {
				// run tcs on remote - grid
				init_remoteDriver("firefox");
			} else {
				// run tcs in local
				tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			}
			break;
		case "edge":
			if (remoteExeution) {
				// run tcs on remote - grid
				init_remoteDriver("edge");
			} else {
				// run tcs in local
				tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
			}
			break;
		case "safari":
			tlDriver.set(new SafariDriver());
			break;
		default:
			log.error(AppError.INVALID_BROWSER_MESG + " : " + browserName);
			FrameworkException fe = new FrameworkException(AppError.INVALID_BROWSER_MESG + " : " + browserName);
			log.error("Exception occurred while initializing driver: ", fe);
			throw new FrameworkException("=====INVALID BROWSER====");

		}
		
		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().fullscreen();
		getDriver().get(prop.getProperty("url"));
		
		return getDriver();
		
	}
	
	private void init_remoteDriver(String browserName) {
		log.info("Running tests on selenoum grid --"+ browserName);

		try {
			switch (browserName) {
			case "chrome":
				tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getChromeOptions()));
				break;
				
			case "firefox":
				tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getFirefoxOptions()));
				break;
				
			case "edge":
				tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getEdgeOptions()));
				break;
				
			default:
				log.error("Plz supply the right browser name for selenium grid....");
				FrameworkException fe = new FrameworkException(AppError.INVALID_BROWSER_MESG + " : " + browserName);
				log.error("Exception occurred while initializing driver: ", fe);
				throw new FrameworkException("=====INVALID BROWSER====");
			}
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		}

		
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
			ip = new FileInputStream("./src/test/resources/config/config.qa.properties");
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
				ip = new FileInputStream("./src/test/resources/config/config.properties");
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
