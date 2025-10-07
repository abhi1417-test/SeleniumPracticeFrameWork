package com.qa.opencart.pages;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.constant.AppConstant;
import com.qa.opencart.util.ElementUtil;

public class LoginPage{
	
	private WebDriver driver;
	private ElementUtil eleUtil;

	// private By locators: page objects
	private final By emailID = By.id("input-email");
	private final By password = By.id("input-password");
	private final By loginBtn = By.xpath("//input[@value='Login']");
	private final By forgotPwdLink = By.linkText("Forgotten Password");
	private final By header = By.tagName("h2");
	private final By registerLink = By.linkText("Register");


	private final By loginErrorMessg = By.cssSelector("div.alert.alert-danger.alert-dismissible");

	//private static final Logger log = LogManager.getLogger(LoginPage.class);

	// public constructor
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		eleUtil = new ElementUtil(driver);

	}

	// public page methods/actions

	
	public String getLoginPageTitle() {
		String title = eleUtil.waitForTitleIs(AppConstant.LOGIN_PAGE_TITLE, AppConstant.DEFAULT_SHORT_WAIT);
		// System.out.println("login page title: "+ title);
		//log.info("login page title: " + title);
		return title;
	}

	
	public String getLoginPageURL() {
		String url = eleUtil.waitForURLContains(AppConstant.LOGIN_PAGE_FRACTION_URL, AppConstant.DEFAULT_SHORT_WAIT);
		// System.out.println("login page url: "+ url);
		//log.info("login page url : " + url);
		return url;
	}

	
	public boolean isForgotPwdLinkExist() {
		return eleUtil.isElementDisplayed(forgotPwdLink);
	}

	
	public boolean isheaderExist() {
		return eleUtil.isElementDisplayed(header);
	}

	
	public AccountPage doLogin(String appUsername, String appPassword) {
		//log.info("application credentials: " + appUsername + " : " + "*********");
		eleUtil.waitForElementVisible(emailID, AppConstant.DEFAULT_MEDIUM_WAIT).sendKeys(appUsername);
		eleUtil.doSendKeys(password, appPassword);
		eleUtil.doClick(loginBtn);
		return new AccountPage(driver);
	}

	
	public boolean doLoginWithInvalidCredentails(String invalidUN, String invalidPWD) {
		//log.info("Invalid application credentials: " + invalidUN + " : " + invalidPWD);
		WebElement emailEle = eleUtil.waitForElementVisible(emailID, AppConstant.DEFAULT_MEDIUM_WAIT);
		emailEle.clear();
		emailEle.sendKeys(invalidUN);
		eleUtil.doSendKeys(password, invalidPWD);
		eleUtil.doClick(loginBtn);
		String errorMessg = eleUtil.doElementGetText(loginErrorMessg);
		//log.info("invalid creds error messg: " + errorMessg);
		if (errorMessg.contains(AppConstant.LOGIN_BLANK_CREDS_MESSG)) {
			return true;
		}
		else if (errorMessg.contains(AppConstant.LOGIN_INVALID_CREDS_MESSG)) {
			return true;
		}
		return false;
	}

	
	public RegitserLoginPage navigateToRegisterPage() {
		//log.info("trying to navigating to register page...");
		eleUtil.waitForElementVisible(registerLink, AppConstant.DEFAULT_SHORT_WAIT).click();
		return new RegitserLoginPage(driver);
	}
	
	 

}
