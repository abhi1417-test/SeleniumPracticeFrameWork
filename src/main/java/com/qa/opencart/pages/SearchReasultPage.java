package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.constant.AppConstant;
import com.qa.opencart.util.ElementUtil;

public class SearchReasultPage {

	private WebDriver driver;
	private ElementUtil eleUtil;

	public SearchReasultPage(WebDriver driver) {
		this.driver = driver;
		eleUtil = new ElementUtil(driver);
	}
	
	private final By searchResults = By.cssSelector("div.product-thumb");
	private final By resultsHeader = By.cssSelector("div#content h1");
	
	
	public int getSearchResultsCount() {
		int resultCount = 
				eleUtil.waitForElementsPresence(searchResults, AppConstant.DEFAULT_MEDIUM_WAIT).size();
		System.out.println("results count --->"+ resultCount);
		return resultCount;
	}
	
	public String getResultsHeaderValue() {
		String header = eleUtil.doElementGetText(resultsHeader);
		System.out.println("results header --->"+ header);
		return header;
	}
	
	public ProductInfoPage selectProduct(String productName) {
		System.out.println("prduct name--->"+ productName);
		eleUtil.doClick(By.linkText(productName));
		return new ProductInfoPage(driver);
	}
	
	
	
	
}