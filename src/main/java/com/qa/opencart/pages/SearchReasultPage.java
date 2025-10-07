package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.util.ElementUtil;

public class SearchReasultPage {
	private WebDriver driver;
	private ElementUtil eleUtil;
	
	public SearchReasultPage(WebDriver driver) {
		this.driver = driver;
		eleUtil = new ElementUtil(driver);
	}
	
	private final By searcResults = By.cssSelector("div.product-thumb");
	private final By resultHeader = By.cssSelector("div#content h1");
	
	
	
	public int getSearchResultCount() {
		
	int resultCount	= 
			
			eleUtil.waitForElementsPresence(searcResults, 5).size();
	System.out.println("");
		return resultCount; 
	}
	
	
	public String getResultHeaderValue() {
		String header = eleUtil.doElementGetText(resultHeader);
		return header;
	}
	
	public ProductInfoPage selectProduct(String productName) {
		System.out.println("product name-----> "+ productName);
		eleUtil.doClick(By.linkText(productName));
		return new ProductInfoPage(driver);
	}
	

}
